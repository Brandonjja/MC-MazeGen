package com.brandonjja.mazegen;

import org.bukkit.plugin.java.JavaPlugin;

import com.brandonjja.mazegen.commands.CommandManager;
import com.brandonjja.mazegen.listeners.ListenerManager;

public class MazeGen extends JavaPlugin {
	
	private static MazeGen plugin;
	
	@Override
	public void onEnable() {
		plugin = this;
		
		CommandManager.registerCommands();
		ListenerManager.registerListeners();
	}
	
	@Override
	public void onDisable() {
		plugin = null;
	}
	
	public static MazeGen getPlugin() {
		return plugin;
	}
}
