package com.salkcoding.essentialsssh.command

import com.salkcoding.essentialsssh.bungeeApi
import com.salkcoding.essentialsssh.essentials
import com.salkcoding.essentialsssh.homeManager
import com.salkcoding.essentialsssh.util.TeleportCooltime
import com.salkcoding.essentialsssh.util.errorFormat
import com.salkcoding.essentialsssh.util.infoFormat
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.io.ByteArrayOutputStream
import java.io.DataOutputStream
import java.io.IOException

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
            val runnable = Runnable {
                bungeeApi.connect(sender, home.serverName)

                val messageBytes = ByteArrayOutputStream()
                val messageOut = DataOutputStream(messageBytes)
                try {
                    messageOut.writeUTF(sender.name)
                    messageOut.writeUTF(home.worldName)
                    messageOut.writeDouble(home.x)
                    messageOut.writeDouble(home.y)
                    messageOut.writeDouble(home.z)
                    messageOut.writeFloat(home.yaw)
                    messageOut.writeFloat(home.pitch)
                } catch (exception: IOException) {
                    exception.printStackTrace()
                } finally {
                    messageOut.close()
                }
                bungeeApi.forward(home.serverName, "essentials-home-teleport", messageBytes.toByteArray())
            }
            if (sender.isOp) Bukkit.getScheduler().runTaskAsynchronously(essentials, runnable)
            else TeleportCooltime.addPlayer(sender, null, 100, runnable, true)
        }
        return true
    }
}