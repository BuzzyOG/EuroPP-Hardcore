package me.caske33.hardcore;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class HardcoreConstruction {

	// TODO add a portal for in the walls

	public static void constructWall(World world, int xfrom, int xto, int zfrom, int zto, Material m1, Material m2) {
		int x, y, z;
		Material m;
		for (y = 0; y < 255; y++) {
			if (y <= 64) {
				m = m1;
			} else {
				m = m2;
			}
			for (x = xfrom; x <= xto + 1; x++) {
				z = zfrom;
				world.getBlockAt(x, y, z).setType(m);
				z = zto + 1;
				world.getBlockAt(x, y, z).setType(m);
			}
			for (z = zfrom; z <= zto + 1; z++) {
				x = xfrom;
				world.getBlockAt(x, y, z).setType(m);
				x = xto + 1;
				world.getBlockAt(x, y, z).setType(m);
			}
		}
	}

	public static void constructUpperWall(World world, int xfrom, int xto, int zfrom, int zto, Material m) {
		int x, y, z;
		for (y = 80; y < 255; y++) {
			for (x = xfrom; x <= xto + 1; x++) {
				z = zfrom;
				world.getBlockAt(x, y, z).setType(m);
				z = zto + 1;
				world.getBlockAt(x, y, z).setType(m);
			}
			for (z = zfrom; z <= zto + 1; z++) {
				x = xfrom;
				world.getBlockAt(x, y, z).setType(m);
				x = xto + 1;
				world.getBlockAt(x, y, z).setType(m);
			}
		}
	}

	public static void constructPodium(Location loc) {
		constructPodium(loc.getWorld(), (int) loc.getX(), (int) loc.getY(), (int) loc.getZ());
	}

	public static void constructPodium(World world, int x, int y, int z) {
		Material m = Material.SPONGE;
		Material m2 = Material.WATER;
		world.getBlockAt(x + 1, y, z).setType(Material.IRON_BLOCK);
		world.getBlockAt(x - 1, y, z).setType(Material.GOLD_BLOCK);
		world.getBlockAt(x, y, z).setType(Material.DIAMOND_BLOCK);
		world.getBlockAt(x, y + 1, z).setType(Material.DIAMOND_BLOCK);

		// left wall
		world.getBlockAt(x + 3, y, z - 2).setType(m);
		world.getBlockAt(x + 3, y, z - 1).setType(m);
		world.getBlockAt(x + 3, y, z).setType(m);
		world.getBlockAt(x + 3, y, z + 1).setType(m);
		world.getBlockAt(x + 3, y, z + 2).setType(m);

		// right wall
		world.getBlockAt(x - 3, y, z - 2).setType(m);
		world.getBlockAt(x - 3, y, z - 1).setType(m);
		world.getBlockAt(x - 3, y, z).setType(m);
		world.getBlockAt(x - 3, y, z + 1).setType(m);
		world.getBlockAt(x - 3, y, z + 2).setType(m);

		// front wall
		world.getBlockAt(x - 2, y, z - 2).setType(m);
		world.getBlockAt(x - 1, y, z - 2).setType(m);
		world.getBlockAt(x, y, z - 2).setType(m);
		world.getBlockAt(x + 1, y, z - 2).setType(m);
		world.getBlockAt(x + 2, y, z - 2).setType(m);

		// back wall
		world.getBlockAt(x - 2, y, z + 2).setType(m);
		world.getBlockAt(x - 1, y, z + 2).setType(m);
		world.getBlockAt(x, y, z + 2).setType(m);
		world.getBlockAt(x + 1, y, z + 2).setType(m);
		world.getBlockAt(x + 2, y, z + 2).setType(m);

		// The lower level
		world.getBlockAt(x - 2, y - 1, z - 1).setType(m);
		world.getBlockAt(x - 1, y - 1, z - 1).setType(m);
		world.getBlockAt(x, y - 1, z - 1).setType(m);
		world.getBlockAt(x + 1, y - 1, z - 1).setType(m);
		world.getBlockAt(x + 2, y - 1, z - 1).setType(m);

		world.getBlockAt(x - 2, y - 1, z).setType(m);
		world.getBlockAt(x - 1, y - 1, z).setType(m);
		world.getBlockAt(x, y - 1, z).setType(m);
		world.getBlockAt(x + 1, y - 1, z).setType(m);
		world.getBlockAt(x + 2, y - 1, z).setType(m);

		world.getBlockAt(x - 2, y - 1, z + 1).setType(m);
		world.getBlockAt(x - 1, y - 1, z + 1).setType(m);
		world.getBlockAt(x, y - 1, z + 1).setType(m);
		world.getBlockAt(x + 1, y - 1, z + 1).setType(m);
		world.getBlockAt(x + 2, y - 1, z + 1).setType(m);

		// The water
		world.getBlockAt(x - 2, y, z - 1).setType(m2);
		world.getBlockAt(x - 1, y, z - 1).setType(m2);
		world.getBlockAt(x, y, z - 1).setType(m2);
		world.getBlockAt(x + 1, y, z - 1).setType(m2);
		world.getBlockAt(x + 2, y, z - 1).setType(m2);

		world.getBlockAt(x - 2, y, z).setType(m2);
		world.getBlockAt(x + 2, y, z).setType(m2);

		world.getBlockAt(x - 2, y, z + 1).setType(m2);
		world.getBlockAt(x - 1, y, z + 1).setType(m2);
		world.getBlockAt(x, y, z + 1).setType(m2);
		world.getBlockAt(x + 1, y, z + 1).setType(m2);
		world.getBlockAt(x + 2, y, z + 1).setType(m2);
	}

	public static void constructGrave(World world, int x, int y, int z, List<ItemStack> drops, Player player) {
		// The chests
		world.getBlockAt(x, y - 2, z).setType(Material.CHEST);
		Chest c = (Chest) world.getBlockAt(x, y - 2, z).getState();
		Inventory ci1 = c.getBlockInventory();
		world.getBlockAt(x - 1, y - 2, z).setType(Material.CHEST);
		c = (Chest) world.getBlockAt(x - 1, y - 2, z).getState();
		Inventory ci2 = c.getBlockInventory();
		int j = 0;
		for (ItemStack i : drops) {
			if (j < 27)
				ci2.addItem(i);
			else
				ci1.addItem(i);
			j++;
		}
		c.update();

		// the smoothstone
		world.getBlockAt(x - 1, y - 1, z).setType(Material.SMOOTH_BRICK);
		world.getBlockAt(x, y - 1, z).setType(Material.SMOOTH_BRICK);

		// the top layer: stair and slab
		world.getBlockAt(x, y, z).setType(Material.SMOOTH_STAIRS);
		world.getBlockAt(x - 1, y, z).setType(Material.STEP);
		world.getBlockAt(x - 1, y, z).setData((byte) 0x5);

		// the RIP sign
		world.getBlockAt(x, y + 1, z).setType(Material.SIGN_POST);
		world.getBlockAt(x, y + 1, z).setData((byte) 0x4);
		Sign s = (Sign) world.getBlockAt(x, y + 1, z).getState();
		s.setLine(0, player.getName());
		s.setLine(1, "R.I.P.");
		s.update();
	}

	public static void constructGrave(Location loc, List<ItemStack> drops, Player player) {
		constructGrave(loc.getWorld(), (int) loc.getX(), (int) loc.getY(), (int) loc.getZ(), drops, player);
	}
}
