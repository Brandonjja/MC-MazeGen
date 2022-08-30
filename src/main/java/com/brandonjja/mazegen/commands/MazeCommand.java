package com.brandonjja.mazegen.commands;

import org.bukkit.entity.Player;

public abstract class MazeCommand {
	public abstract boolean execute(Player player, String args[]);
}
