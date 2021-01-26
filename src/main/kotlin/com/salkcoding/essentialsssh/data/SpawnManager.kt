package com.salkcoding.essentialsssh.data

import br.com.devsrsouza.kotlinbukkitapi.extensions.world.mainWorld
import com.salkcoding.essentialsssh.io.JsonReader
import com.salkcoding.essentialsssh.io.JsonWriter
import com.salkcoding.essentialsssh.currentServerName

class SpawnManager {
    lateinit var spawn: Spawn

    fun load() {
        val spawn = JsonReader.loadSpawn()
        if (spawn == null) {
            val location = mainWorld().spawnLocation
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