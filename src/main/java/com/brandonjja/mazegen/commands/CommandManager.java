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
	private static final Map<String, MazeCommand> COMMAND_LIST = new HashMap<>();

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (!(sender instanceof Player)) {
			return false;
		}

		return COMMAND_LIST.get(commandLabel).execute((Player) sender, args);
	}

	public static void registerCommands() {
		COMMAND_LIST.put("generate", new GenerateCommand());
		COMMAND_LIST.put("mazewand", new MazeWandCommand());

		for (String cmdLabel : COMMAND_LIST.keySet()) {
			register(cmdLabel, new CommandManager());
		}
	}

	private static void register(String cmdLabel, CommandExecutor command) {
		Bukkit.getPluginCommand(cmdLabel).setExecutor(command);
	}
}
