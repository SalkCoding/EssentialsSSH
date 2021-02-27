package com.salkcoding.essentialsssh.bungee.receiver

import com.google.common.io.ByteStreams
import com.salkcoding.essentialsssh.*
import com.salkcoding.essentialsssh.bungee.channelapi.BungeeChannelApi
import com.salkcoding.essentialsssh.util.infoFormat
import com.salkcoding.essentialsssh.util.warnFormat
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.io.ByteArrayOutputStream
import java.io.DataOutputStream
import java.io.IOException
import java.util.*

class CommandReceiver : BungeeChannelApi.ForwardConsumer {

    override fun accept(channel: String, receiver: Player, data: ByteArray) {
        val inMessage = ByteStreams.newDataInput(data)
        when (channel) {
            "essentials-home" -> {
                Bukkit.getScheduler().runTaskAsynchronously(essentials, Runnable {
                    val playerName = inMessage.readUTF()
                    val playerUUID = UUID.fromString(inMessage.readUTF())
                    val serverName = inMessage.readUTF()

                    val messageBytes = ByteArrayOutputStream()
                    val messageOut = DataOutputStream(messageBytes)
                    try {
                        messageOut.writeUTF(playerName)
                        messageOut.writeUTF(playerUUID.toString())
                        messageOut.writeUTF(currentServerName)
                        messageOut.writeBoolean(homeManager.hasHome(playerUUID))
                    } catch (exception: IOException) {
                        exception.printStackTrace()
                    } finally {
                        messageOut.close()
                    }
                    bungeeApi.forward(serverName, "essentials-home-receive", messageBytes.toByteArray())
                })
            }
            "essentials-home-teleport" -> {
                Bukkit.getScheduler().runTaskAsynchronously(essentials, Runnable {
                    val playerName = inMessage.readUTF()
                    val playerUUID = UUID.fromString(inMessage.readUTF())
                    val home = homeManager.getHome(playerUUID)!!
                    if (home.isSameServer) {
                        bungeeApi.connectOther(playerName, currentServerName)
                        Bukkit.getScheduler().runTaskLater(essentials, Runnable {
                            val player = Bukkit.getPlayer(playerName)
                            if (player != null) {
                                player.teleportAsync(home.getLocation()!!)
                                player.sendMessage("이동되었습니다.".infoFormat())
                            } else essentials.logger.warning("$playerName teleport to home failed.(Player is not existed)")
                        }, 15)
                    } else {
                        bungeeApi.connectOther(playerName, home.serverName)

                        val messageBytes = ByteArrayOutputStream()
                        val messageOut = DataOutputStream(messageBytes)
                        try {
                            messageOut.writeUTF(playerName)
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
                })
            }
            "essentials-spawn" -> {
                Bukkit.getScheduler().runTaskAsynchronously(essentials, Runnable {
                    val playerName = inMessage.readUTF()
                    val serverName = inMessage.readUTF()

                    val messageBytes = ByteArrayOutputStream()
                    val messageOut = DataOutputStream(messageBytes)
                    try {
                        messageOut.writeUTF(playerName)
                        messageOut.writeUTF(currentServerName)
                    } catch (exception: IOException) {
                        exception.printStackTrace()
                    } finally {
                        messageOut.close()
                    }
                    bungeeApi.forward(serverName, "essentials-spawn-receive", messageBytes.toByteArray())
                })
            }
            "essentials-spawn-teleport" -> {
                Bukkit.getScheduler().runTaskLater(essentials, Runnable {
                    val playerName = inMessage.readUTF()
                    val player = Bukkit.getPlayer(playerName)
                    if (player == null) {
                        essentials.logger.warning("$playerName teleport to spawn failed.(Player is not existed)")
                        return@Runnable
                    }

                    player.teleportAsync(spawnManager.spawn.getLocation())
                }, 15)
            }
            "essentials-sethome" -> {
                val playerUUID = UUID.fromString(inMessage.readUTF())
                val serverName = inMessage.readUTF()
                val worldName = inMessage.readUTF()
                val x = inMessage.readDouble()
                val y = inMessage.readDouble()
                val z = inMessage.readDouble()
                val yaw = inMessage.readFloat()
                val pitch = inMessage.readFloat()
                homeManager.setHome(playerUUID, serverName, worldName, x, y, z, yaw, pitch)
            }
            "essentials-sethome-delete" -> {
                val playerName = inMessage.readUTF()
                val playerUUID = UUID.fromString(inMessage.readUTF())
                val serverName = inMessage.readUTF()
                val worldName = inMessage.readUTF()
                val x = inMessage.readDouble()
                val y = inMessage.readDouble()
                val z = inMessage.readDouble()
                if (homeManager.deleteHome(playerUUID, serverName, worldName, x, y, z)) bungeeApi.sendMessage(playerName, "홈이 삭제되었습니다.".warnFormat())
            }
        }
    }
}