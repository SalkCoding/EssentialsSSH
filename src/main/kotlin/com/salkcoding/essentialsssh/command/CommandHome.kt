package com.salkcoding.essentialsssh.command

import com.salkcoding.essentialsssh.bukkitLinkedAPI
import com.salkcoding.essentialsssh.essentials
import com.salkcoding.essentialsssh.homeManager
import com.salkcoding.essentialsssh.util.TeleportCooltime
import com.salkcoding.essentialsssh.util.errorFormat
import com.salkcoding.essentialsssh.util.infoFormat
import me.baiks.bukkitlinked.api.TeleportResult
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class CommandHome : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) {
            essentials.logger.warning("Player only command")
            return true
        }

        val home = homeManager.getHome(sender.uniqueId)
        if (home == null) {
            sender.sendMessage("저장된 홈이 없습니다!".errorFormat())
            return true
        }

        if (home.isSameServer) {
            if (sender.isOp) sender.teleportAsync(home.getLocation()!!)
            else TeleportCooltime.addPlayer(
                sender,
                home.getLocation()!!,
                100,
                { sender.sendMessage("이동되었습니다.".infoFormat()) },
                false
            )
        } else {
            val teleportRunnable = Runnable {
                val result = bukkitLinkedAPI.teleport(
                    sender.name, home.serverName, home.worldName,
                    home.x.toInt(), home.y.toInt(), home.z.toInt()
                )
                if(result != TeleportResult.TELEPORT_STARTED){
                    essentials.logger.warning("${sender.name} teleport to spawn fail!: $result")
                }
            }
            if (sender.isOp) Bukkit.getScheduler().runTaskAsynchronously(essentials, teleportRunnable)
            else TeleportCooltime.addPlayer(sender, null, 100, teleportRunnable, true)
        }
        return true
    }
}