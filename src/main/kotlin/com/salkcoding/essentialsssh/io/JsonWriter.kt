package com.salkcoding.essentialsssh.io

import com.google.gson.Gson
import com.salkcoding.essentialsssh.data.Home
import com.salkcoding.essentialsssh.data.Spawn
import com.salkcoding.essentialsssh.essentials
import java.io.File
import java.util.*

object JsonWriter {

    private val gson = Gson()

    fun saveSpawn(spawn: Spawn) {
        val file = File(essentials.dataFolder, "spawn.json")
        if (!file.exists())
            file.createNewFile()

        file.bufferedWriter().use { bufferedWriter ->
            gson.toJson(spawn, bufferedWriter)
        }
    }

    fun saveHome(uuid: UUID, home: Home) {
        val folder = File(essentials.dataFolder, "home")
        if (!folder.exists())
            folder.mkdirs()

        val file = File(folder, "${uuid}.json")
        if (!file.exists())
            file.createNewFile()

        file.bufferedWriter().use { bufferedWriter ->
            gson.toJson(home, bufferedWriter)
        }
    }
}