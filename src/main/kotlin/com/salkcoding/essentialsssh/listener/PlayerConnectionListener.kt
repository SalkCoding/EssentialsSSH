package com.salkcoding.essentialsssh.listener

import com.salkcoding.essentialsssh.homeManager
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

class PlayerConnectionListener : Listener {

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        homeManager.load(event.player.uniqueId)
    }

    @EventHandler
    fun onQuit(event: PlayerQuitEvent) {
        homeManager.saveAndUnload(event.player.uniqueId)
    }
}