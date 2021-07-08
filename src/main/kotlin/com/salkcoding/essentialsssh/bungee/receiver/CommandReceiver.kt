package com.salkcoding.essentialsssh.bungee.receiver

import com.google.gson.JsonParser
import com.salkcoding.essentialsssh.bukkitLinkedAPI
import com.salkcoding.essentialsssh.essentials
import com.salkcoding.essentialsssh.homeManager
import com.salkcoding.essentialsssh.spawnManager
import com.salkcoding.essentialsssh.util.errorFormat
import com.salkcoding.essentialsssh.util.infoFormat
import fish.evatuna.metamorphosis.kafka.KafkaReceiveEvent
import me.baiks.bukkitlinked.api.TeleportResult
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import java.util.*

class CommandReceiver : Listener {

    @EventHandler
    fun onReceived(event: KafkaReceiveEvent) {
        if (!event.key.startsWith("com.salkcoding.essentialsssh")) return
        val json = JsonParser().parse(event.value).asJsonObject
        val uuid = UUID.fromString(json["uuid"].asString)
        //Split a last sub key
        when (event.key.split(".").last()) {
            "home" -> {
                Bukkit.getScheduler().runTaskAsynchronously(essentials, Runnable {
                    val hasHome = homeManager.hasHome(uuid)
                    if (hasHome) {
                        val home = homeManager.getHome(uuid)!!
                        val name = json["name"].asString
                        val result = bukkitLinkedAPI.teleport(
                            name, home.serverName, home.worldName,
                            home.x.toInt(), home.y.toInt(), home.z.toInt()
                        )
                        if (result != TeleportResult.TELEPORT_STARTED) {
                            essentials.logger.warning("$name teleport to home fail!: $result")
                        }
                    } else bukkitLinkedAPI.sendMessageAcrossServer(uuid, "지정된 홈이 없습니다!".errorFormat())
                })
            }
            "sethome" -> {
                val serverName = json["serverName"].asString
                val worldName = json["worldName"].asString
                val x = json["x"].asDouble
                val y = json["y"].asDouble
                val z = json["z"].asDouble
                val yaw = json["yaw"].asFloat
                val pitch = json["pitch"].asFloat
                homeManager.setHome(uuid, serverName, worldName, x, y, z, yaw, pitch)
            }
            "delhome" -> {
                val serverName = json["serverName"].asString
                val worldName = json["worldName"].asString
                val x = json["x"].asDouble
                val y = json["y"].asDouble
                val z = json["z"].asDouble
                if (homeManager.deleteHome(uuid, serverName, worldName, x, y, z)) {
                    bukkitLinkedAPI.sendMessageAcrossServer(uuid, "홈이 삭제되었습니다.".infoFormat())
                }
            }
            "spawn" -> {
                Bukkit.getScheduler().runTaskAsynchronously(essentials, Runnable {
                    val name = json["name"].asString
                    val spawn = spawnManager.spawn
                    val result = bukkitLinkedAPI.teleport(
                        name, spawn.serverName, spawn.worldName,
                        spawn.x.toInt(), spawn.y.toInt(), spawn.z.toInt()
                    )
                    if (result != TeleportResult.TELEPORT_STARTED) {
                        essentials.logger.warning("$name teleport to spawn fail!: $result")
                    }
                })
            }
            "respawn" -> {
                val name = json["name"].asString
                val spawn = spawnManager.spawn
                val result = bukkitLinkedAPI.teleport(
                    name, spawn.serverName, spawn.worldName, spawn.x.toInt(),
                    spawn.y.toInt(), spawn.z.toInt()
                )
                if (result != TeleportResult.TELEPORT_STARTED) {
                    essentials.logger.warning("$name respawn fail!: $result")
                }
            }
        }
    }
}