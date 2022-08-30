package com.brandonjja.mazegen.commands.handler;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.brandonjja.mazegen.commands.MazeCommand;

public class MazeWandCommand extends MazeCommand {
	private static final ItemStack WAND;

	static {
		WAND = new ItemStack(Material.STICK);
		ItemMeta meta = WAND.getItemMeta();
		meta.setDisplayName("Maze Wand");
		WAND.setItemMeta(meta);
		WAND.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
	}
	
	@Override
	public boolean execute(Player player, String[] args) {
		player.getInventory().addItem(WAND);
		player.sendMessage(ChatColor.GREEN + "Maze Wand has been added to your inventory!");
		return true;
	}
}
