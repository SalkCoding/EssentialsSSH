package com.salkcoding.essentialsssh.command

import com.salkcoding.essentialsssh.currentServerName
import com.salkcoding.essentialsssh.essentials
import com.salkcoding.essentialsssh.homeManager
import com.salkcoding.essentialsssh.util.infoFormat
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class CommandSetHome : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) {
            essentials.logger.warning("Player only command")
            return true
        }

        val location = sender.location
        homeManager.setHome(
            sender.uniqueId,
            currentServerName,
            location.world.name,
            location.x,
            location.y,
            location.z,
            location.yaw,
            location.pitch
        )
        sender.sendMessage("해당 위치를 홈으로 설정했습니다".infoFormat())

        return true
    }
}