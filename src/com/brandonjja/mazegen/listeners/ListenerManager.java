package com.brandonjja.mazegen.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import com.brandonjja.mazegen.MazeGen;
import com.brandonjja.mazegen.listeners.player.PlayerInteractListener;

public class ListenerManager {
	public static void registerListeners() {
		register(new PlayerInteractListener());
	}
	
	private static void register(Listener listener) {
		Bukkit.getPluginManager().registerEvents(listener, MazeGen.getPlugin());
	}
}
