package com.salkcoding.essentialsssh

import com.salkcoding.essentialsssh.bungee.channelapi.BungeeChannelApi
import com.salkcoding.essentialsssh.bungee.receiver.CommandReceiver
import com.salkcoding.essentialsssh.command.CommandHome
import com.salkcoding.essentialsssh.command.CommandSetHome
import com.salkcoding.essentialsssh.command.CommandSetSpawn
import com.salkcoding.essentialsssh.command.CommandSpawn
import com.salkcoding.essentialsssh.data.HomeManager
import com.salkcoding.essentialsssh.data.SpawnManager
import com.salkcoding.essentialsssh.listener.BedListener
import com.salkcoding.essentialsssh.listener.PlayerConnectionListener
import com.salkcoding.essentialsssh.listener.PlayerRespawnListener
import org.bukkit.plugin.java.JavaPlugin

lateinit var essentials: EssentialsSSH
lateinit var bungeeApi: BungeeChannelApi
lateinit var spawnManager: SpawnManager
lateinit var homeManager: HomeManager
lateinit var currentServerName: String
lateinit var enabledWorld: Set<String>

class EssentialsSSH : JavaPlugin() {

    override fun onEnable() {
        essentials = this

        saveDefaultConfig()
        currentServerName = config.getString("serverName")!!
        enabledWorld = config.getList("enabledWorld")!!.toSet() as Set<String>

        bungeeApi = BungeeChannelApi.of(this)

        val receiver = CommandReceiver()
        bungeeApi.registerForwardListener("essentials-home", receiver)
        bungeeApi.registerForwardListener("essentials-home-teleport", receiver)
        bungeeApi.registerForwardListener("essentials-spawn", receiver)
        bungeeApi.registerForwardListener("essentials-spawn-teleport", receiver)
        bungeeApi.registerForwardListener("essentials-sethome", receiver)
        bungeeApi.registerForwardListener("essentials-sethome-delete", receiver)

        spawnManager = SpawnManager()
        homeManager = HomeManager()

        spawnManager.load()

        getCommand("home")!!.setExecutor(CommandHome())
        getCommand("sethome")!!.setExecutor(CommandSetHome())
        getCommand("spawn")!!.setExecutor(CommandSpawn())
        getCommand("setspawn")!!.setExecutor(CommandSetSpawn())

        server.pluginManager.registerEvents(BedListener(), this)
        server.pluginManager.registerEvents(PlayerConnectionListener(), this)
        server.pluginManager.registerEvents(PlayerRespawnListener(), this)

        logger.info("Plugin enabled")
    }

    override fun onDisable() {
        spawnManager.save()
        homeManager.saveAllAndUnload()

        logger.warning("Plugin disabled")
    }
}