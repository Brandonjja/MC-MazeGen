package com.brandonjja.mazegen.commands.handler;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.brandonjja.mazegen.commands.MazeCommand;

public class MazeWandCommand extends MazeCommand {
	private static ItemStack item;
	private static String addedItemMsg = ChatColor.GREEN + "Maze Wand has been added to your inventory!";
	static {
		item = new ItemStack(Material.STICK);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("Maze Wand");
		item.setItemMeta(meta);
		item.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
	}
	
	@Override
	public boolean execute(Player player, String[] args) {
		player.getInventory().addItem(item);
		player.sendMessage(addedItemMsg);
		return true;
	}
}
