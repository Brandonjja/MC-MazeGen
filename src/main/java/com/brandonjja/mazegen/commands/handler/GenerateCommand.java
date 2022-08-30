package com.brandonjja.mazegen.commands.handler;

import org.bukkit.entity.Player;

import com.brandonjja.mazegen.api.MazeAPI;
import com.brandonjja.mazegen.commands.MazeCommand;

import net.md_5.bungee.api.ChatColor;

public class GenerateCommand extends MazeCommand {
	
	final String invalidBlock = ChatColor.RED + "Please enter a valid block";
	
	@Override
	public boolean execute(Player player, String[] args) {
		if (args.length != 1) return false;
		try {
			MazeAPI.generateMaze(args[0]);
		} catch (NullPointerException ex) {
			ex.printStackTrace();
			return false;
		} catch (IllegalArgumentException ex) {
			player.sendMessage(invalidBlock);
			return true;
		}
		return true;
	}

}
