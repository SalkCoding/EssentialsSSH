package com.salkcoding.essentialsssh.listener

import com.salkcoding.essentialsssh.essentials
import com.salkcoding.essentialsssh.spawnManager
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerRespawnEvent

class PlayerRespawnListener : Listener {

    @EventHandler
    fun onRespawn(event: PlayerRespawnEvent) {
        Bukkit.getScheduler().runTaskLater(essentials, Runnable {
            event.player.sendTitle("\ue405", "", 0, 20, 30)
        }, 2)
        event.respawnLocation = spawnManager.spawn.getLocation()
    }
}