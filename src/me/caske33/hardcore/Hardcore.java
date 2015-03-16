package me.caske33.hardcore;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.block.Chest;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class Hardcore {

	public Plugin p; // the plugin
	public World world;
	public int countdown;
	public boolean playing = false;
	public Random random = new Random();
	public int nIron, nDiamond, nGold, nCoal;
	public Location lastChest;

	public HardcoreOption options = new HardcoreOption();
	public ArrayList<Player> regPlayers = new ArrayList<Player>(); // registered players
	public ArrayList<Player> deadPlayers = new ArrayList<Player>(); // players who died
	public ArrayList<Location> spawnLocations = new ArrayList<Location>(); // possible spawn locations

	public int width, length; // world width and height
	public int xstart, xstop, zstart, zstop; // gebied te kopiëren
	public int xrstart, xrstop, zrstart, zrstop; // gebied waar hardcore doorgaat

	public int xrlstart, xrlstop, zrlstart, zrlstop; // kleiner gebied
	public int loweringScheduler;

	private static final Material WALL_MATERIAL_UP = Material.GLASS;
	private static final Material WALL_MATERIAL_DOWN = Material.BEDROCK;

	public Player winner;

	public Hardcore(Plugin plugin) {
		this.p = plugin;
	}

	public void constructTerrain2() {

		xstart = world.getSpawnLocation().getBlockX();
		xstop = xstart + width;
		zstart = world.getSpawnLocation().getBlockZ();
		zstop = zstart + length;

		xrstart = xstart;
		xrstop = xstop;
		zrstart = zstart;
		zrstop = zstop;

		int x, y, z;

		if (options.pumpkinConvert || options.worldinformation || options.chestWorld) {
			for (y = 0; y < 255; y++) {
				for (x = xstart; x <= xstop; x++) {
					for (z = zstart; z <= zstop; z++) {
						if (options.pumpkinConvert && world.getBlockAt(x, y, z).getType() == Material.PUMPKIN)
							world.getBlockAt(x, y, z).setType(Material.GOLD_BLOCK);

						if (options.worldinformation) {
							// Count the ores
							switch (world.getBlockAt(x, y, z).getType()) {
							case IRON_ORE:
								nIron++;
								break;
							case GOLD_ORE:
								nGold++;
								break;
							case COAL_ORE:
								nCoal++;
								break;
							case DIAMOND_ORE:
								nDiamond++;
								break;
							}
						}

						if (options.chestWorld && y == 120 && (x % 2 == 0 ^ z % 2 == 0)) {
							world.getBlockAt(x, y, z).setType(Material.CHEST);
							Chest c = (Chest) world.getBlockAt(x, y, z).getState();
							Inventory ci = c.getBlockInventory();

							for (int j = 0; j < options.chestLootItems.length; j++) {
								for (int i = 0; i < random.nextInt(options.chestLootItems[j]); i++) {
									ItemStack is = new ItemStack(options.chestLoot[j][random.nextInt(options.chestLoot[j].length)]);
									is.setAmount(random.nextInt(options.chestLootMaxAmount[j]) + 1);
									ci.setItem(random.nextInt(ci.getSize()), is);
								}
							}
							c.update();
						}

						// FOR MORE OPTIONS DON'T FORGET TO CHANGE IF STATEMENT ABOVE!!!
					}
				}
			}
		}

		if (options.waterborder) {
			Material m;
			for (int a = 0; a < options.waterborderwidth; a++) {
				for (int b = 0; b < options.waterborderlength; b++) {
					for (int c = 0; c < 128; c++) {
						if (c <= 64)
							m = Material.WATER;
						else
							m = Material.AIR;

						world.getBlockAt(xstart + a - width, c, zstart + b).setType(m);
						world.getBlockAt(xstart + a + width, c, zstart + b).setType(m);
						world.getBlockAt(xstart + a, c, zstart + b - length).setType(m);
						world.getBlockAt(xstart + a, c, zstart + b + length).setType(m);

						world.getBlockAt(xstart + a - width, c, zrstart + b - length).setType(m);
						world.getBlockAt(xstart + a + width, c, zrstart + b - length).setType(m);
						world.getBlockAt(xstart + a - width, c, zrstart + b + length).setType(m);
						world.getBlockAt(xstart + a + width, c, zrstart + b + length).setType(m);
					}
				}
			}
		}

	}

	public void initiate() {
		p.getServer().broadcastMessage("The game is beeing initiated");
		worldGenerate();
		startCountDown();
	}

	public void worldGenerate() {
		new HardcoreListener(p, this);
		p.getServer().broadcastMessage("The world is beeing generated.");
		width = options.width;
		length = options.length;

		constructTerrain2();
		HardcoreConstruction.constructWall(world, xrstart, xrstop, zrstart, zrstop, WALL_MATERIAL_DOWN, WALL_MATERIAL_UP);

		p.getServer().broadcastMessage("The world has been generated.");
	}

	public void startCountDown() {
		if (regPlayers.size() != 0) {
			world.setPVP(true);
			world.setSpawnFlags(true, true);
			for (Entity entity : world.getEntities()) {
				// remove mobs
				if (entity instanceof LivingEntity && !(entity instanceof Player))
					entity.remove();
				// remove drops
				if (entity instanceof Item)
					entity.remove();
			}

			for (Player player : regPlayers) {
				player.setGameMode(GameMode.SURVIVAL);
				player.setHealth(20);
				player.setFoodLevel(20);
				player.setTotalExperience(0);
				player.getInventory().clear();
				player.setDisplayName("Playing - " + player.getName());
			}

			double rnp = Math.sqrt(regPlayers.size());

			double rr = Math.sqrt(width / length); // root of the ratio
			int nx = (int) Math.ceil(rr * rnp); // number of locations in width
			int nz = (int) Math.ceil(rnp / rr); // number of locations in length
			int wss = width / nx; // width of one 'spawn square'
			int lss = length / nz; // length of one 'spawn square'

			for (int x = 0; x < nx; x++) {
				for (int z = 0; z < nz; z++) {
					Location loc = new Location(world, 0, 0, 0);
					int lx = xrstart;
					int lz = zrstart;

					if (options.randomSpawn) {
						lx += random.nextInt(wss / 2);
						lz += random.nextInt(lss / 2);
					} else {
						lx += wss / 2;
						lz += lss / 2;
					}

					lx += x * wss;
					lz += z * lss;

					loc.setX(lx);
					loc.setZ(lz);

					loc.setY(world.getHighestBlockYAt(loc));
					spawnLocations.add(loc);
				}
			}
			// first countdown and then start teleporting the players in.
			countdown = options.countdown;
			p.getServer().getScheduler().scheduleAsyncDelayedTask(p, new Runnable() {
				public void run() {
					countdown();
				}
			}, 20L); // scheduler delayed 1 sec.
		} else {
			p.getServer().broadcastMessage("There are no players registrated!");
		}
	}

	public void countdown() {
		if (countdown >= 1) {
			p.getServer().getScheduler().scheduleAsyncDelayedTask(p, new Runnable() {
				public void run() {
					countdown();
				}
			}, 20L); // scheduler delayed 1 sec.
			p.getServer().broadcastMessage("The game starts in " + countdown--);
		} else {
			world.setFullTime(0);
			playing = true;
			for (Player player : regPlayers) {
				player.setGameMode(GameMode.SURVIVAL);
				player.setHealth(20);
				player.setFoodLevel(20);
				player.setTotalExperience(0);
				player.getInventory().clear();
				int n = random.nextInt(spawnLocations.size());
				Location loc = spawnLocations.get(n);
				spawnLocations.remove(n);
				player.teleport(loc, TeleportCause.PLUGIN);
			}
			if (options.worldinformation)
				worldinformation();
			if (options.lowerWorldSizeMinutes != 0) {
				loweringScheduler = p.getServer().getScheduler().scheduleAsyncRepeatingTask(p, new Runnable() {
					public void run() {
						lowerWorldSizeStart();
					}
				}, 20L * 60 * options.lowerWorldSizeMinutes, 20L * 60 * options.lowerWorldSizeMinutes);
			}
			if (options.chestPlacingMinutes != 0) {
				loweringScheduler = p.getServer().getScheduler().scheduleAsyncRepeatingTask(p, new Runnable() {
					public void run() {
						if (random.nextInt(options.chestPlacingLuck) == 0)
							placeChest();
						else
							p.getServer().broadcastMessage("Bad luck prevented a chest from placing.");
					}
				}, 20L * 60 * options.chestPlacingMinutes, 20L * 60 * options.chestPlacingMinutes);
			}
			if (options.duration != 0) {
				p.getServer().getScheduler().scheduleAsyncDelayedTask(p, new Runnable() {
					public void run() {
						endTime();
					}
				}, 20L * 60 * options.duration);
				if (options.duration > options.durationWarning) {
					p.getServer().getScheduler().scheduleAsyncDelayedTask(p, new Runnable() {
						public void run() {
							p.getServer().broadcastMessage(
									"Time's almost gone. You guys have " + options.durationWarning
											+ " minutes left before the end battle. Prepare yourself!");
						}
					}, 20L * 60 * (options.duration - options.durationWarning));
				}
			}
		}

	}

	public void worldinformation() {
		Server s = p.getServer();
		s.broadcastMessage("This world holds " + nCoal + " coal ore, " + nIron + " iron ore, " + nGold + " gold ore and " + nDiamond
				+ " diamond ore.");
		s.broadcastMessage("This world is " + width + " blocks by " + length + " blocks");
		s.broadcastMessage("Enjoy your game, good luck and have fun!");
	}

	public void lowerWorldSizeStart() {
		Server s = p.getServer();

		// Calculate new world size
		xrlstart = xrstart;
		xrlstop = xrstop;
		zrlstart = zrstart;
		zrlstop = zrstop;

		if (random.nextInt(2) == 0)
			xrlstart += options.lowerWorldSizeX;
		else
			xrlstop -= options.lowerWorldSizeX;
		if (random.nextInt(2) == 0)
			zrlstart += options.lowerWorldSizeZ;
		else
			zrlstop -= options.lowerWorldSizeZ;

		HardcoreConstruction.constructUpperWall(world, xrlstart, xrlstop, zrlstart, zrlstop, WALL_MATERIAL_UP);
		s.broadcastMessage("The world size will be lowered in 5 minutes!");
		p.getServer().broadcastMessage("The new world borders will be x=" + xrlstart + "; x=" + xrlstop + "; z=" + zrlstart + " and z=" + zrlstop);

		// set the delay for a reminder
		s.getScheduler().scheduleAsyncDelayedTask(p, new Runnable() {
			public void run() {
				p.getServer().broadcastMessage("The world size will be lowered in 1 minute!");
				p.getServer().broadcastMessage(
						"The new world borders will be x=" + xrlstart + "; x=" + xrlstop + "; z=" + zrlstart + " and z=" + zrlstop);

			}
		}, 20L * 60 * 4); // scheduler delayed 20 ticks/sec * 60 sec * 1 minutes.

		// set the delay for the actual lowering
		s.getScheduler().scheduleAsyncDelayedTask(p, new Runnable() {
			public void run() {
				lowerWorldSizeEnd();
			}
		}, 20L * 60 * 5); // scheduler delayed 20 ticks/sec * 60 sec * 5 minutes.
	}

	public void lowerWorldSizeEnd() {
		p.getServer().broadcastMessage("The world size has been lowered!");
		p.getServer().broadcastMessage("The new world borders are x=" + xrlstart + "; x=" + xrlstop + "; z=" + zrlstart + " and z=" + zrlstop);
		xrstart = xrlstart;
		xrstop = xrlstop;
		zrstart = zrlstart;
		zrstop = zrlstop;

		width = xrstop - xrstart;
		length = zrstop - zrstart;

		HardcoreConstruction.constructWall(world, xrstart, xrstop, zrstart, zrstop, WALL_MATERIAL_DOWN, WALL_MATERIAL_UP);
	}

	public void placeChest() {
		int x = xrstart + random.nextInt(width - 2) + 1;
		int z = zrstart + random.nextInt(length - 2) + 1;
		int y = world.getHighestBlockYAt(x, z);

		world.getBlockAt(x, y, z).setType(Material.CHEST);
		Chest c = (Chest) world.getBlockAt(x, y, z).getState();
		Inventory ci = c.getBlockInventory();

		for (int j = 0; j < options.chestLootItems.length; j++) {
			for (int i = 0; i < random.nextInt(options.chestLootItems[j]); i++) {
				ItemStack is = new ItemStack(options.chestLoot[j][random.nextInt(options.chestLoot[j].length)]);
				is.setAmount(random.nextInt(options.chestLootMaxAmount[j]) + 1);
				ci.setItem(random.nextInt(ci.getSize()), is);
			}
		}
		c.update();
		p.getServer().broadcastMessage("A chest has been placed");
		lastChest = new Location(world, x, y, z);
		// reveal chest over 5 minutes.
		p.getServer().getScheduler().scheduleAsyncDelayedTask(p, new Runnable() {
			public void run() {
				int x = (int) (p.hc.lastChest.getX() + p.hc.random.nextInt(10) - p.hc.random.nextInt(10));
				int z = (int) (p.hc.lastChest.getZ() + p.hc.random.nextInt(10) - p.hc.random.nextInt(10));
				p.getServer().broadcastMessage("The chest place 5 minutes ago around x=" + x + " and z=" + z);
			}
		}, 20L * 60 * 5);
	}

	public void endTime() {
		// TODO add end battle (after time's up)
		p.getServer().broadcastMessage("Time's up! A Battlefield is beeing constructed!");
		p.getServer().getScheduler().scheduleAsyncDelayedTask(p, new Runnable() {
			public void run() {
				int x = random.nextInt(width - 2) + xrstart + 1;
				int z = random.nextInt(length - 2) + zrstart + 1;
				int y = world.getHighestBlockYAt(x, z);

				Location loc = new Location(world, x, y, z);
				Location loc2;
				Location loc3 = loc;
				loc3.setX(loc.getX() + 7);
				loc3.setZ(loc.getZ() + 7);
				loc3.setY(world.getHighestBlockYAt(loc3));

				for (Player player : world.getPlayers()) {
					if (!regPlayers.contains(player)) {
						player.teleport(loc3);
					} else {
						loc2 = loc;
						loc2.setX(loc.getX() + random.nextInt(15));
						loc2.setZ(loc.getZ() + random.nextInt(15));
						loc2.setY(world.getHighestBlockYAt(loc2));
						player.teleport(loc2);
					}
				}

				p.getServer().broadcastMessage("All remaining players are teleported within a area of fifteen by fifteen blocks.");
				p.getServer().broadcastMessage("The center of the battlefield is " + (int) loc3.getX() + ", " + (int) loc3.getZ());

				p.countdownover = 15;
				p.countdown();
			}
		}, 20L * 5);
	}

	public void end() {
		p.getServer().getScheduler().cancelTask(loweringScheduler);

		winner = regPlayers.get(0);
		p.getServer().broadcastMessage("Congratulations, " + winner.getName());

		Location loc = winner.getLocation();
		int x = (int) loc.getX();
		int y = (int) loc.getY();
		int z = (int) loc.getZ();
		loc.setY(world.getHighestBlockYAt(loc) + 2);

		// constructing the podium
		HardcoreConstruction.constructPodium(world, x, y, z);

		winner.teleport(loc, TeleportCause.PLUGIN); // put him on the podium
		winner.setDisplayName("Winner - " + winner.getName());

		// Place the rest of the players in front of the podium
		for (Player player : deadPlayers) {
			player.teleport(new Location(world, x, world.getHighestBlockYAt(x, z - 5), z - 5), TeleportCause.PLUGIN);
		}
		// put the second and third player on their place
		if (deadPlayers.size() >= 1)
			deadPlayers.get(deadPlayers.size() - 1).teleport(new Location(world, x + 1, y + 1, z), TeleportCause.PLUGIN);
		if (deadPlayers.size() >= 2)
			deadPlayers.get(deadPlayers.size() - 2).teleport(new Location(world, x - 1, y + 1, z), TeleportCause.PLUGIN);

		ItemStack reward = new ItemStack(Material.DIAMOND, 1);
		// Special rewards
		if (winner.getName().equalsIgnoreCase("Tuskinton"))
			reward = new ItemStack(Material.SPONGE, 1);
		else if (winner.getName().equalsIgnoreCase("Gggorm"))
			reward = new ItemStack(Material.ARROW, 1);
		else if (winner.getName().equalsIgnoreCase("lasere123456"))
			reward = new ItemStack(Material.TNT, 1);
		else if (winner.getName().equalsIgnoreCase("Breindahl"))
			reward = new ItemStack(Material.ENDER_PEARL, 1);
		else if (winner.getName().equalsIgnoreCase("martinbrook"))
			reward = new ItemStack(Material.ENDER_PEARL, 1);
		else if (winner.getName().equalsIgnoreCase("elementalguy2"))
			reward = new ItemStack(Material.STRING, 1);
		else if (winner.getName().equalsIgnoreCase("caske33"))
			reward = new ItemStack(Material.SNOW_BALL, 1);

		loc.setY(loc.getY() + 33);
		for (int i = 0; i < 213; i++) {
			loc.setY(loc.getY() + 1);
			world.dropItemNaturally(loc, reward);
		}

		playing = false;
		regPlayers.removeAll(regPlayers);
		deadPlayers.removeAll(deadPlayers);
	}
}
