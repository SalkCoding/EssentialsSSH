package com.salkcoding.essentialsssh.io

import com.google.gson.Gson
import com.salkcoding.essentialsssh.data.Home
import com.salkcoding.essentialsssh.data.Spawn
import com.salkcoding.essentialsssh.essentials
import java.io.File
import java.util.*

object JsonReader {

    private val gson = Gson()

    fun loadSpawn(): Spawn? {
        val file = File(essentials.dataFolder, "spawn.json")
        return if (file.exists()) {
            var spawn: Spawn
            file.bufferedReader().use { bufferedReader ->
                spawn = gson.fromJson(bufferedReader, Spawn::class.java)
            }
            spawn
        } else null
    }

    fun loadHome(uuid: UUID): Home? {
        val folder = File(essentials.dataFolder, "home")
        return if (folder.exists()) {
            val file = File(folder, "${uuid}.json")
            if (file.exists()) {
                var home: Home
                file.bufferedReader().use { bufferedReader ->
                    home = gson.fromJson(bufferedReader, Home::class.java)
                }
                home
            } else null
        } else null
    }
}