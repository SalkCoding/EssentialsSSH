package com.salkcoding.essentialsssh

import com.salkcoding.essentialsssh.bungee.receiver.CommandReceiver
import com.salkcoding.essentialsssh.command.CommandHome
import com.salkcoding.essentialsssh.command.CommandSetHome
import com.salkcoding.essentialsssh.command.CommandSetSpawn
import com.salkcoding.essentialsssh.command.CommandSpawn
import com.salkcoding.essentialsssh.data.HomeManager
import com.salkcoding.essentialsssh.data.SpawnManager
import com.salkcoding.essentialsssh.listener.BedListener
import com.salkcoding.essentialsssh.listener.PlayerRespawnListener
import fish.evatuna.metamorphosis.Metamorphosis
import me.baiks.bukkitlinked.BukkitLinked
import me.baiks.bukkitlinked.api.BukkitLinkedAPI
import org.bukkit.plugin.java.JavaPlugin

lateinit var essentials: EssentialsSSH
lateinit var metamorphosis: Metamorphosis
lateinit var bukkitLinkedAPI: BukkitLinkedAPI
lateinit var spawnManager: SpawnManager
lateinit var homeManager: HomeManager
lateinit var currentServerName: String
lateinit var enabledWorld: Set<String>

class EssentialsSSH : JavaPlugin() {

    override fun onEnable() {
        essentials = this

        val tempMetamorphosis = server.pluginManager.getPlugin("Metamorphosis") as? Metamorphosis
        if (tempMetamorphosis == null) {
            server.pluginManager.disablePlugin(this)
            logger.warning("Metamorphosis is not running on this server!")
            return
        }
        metamorphosis = tempMetamorphosis

        val tempBukkitLinked = server.pluginManager.getPlugin("BukkitLinked") as? BukkitLinked
        if (tempBukkitLinked == null) {
            server.pluginManager.disablePlugin(this)
            logger.warning("BukkitLinked is not running on this server!")
            return
        }
        bukkitLinkedAPI = tempBukkitLinked.api

        saveDefaultConfig()
        currentServerName = config.getString("serverName")!!
        enabledWorld = config.getList("enabledWorld")!!.toSet() as Set<String>

        spawnManager = SpawnManager()
        homeManager = HomeManager()

        spawnManager.load()

        getCommand("home")!!.setExecutor(CommandHome())
        getCommand("sethome")!!.setExecutor(CommandSetHome())
        getCommand("spawn")!!.setExecutor(CommandSpawn())
        getCommand("setspawn")!!.setExecutor(CommandSetSpawn())

        server.pluginManager.registerEvents(CommandReceiver(), this)

        server.pluginManager.registerEvents(BedListener(), this)
        server.pluginManager.registerEvents(PlayerRespawnListener(), this)

        logger.info("Plugin enabled")
    }

    override fun onDisable() {
        spawnManager.save()
        homeManager.saveAllAndUnload()

        logger.warning("Plugin disabled")
    }
}