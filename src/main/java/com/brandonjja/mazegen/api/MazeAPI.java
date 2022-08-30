package com.brandonjja.mazegen.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

import com.brandonjja.mazegen.MazeGen;

@SuppressWarnings("unused")
public class MazeAPI {
	private static Random random = new Random();
	private static Location locationOne, locationTwo;
	private static Map<Location, Block> maze = new HashMap<>(); // set of blocks already in the maze
	private static List<Block> frontier = new ArrayList<>(); // set of blocks adjacent to a block in the maze, but not yet in the maze itself
	private static Map<Location, Block> grid = new HashMap<>(); // set of blocks to be added to the maze
	private static World world;
	private static Map<Location, Block> toAdd = new HashMap<>();
	
	static long startTime; // For debugging
	
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
		
		Bukkit.getServer().broadcastMessage("Generating Maze....");
		
		world = locationOne.getWorld();
		
		startTime = System.currentTimeMillis();
		
		maze.clear();
		frontier.clear();
		grid.clear();
		toAdd.clear();
		
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
		
		// Add all blocks in the selected region to the grid
		for (int i = x1; i <= x2; i++) {
			for (int j = z1; j <= z2; j++) {
				Location location = new  Location(world, i, locationOne.getY(), j);
				Block b = world.getBlockAt(location);//world.getBlockAt(new Location(world, i, 100, j));
				if (i == x2 && j == centerZ) {
					b.setType(Material.AIR);
				} else if (i == x1 && j == centerZ) {
					b.setType(Material.AIR);
				} else {
					//b.setType(Material.DIAMOND_BLOCK);
					toAdd.putIfAbsent(location, b);
				}
				
				if (i % 2 != 0) continue;
				if (j % 2 != 0) continue;
				
				toAdd.putIfAbsent(location, b);
				grid.put(location, b);//.setType(Material.DIAMOND_BLOCK);
				//b.setType(Material.EMERALD_BLOCK);
				//b.setType(Material.DIAMOND_BLOCK);
			}
		}
		
