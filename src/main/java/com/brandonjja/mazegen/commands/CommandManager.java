package com.brandonjja.mazegen.commands;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.brandonjja.mazegen.commands.handler.GenerateCommand;
import com.brandonjja.mazegen.commands.handler.MazeWandCommand;

public class CommandManager implements CommandExecutor {
	private static Map<String, MazeCommand> commandList = new HashMap<>();

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (!(sender instanceof Player)) {
			return false;
		}
		return commandList.get(commandLabel).execute((Player) sender, args);
	}
	
	public static void registerCommands() {
		commandList.put("generate", new GenerateCommand());
		commandList.put("mazewand", new MazeWandCommand());
		
		for (String cmdLabel : commandList.keySet()) {
			register(cmdLabel, new CommandManager());
		}
	}
	
	private static void register(String cmdLabel, CommandExecutor command) {
		Bukkit.getPluginCommand(cmdLabel).setExecutor(command);
	}
}
