package com.salkcoding.essentialsssh.data

import org.bukkit.Bukkit
import org.bukkit.Location

class Spawn(
    val serverName: String,
    val worldName: String,
    val x: Double,
    val y: Double,
    val z: Double,
    val yaw: Float,
    val pitch: Float
) {
    @Transient val location = Location(Bukkit.getWorld(worldName), x, y, z, yaw, pitch)
}