		for (Location l : toAdd.keySet()) {
			if (!grid.containsKey(l)) {
				toAdd.get(l).setType(Material.DIAMOND_BLOCK);
				toAdd.put(l, grid.get(l));
			}
		}
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(MazeGen.getPlugin(), new Runnable() {
			@Override
			public void run() {
				//Block point = grid.get(random.nextInt(grid.size())); // Add the first point to the maze
				Location loc = grid.keySet().iterator().next();
				Block point = grid.get(loc);
				addNeighborsToFronteir(loc);
				maze.put(loc, point);
				grid.remove(loc);
				point.setType(Material.AIR);
				
				while (frontier.size() > 0) {
					point = frontier.get(random.nextInt(frontier.size()));
					addToMaze(point);
				}
				long endTime = System.currentTimeMillis();
				Bukkit.getServer().broadcastMessage("Maze Complete");
				Bukkit.getServer().broadcastMessage("Generation Time: " + (endTime - startTime) + "ms");
				Bukkit.getServer().broadcastMessage(grid.size() + "");
			}
		}, 1);
	}
	
	// Merge into the above function. Separated for testing
	public static void generateMaze(int pathWidth) throws NullPointerException {
		if (locationOne == null) {
			throw new NullPointerException("locationOne not set");
		} else if (locationTwo == null) {
			throw new NullPointerException("locationTwo not set");
		}
		
		Bukkit.getServer().broadcastMessage("Generating Maze....");
		
		world = locationOne.getWorld();
		
		startTime = System.currentTimeMillis();
		
		maze.clear();
		frontier.clear();
		grid.clear();
		toAdd.clear();
		
		int x1 = Math.min(locationOne.getBlockX(), locationTwo.getBlockX());
		int x2 = Math.max(locationOne.getBlockX(), locationTwo.getBlockX());
		
		int z1 = Math.min(locationOne.getBlockZ(), locationTwo.getBlockZ());
		int z2 = Math.max(locationOne.getBlockZ(), locationTwo.getBlockZ());;
		
		// Ensure there will always be a border
		
		while ((x1 + x2 + 1) % 3 != 0) {
			x2++;
		}
		while ((z1 + z2 + 1) % 3 != 0) {
			z2++;
		}
		
		/*while ((x1 + x2 - 1) % (3 * 2) != 0) {
			x2--;
		}
		while ((z1 + z2 - 1) % (3 * 2) != 0) {
			z2--;
		}*/
		/*if (x1 % 3 == 0) x1++;
		if (x2 % 3 == 0) x2++;
		if (z1 % 3 == 0) z1++;
		if (z2 % 3 == 0) z2++;*/
		
		int centerX = (x1 + x2) / 3;
		int centerZ = (z1 + z2) / 3;
		
		// Ensure the entrance and exits won't be blocked by walls
		if (centerX % 3 != 0) centerX++;
		if (centerZ % 3 != 0) centerZ++;
		
		boolean wall = false;
		boolean hardWall = false;
		int ctr = 0;
		int wallCtr = -1;
		int wallCtr2 = 0;
		
		// Add all blocks in the selected region to the grid
		for (int i = x1; i <= x2 - 3; i++) {
			ctr = 0;
			wall = false;
			
			if (wallCtr >= 3) {
				wall = true;
				wallCtr2++;
				hardWall = true;
				if (wallCtr2 > 3) {
					wallCtr = 0;
					wallCtr2 = 0;
					wall = false;
					hardWall = false;
				}
			}
			wallCtr++;
			
			/*if (wallCtr < 3) {
				wallCtr++;
				wall3 = false;
			} else {
				if (wallCtr2 < 3) {
					wallCtr2++;
				} else {
					wallCtr = 0;
					wallCtr2 = 0;
					wall3 = true;
				}
			}*/
			for (int j = z1; j <= z2 - 3; j++) {
				Location location = new  Location(world, i, locationOne.getY(), j);
				Block b = world.getBlockAt(location);//world.getBlockAt(new Location(world, i, 100, j));
				if (i == x2 && j == centerZ) {
					b.setType(Material.AIR);
				} else if (i == x1 && j == centerZ) {
					b.setType(Material.AIR);
				} else {
					//b.setType(Material.DIAMOND_BLOCK);
					toAdd.putIfAbsent(location, b);
				}
				
				if (i == x1) continue;
				if (i == x2) continue;
				if (j == z1) continue;
				if (j == z2) continue;
				
				if (ctr == 3) {
					ctr = 0;
					wall = !wall;
				}
				/*if (wall3 == true) {
					//grid.put(location, b);
					continue;
				}*/
				ctr++;
				/*if (wallCtr2 >= 3) {
					continue;
				}*/
				if (wall || hardWall) {
					//grid.put(location, b);
					continue;
				}
				
				//if (i % (pathWidth + 1) != 0) continue;
				//if (j % (pathWidth + 1) != 0) continue;
				
				toAdd.putIfAbsent(location, b);
				grid.put(location, b);//.setType(Material.DIAMOND_BLOCK);
				b.setType(Material.EMERALD_BLOCK);
				//b.setType(Material.DIAMOND_BLOCK);
			}
		}
		
		for (Location l : toAdd.keySet()) {
			if (!grid.containsKey(l)) {
				toAdd.get(l).setType(Material.DIAMOND_BLOCK);
				toAdd.put(l, grid.get(l));
			}
		}
		
		/*Bukkit.getScheduler().scheduleSyncDelayedTask(MazeGen.getPlugin(), new Runnable() {
			@Override
			public void run() {
				//Block point = grid.get(random.nextInt(grid.size())); // Add the first point to the maze
				Location loc = grid.keySet().iterator().next();
				Block point = grid.get(loc);
				addNeighborsToFronteir(loc);
				maze.put(loc, point);
				grid.remove(loc);
				point.setType(Material.AIR);
				
				while (frontier.size() > 0) {
					point = frontier.get(random.nextInt(frontier.size()));
					addToMaze(point);
				}
				long endTime = System.currentTimeMillis();
				Bukkit.getServer().broadcastMessage("Maze Complete");
				Bukkit.getServer().broadcastMessage("Generation Time: " + (endTime - startTime) + "ms");
				Bukkit.getServer().broadcastMessage(grid.size() + "");
			}
		}, 1);*/
	}
	
	private static void addToMaze(Block point) {
		addNeighborsToFronteir(point.getLocation());
		
		List<Block> neighborsInMaze = getInMazeNeighbors(point);
		Collections.shuffle(neighborsInMaze);
		Block inMaze = neighborsInMaze.get(0);
		
		int newX = (inMaze.getX() + point.getX()) / 2;
		int newZ = (inMaze.getZ() + point.getZ()) / 2;
		
		world.getBlockAt(new Location(point.getWorld(), newX, point.getY(), newZ)).setType(Material.AIR);
		point.setType(Material.AIR);
		
		Location loc = point.getLocation();
		
		grid.remove(loc);
		frontier.remove(point);
		maze.put(loc, point);
	}
	
	/*private static void connectToMaze(Block point) {
		List<Block> neighbors = getInMazeNeighbors(point);
	}*/
	
	private static List<Block> getInMazeNeighbors(Block point) {
		List<Block> inMaze = new ArrayList<>();
		Location location = new Location(point.getWorld(), point.getX(), point.getY(), point.getZ());
		Block block = world.getBlockAt(location.add(2, 0, 0));
		if (maze.containsKey(location)) {
			inMaze.add(block);
		}
		block = world.getBlockAt(location.add(-4, 0, 0));
		if (maze.containsKey(location)) {
			inMaze.add(block);
		}
		block = world.getBlockAt(location.add(2, 0, 2));
		if (maze.containsKey(location)) {
			inMaze.add(block);
		}
		block = world.getBlockAt(location.add(0, 0, -4));
		if (maze.containsKey(location)) {
			inMaze.add(block);
		}
		return inMaze;
	}
	
	private static void addNeighborsToFronteir(Location loc) {
		Location location = new Location(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ());
		//Block block = grid.get(location);
		Block block = world.getBlockAt(location.add(2, 0, 0));
		if (grid.containsKey(location) && !maze.containsKey(location) && !frontier.contains(block)) {
			frontier.add(block);
			grid.remove(location);
		}
		
		block = world.getBlockAt(location.add(-4, 0, 0));
		if (grid.containsKey(location) && !maze.containsKey(location) && !frontier.contains(block)) {
			frontier.add(block);
			grid.remove(location);
		}
		
		block = world.getBlockAt(location.add(2, 0, 2));
		if (grid.containsKey(location) && !maze.containsKey(location) && !frontier.contains(block)) {
			frontier.add(block);
			grid.remove(location);
		}
		
		block = world.getBlockAt(location.add(0, 0, -4));
		if (grid.containsKey(location) && !maze.containsKey(location) && !frontier.contains(block)) {
			frontier.add(block);
			grid.remove(location);
		}
	}
	
	/*private Location newLocation(Location location, int x, int y, int z) {
		Location newLoc = new Location(location.getWorld(), location.getX(), location.getY(), location.getZ());
	}*/
	
	
}
