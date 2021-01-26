package com.salkcoding.essentialsssh.data

import org.bukkit.Bukkit
import org.bukkit.Location

class Home(
    val serverName: String,
    val worldName: String,
    val x: Double,
    val y: Double,
    val z: Double,
    val yaw: Float,
    val pitch: Float,
    val isSameServer: Boolean
) {
    fun getLocation(): Location? {
        return if (isSameServer) Location(Bukkit.getWorld(worldName), x, y, z, yaw, pitch) else null
    }

    fun equals(
        serverName: String,
        worldName: String,
        x: Double,
        y: Double,
        z: Double,
    ): Boolean {
        return this.serverName == serverName && this.worldName == worldName && this.x == x && this.y == y && this.z == z
    }
}