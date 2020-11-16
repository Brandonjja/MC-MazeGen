package com.brandonjja.mazegen.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

import com.brandonjja.mazegen.MazeGen;

public class MazeAPI {
	private static Random random = new Random();
	private static Location locationOne, locationTwo;
	private static List<Block> maze = new ArrayList<>(); // set of blocks already in the maze
	private static List<Block> frontier = new ArrayList<>(); // set of blocks adjacent to a block in the maze, but not yet in the maze itself
	private static List<Block> grid = new ArrayList<>(); // set of blocks to be added to the maze
	
	/**
	 * Sets the first location of the region for maze generation
	 * 
	 * @param location
	 */
	public static void setLocationOne(Location location) {
		locationOne = location;
	}
	
	/**
	 * Sets the second location of the region for maze generation
	 * 
	 * @param location
	 */
	public static void setLocationTwo(Location location) {
		locationTwo = location;
	}
	
	/**
	 * Generates a random maze if there is a region selected
	 * 
	 * @throws NullPointerException Thrown if a full region has not been set
	 */
	public static void generateMaze() throws NullPointerException {
		if (locationOne == null) {
			throw new NullPointerException("locationOne not set");
		} else if (locationTwo == null) {
			throw new NullPointerException("locationTwo not set");
		}
		
		maze.clear();
		frontier.clear();
		grid.clear();
		
		int x1 = Math.min(locationOne.getBlockX(), locationTwo.getBlockX());
		int x2 = Math.max(locationOne.getBlockX(), locationTwo.getBlockX());
		
		int z1 = Math.min(locationOne.getBlockZ(), locationTwo.getBlockZ());
		int z2 = Math.max(locationOne.getBlockZ(), locationTwo.getBlockZ());;
		
		// Ensure there will always be a border
		if (x1 % 2 == 0) x1++;
		if (x2 % 2 == 0) x2++;
		if (z1 % 2 == 0) z1++;
		if (z2 % 2 == 0) z2++;
		
		int centerX = (x1 + x2) / 2;
		int centerZ = (z1 + z2) / 2;
		
		// Ensure the entrance and exits won't be blocked by walls
		if (centerX % 2 != 0) centerX++;
		if (centerZ % 2 != 0) centerZ++;
		
		World world = Bukkit.getWorld("world"); // TODO: parameterize this
		
		// Add all blocks in the selected region to the grid
		for (int i = x1; i <= x2; i++) {
			for (int j = z1; j <= z2; j++) {
				Block b = world.getBlockAt(new Location(world, i, 100, j));
				if (i == x2 && j == centerZ) {
					b.setType(Material.AIR);
				} else if (i == x1 && j == centerZ) {
					b.setType(Material.AIR);
				} else {
					b.setType(Material.DIAMOND_BLOCK);
				}
				
				if (i % 2 != 0) continue;
				if (j % 2 != 0) continue;
				
				grid.add(b);//.setType(Material.DIAMOND_BLOCK);
				//b.setType(Material.EMERALD_BLOCK);
				//b.setType(Material.DIAMOND_BLOCK);
			}
		}
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(MazeGen.getPlugin(), new Runnable() {
			@Override
			public void run() {
				Block point = grid.get(random.nextInt(grid.size())); // Add the first point to the maze
				addNeighborsToFronteir(point);
				maze.add(point);
				grid.remove(point);
				point.setType(Material.AIR);
				
				while (frontier.size() > 0) {
					point = frontier.get(random.nextInt(frontier.size()));
					addToMaze(point);
				}
			}
		}, 1);
		
		
	}
	
	private static void addToMaze(Block point) {
		addNeighborsToFronteir(point);
		
		List<Block> neighborsInMaze = getInMazeNeighbors(point);
		Collections.shuffle(neighborsInMaze);
		Block inMaze = neighborsInMaze.get(0);
		
		int newX = (inMaze.getX() + point.getX()) / 2;
		int newZ = (inMaze.getZ() + point.getZ()) / 2;
		
		Bukkit.getWorld("world").getBlockAt(new Location(point.getWorld(), newX, point.getY(), newZ)).setType(Material.AIR);
		point.setType(Material.AIR);
		
		
		grid.remove(point);
		frontier.remove(point);
		maze.add(point);
	}
	
	/*private static void connectToMaze(Block point) {
		List<Block> neighbors = getInMazeNeighbors(point);
	}*/
	
	private static List<Block> getInMazeNeighbors(Block point) {
		List<Block> inMaze = new ArrayList<>();
		Location location = new Location(point.getWorld(), point.getX(), point.getY(), point.getZ());
		Block block = Bukkit.getWorld("world").getBlockAt(location.add(2, 0, 0));
		if (maze.contains(block)) {
			inMaze.add(block);
		}
		block = Bukkit.getWorld("world").getBlockAt(location.add(-4, 0, 0));
		if (maze.contains(block)) {
			inMaze.add(block);
		}
		block = Bukkit.getWorld("world").getBlockAt(location.add(2, 0, 2));
		if (maze.contains(block)) {
			inMaze.add(block);
		}
		block = Bukkit.getWorld("world").getBlockAt(location.add(0, 0, -4));
		if (maze.contains(block)) {
			inMaze.add(block);
		}
		return inMaze;
	}
	
	private static void addNeighborsToFronteir(Block point) {
		Location location = new Location(point.getWorld(), point.getX(), point.getY(), point.getZ());
		Block block = Bukkit.getWorld("world").getBlockAt(location.add(2, 0, 0));
		if (grid.contains(block) && !maze.contains(block) && !frontier.contains(block)) {
			frontier.add(block);
			grid.remove(block);
		}
		
		block = Bukkit.getWorld("world").getBlockAt(location.add(-4, 0, 0));
		if (grid.contains(block) && !maze.contains(block) && !frontier.contains(block)) {
			frontier.add(block);
			grid.remove(block);
		}
		
		block = Bukkit.getWorld("world").getBlockAt(location.add(2, 0, 2));
		if (grid.contains(block) && !maze.contains(block) && !frontier.contains(block)) {
			frontier.add(block);
			grid.remove(block);
		}
		
		block = Bukkit.getWorld("world").getBlockAt(location.add(0, 0, -4));
		if (grid.contains(block) && !maze.contains(block) && !frontier.contains(block)) {
			frontier.add(block);
			grid.remove(block);
		}
	}
	
	/*private Location newLocation(Location location, int x, int y, int z) {
		Location newLoc = new Location(location.getWorld(), location.getX(), location.getY(), location.getZ());
	}*/
	
	
}
