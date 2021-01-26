package com.salkcoding.essentialsssh.data

import com.salkcoding.essentialsssh.currentServerName
import com.salkcoding.essentialsssh.essentials
import com.salkcoding.essentialsssh.io.JsonReader
import com.salkcoding.essentialsssh.io.JsonWriter
import org.bukkit.Bukkit
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class HomeManager {

    private val homeMap = ConcurrentHashMap<UUID, Home>()

    fun getHome(uuid: UUID): Home? {
        return if (homeMap.containsKey(uuid)) homeMap[uuid]!!
        else null
    }

    fun setHome(
        uuid: UUID,
        serverName: String,
        worldName: String,
        x: Double,
        y: Double,
        z: Double,
        yaw: Float,
        pitch: Float
    ) {
        homeMap[uuid] =
            Home(serverName, worldName, x, y, z, yaw, pitch, (serverName == currentServerName))
    }

    fun hasHome(uuid: UUID): Boolean {
        return homeMap.containsKey(uuid)
    }

    fun deleteHome(
        uuid: UUID,
        serverName: String,
        worldName: String,
        x: Double,
        y: Double,
        z: Double,
    ): Boolean {
        val home = homeMap[uuid] ?: return false
        if (home.equals(serverName, worldName, x, y, z)) {
            homeMap.remove(uuid)
            return true
        }
        return false
    }

    fun load(uuid: UUID) {
        Bukkit.getScheduler().runTaskAsynchronously(essentials, Runnable {
            val home = JsonReader.loadHome(uuid) ?: return@Runnable
            homeMap[uuid] = home
        })
    }

    fun saveAndUnload(uuid: UUID) {
        Bukkit.getScheduler().runTaskAsynchronously(essentials, Runnable {
            JsonWriter.saveHome(uuid, homeMap[uuid]!!)
        })
    }

    fun saveAllAndUnload() {
        Bukkit.getScheduler().runTaskAsynchronously(essentials, Runnable {
            homeMap.forEach {
                JsonWriter.saveHome(it.key, it.value)
            }
        })
    }
}