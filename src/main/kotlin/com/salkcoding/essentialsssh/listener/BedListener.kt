package com.salkcoding.essentialsssh.listener

import com.salkcoding.essentialsssh.currentServerName
import com.salkcoding.essentialsssh.enabledWorld
import com.salkcoding.essentialsssh.homeManager
import com.salkcoding.essentialsssh.util.errorFormat
import com.salkcoding.essentialsssh.util.infoFormat
import com.salkcoding.essentialsssh.util.sendErrorTipMessage
import com.salkcoding.essentialsssh.util.warnFormat
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.event.Event
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.player.PlayerInteractEvent

class BedListener : Listener {

    private val bedSet = setOf(
        Material.BLACK_BED,
        Material.BLUE_BED,
        Material.BROWN_BED,
        Material.CYAN_BED,
        Material.GRAY_BED,
        Material.GREEN_BED,
        Material.LIGHT_BLUE_BED,
        Material.LIGHT_GRAY_BED,
        Material.LIME_BED,
        Material.MAGENTA_BED,
        Material.ORANGE_BED,
        Material.PINK_BED,
        Material.RED_BED,
        Material.WHITE_BED,
        Material.YELLOW_BED,
    )

    @EventHandler
    fun onInteract(event: PlayerInteractEvent) {
        if (event.useInteractedBlock() == Event.Result.DENY || event.useItemInHand() == Event.Result.DENY)
            return

        val player = event.player
        val world = player.world

        if ("nether" in world.name || "ender" in world.name) return

        if (event.action == Action.RIGHT_CLICK_BLOCK && event.clickedBlock != null) {
            if (event.clickedBlock!!.type !in bedSet) return

            if (world.name !in enabledWorld) {
                player.sendErrorTipMessage("${ChatColor.RED}현재 월드에서는 사용할 수 없습니다.".errorFormat())
                return
            }

            val location = event.clickedBlock!!.location
            homeManager.setHome(
                player.uniqueId,
                currentServerName,
                world.name,
                location.x,
                location.y,
                location.z,
                location.yaw,
                location.pitch
            )
            player.sendMessage("홈으로 설정되었습니다.".infoFormat())
            event.isCancelled = true
        }
    }

    @EventHandler
    fun onBreak(event: BlockBreakEvent) {
        if (event.isCancelled) return
        if (event.block.type !in bedSet) return

        val player = event.player
        val location = event.block.location
        if (homeManager.deleteHome(
                player.uniqueId,
                currentServerName,
                location.world.name,
                location.x,
                location.y,
                location.z
            )
        ) player.sendMessage("홈이 삭제되었습니다.".warnFormat())
    }
}