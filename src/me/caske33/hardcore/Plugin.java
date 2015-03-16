package me.caske33.hardcore;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.plugin.java.JavaPlugin;

public class Plugin extends JavaPlugin {

	public Hardcore hc;

	public int countdownover = 0;

	public void onEnable() {
		hc = new Hardcore(this);
	}

	public void onDisable() {
	}

	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
		if (command.getName().equalsIgnoreCase("hcin") && sender instanceof Player) {
			Player player = (Player) sender;
			hc.world = player.getWorld();
			hc.initiate();
			return true;
		} else if (command.getName().equalsIgnoreCase("hcwg") && sender instanceof Player) {
			Player player = (Player) sender;
			hc.world = player.getWorld();
			hc.worldGenerate();
			return true;
		} else if (command.getName().equalsIgnoreCase("hccd")) {
			hc.startCountDown();
			return true;
		} else if (command.getName().equalsIgnoreCase("hcreg") && sender instanceof Player) {
			Player player = (Player) sender;
			if (!hc.regPlayers.contains(player)) {
				if (!hc.deadPlayers.contains(player)) {
					if (!hc.playing) {
						hc.regPlayers.add(player);
						player.setDisplayName("Registered - " + player.getName());
						sender.sendMessage("You have been registered.");
					} else {
						player.setGameMode(GameMode.SURVIVAL);
						player.setHealth(20);
						player.setFoodLevel(20);
						player.setTotalExperience(0);
						player.getInventory().clear();
						player.setDisplayName("Playing - " + player.getName());

						int x = hc.xrstart + hc.random.nextInt(hc.width - 2) + 1;
						int z = hc.zrstart + hc.random.nextInt(hc.length - 2) + 1;
						Location loc = new Location(hc.world, x, 0, z);
						loc.setY(hc.world.getHighestBlockYAt(x, z));
						player.teleport(loc, TeleportCause.PLUGIN);
					}
				} else {
					sender.sendMessage("You died already");
				}
			} else {
				sender.sendMessage("You're already registered.");
			}
			return true;
		} else if (command.getName().equalsIgnoreCase("hcunreg") && sender instanceof Player) {
			Player player = (Player) sender;
			if (hc.regPlayers.contains(player)) {
				hc.regPlayers.remove(player);
				player.sendMessage("You have been unregistered.");
				player.setDisplayName(player.getName());
			} else {
				sender.sendMessage("You weren't registered yet");
			}
			return true;
		} else if (command.getName().equalsIgnoreCase("hco")) {
			hc.options.command(sender, args);
			return true;
		} else if (command.getName().equalsIgnoreCase("hcend")) {
			hc.endTime();
			return true;
		}

		return false;
	}

	public void countdown() {
		if (countdownover > 0) {
			getServer().broadcastMessage("The game starts in " + countdownover--);
			getServer().getScheduler().scheduleAsyncDelayedTask(this, new Runnable() {
				public void run() {
					countdown();
				}
			}, 20L);
		} else {
			getServer().broadcastMessage("GO!");
		}

	}
}
