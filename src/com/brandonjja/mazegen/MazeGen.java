package com.brandonjja.mazegen;

import org.bukkit.plugin.java.JavaPlugin;

import com.brandonjja.mazegen.commands.CommandManager;

public class MazeGen extends JavaPlugin {
	
	private static MazeGen plugin;
	
	@Override
	public void onEnable() {
		plugin = this;
		
		CommandManager.registerCommands();
	}
	
	@Override
	public void onDisable() {
		plugin = null;
	}
	
	public static MazeGen getPlugin() {
		return plugin;
	}
}
