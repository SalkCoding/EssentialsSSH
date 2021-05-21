package com.salkcoding.essentialsssh.listener

import com.salkcoding.essentialsssh.spawnManager
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerRespawnEvent

class PlayerRespawnListener : Listener {

    @EventHandler
    fun onRespawn(event: PlayerRespawnEvent) {
        event.player.sendTitle("\ue405", "", 0, 10, 30)
        event.respawnLocation = spawnManager.spawn.getLocation()
    }
}