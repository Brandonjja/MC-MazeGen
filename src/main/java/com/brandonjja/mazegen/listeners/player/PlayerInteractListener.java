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
	
	private final String firstPositionSet = ChatColor.GREEN + "FIRST Position Set";
	private final String secondPositionSet = ChatColor.GREEN + "SECOND Position Set";
	
	@EventHandler
	public void onStickWand(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		ItemStack item = event.getItem();
		
		if (item == null) {
			return;
		}
		
		ItemMeta meta = item.getItemMeta();
		if (meta == null || !meta.hasDisplayName()) {
			return;
		}
		
		if (item.getType() == Material.STICK && meta.getDisplayName().equals("Maze Wand")) {
			event.setCancelled(true);
			Action action = event.getAction();
			if (action == Action.LEFT_CLICK_BLOCK) {
				player.sendMessage(firstPositionSet);
				MazeAPI.getInstance().setLocationOne(event.getClickedBlock().getLocation());
			} else if (action == Action.RIGHT_CLICK_BLOCK) {
				player.sendMessage(secondPositionSet);
				MazeAPI.getInstance().setLocationTwo(event.getClickedBlock().getLocation());
			}
		}
	}
}
