package com.salkcoding.essentialsssh.command

import com.salkcoding.essentialsssh.essentials
import com.salkcoding.essentialsssh.spawnManager
import com.salkcoding.essentialsssh.util.TeleportCooltime
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class CommandSpawn : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) {
            essentials.logger.warning("Player only command")
            return true
        }

        val spawn = spawnManager.spawn.getLocation()
        if (sender.isOp) sender.teleportAsync(spawn)
        else TeleportCooltime.addPlayer(sender, spawn, 100, null, false)
        return true
    }
}