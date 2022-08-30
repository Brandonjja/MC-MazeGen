package com.brandonjja.mazegen.commands.handler;

import org.bukkit.entity.Player;

import com.brandonjja.mazegen.api.MazeAPI;
import com.brandonjja.mazegen.commands.MazeCommand;

public class GenerateCommand extends MazeCommand {

	@Override
	public boolean execute(Player player, String[] args) {
		if (args.length != 1) {
			return false;
		}

		try {
			int size = Integer.parseInt(args[0]);
			MazeAPI.getInstance().generateMaze(size);
		} catch (NumberFormatException ex) {
			player.sendMessage("Please enter a valid path width.");
		}

		return true;
	}

}
