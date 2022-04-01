package com.salkcoding.essentialsssh.listener

import com.salkcoding.essentialsssh.homeManager
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class PlayerJoinListener: Listener {
    @EventHandler
    fun onJoin(e: PlayerJoinEvent) {
        if (!homeManager.hasHome(e.player.uniqueId)) {
            homeManager.load(e.player.uniqueId)
        }
    }
}