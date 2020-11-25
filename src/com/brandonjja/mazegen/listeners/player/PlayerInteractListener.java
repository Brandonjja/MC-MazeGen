package com.brandonjja.mazegen.listeners.player;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.brandonjja.mazegen.api.MazeAPI;

public class PlayerInteractListener implements Listener {
	
	final String firstPositionSet = ChatColor.GREEN + "FIRST Position Set";
	final String secondPositionSet = ChatColor.GREEN + "SECOND Position Set";
	
	@EventHandler
	public void onStickWand(PlayerInteractEvent e) {
		Player player = e.getPlayer();
		ItemStack item = e.getItem();
		
		if (item == null) return;
		
		ItemMeta meta = item.getItemMeta();
		if (!meta.hasDisplayName()) return;
		
		if (item.getType() == Material.STICK && item.getItemMeta().getDisplayName().equals("Maze Wand")) {
			e.setCancelled(true);
			Action action = e.getAction();
			if (action == Action.LEFT_CLICK_BLOCK) {
				player.sendMessage(firstPositionSet);
				MazeAPI.setLocationOne(e.getClickedBlock().getLocation());
			} else if (action == Action.RIGHT_CLICK_BLOCK) {
				player.sendMessage(secondPositionSet);
				MazeAPI.setLocationTwo(e.getClickedBlock().getLocation());
			}
		}
	}
}
