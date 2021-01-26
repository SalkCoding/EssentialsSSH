package com.salkcoding.essentialsssh.command

import com.salkcoding.essentialsssh.currentServerName
import com.salkcoding.essentialsssh.data.Spawn
import com.salkcoding.essentialsssh.essentials
import com.salkcoding.essentialsssh.currentServerName
import com.salkcoding.essentialsssh.spawnManager
import com.salkcoding.essentialsssh.util.errorFormat
import com.salkcoding.essentialsssh.util.infoFormat
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class CommandSetSpawn : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) {
            essentials.logger.warning("Player only command")
            return true
        }

        if (!sender.isOp) {
            sender.sendMessage("권한이 없습니다.".errorFormat())
            return true
        }

        val location = sender.location
        spawnManager.spawn = Spawn(
            currentServerName,
            location.world.name,
            location.x,
            location.y,
            location.z,
            location.yaw,
            location.pitch
        )
        sender.sendMessage("해당 위치를 스폰으로 설정했습니다.".infoFormat())

        return true
    }
}