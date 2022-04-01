package com.salkcoding.essentialsssh.data

import com.salkcoding.essentialsssh.io.JsonReader
import com.salkcoding.essentialsssh.io.JsonWriter
import com.salkcoding.essentialsssh.currentServerName
import com.salkcoding.essentialsssh.mainWorld

class SpawnManager {
    lateinit var spawn: Spawn

    fun load() {
        val spawn = JsonReader.loadSpawn()
        if (spawn == null) {
            val location = mainWorld.spawnLocation
            this.spawn = Spawn(
                currentServerName,
                location.world.name,
                location.x,
                location.y,
                location.z,
                location.yaw,
                location.pitch
            )
        } else this.spawn = spawn
    }

    fun save() {
        JsonWriter.saveSpawn(spawn)
    }
}