package me.caske33.hardcore;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

public class HardcoreListener implements Listener {

	private Plugin p;
	private Hardcore hc;
	private Player lpd; // last player damage report

	public HardcoreListener(Plugin plugin, Hardcore hc) {
		this.p = plugin;
		this.hc = hc;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public boolean onMove(PlayerMoveEvent e) {
		Player player = e.getPlayer();
		if (hc.regPlayers.contains(player) && hc.options.circleWorld && hc.playing) {
			Location loc = e.getPlayer().getLocation();
			int x = (int) Math.round(loc.getX());
			int z = (int) Math.round(loc.getZ());
			boolean xsmall, xlarge, zsmall, zlarge;
			xsmall = x <= hc.xrstart;
			xlarge = x > hc.xrstop + 1;
			zsmall = z <= hc.zrstart;
			zlarge = z > hc.zrstop + 1;

			if (xsmall || xlarge || zsmall || zlarge) {
				if (xsmall)
					loc.setX(hc.xrstop);
				if (xlarge)
					loc.setX(hc.xrstart + 1);
				if (zsmall)
					loc.setZ(hc.zrstop);
				if (zlarge)
					loc.setZ(hc.zrstart + 1);

				int y = hc.world.getHighestBlockYAt(loc);
				loc.setY(y);
				player.teleport(loc, TeleportCause.PLUGIN);
				return false;
			}
		}
		return true;
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public boolean onDead(PlayerDeathEvent e) {

		Player player = e.getEntity();
		if (hc.regPlayers.contains(player)) {
			World world = hc.world;
			Location locd = player.getLocation(); // Location of his death
			locd.setY(world.getHighestBlockYAt(locd));

			// GRAVE enabled?
			if (hc.options.grave && hc.playing) {
				HardcoreConstruction.constructGrave(locd, e.getDrops(), player);
				e.getDrops().clear();

				// Message all the other players where the grave is
				p.getServer().broadcastMessage("You can visit " + player.getName() + "s grave at " + locd.getBlockX() + ", " + locd.getBlockZ());
			}

			// remove player from registered players and add him to the died player list.
			hc.regPlayers.remove(player);
			hc.deadPlayers.add(player);
			player.setDisplayName("Zombie - " + player.getName());

			if (hc.regPlayers.size() == 1)
				hc.end();
		}
		return true;
	}

	@EventHandler
	public boolean onDamage(EntityDamageEvent e) {
		if (e.getEntity() instanceof Player) {
			Player player = (Player) e.getEntity();
			if (player != lpd && player.getHealth() <= 15 && (hc.random.nextInt(3) != 0 || player.getHealth() <= 5)) {
				String[] s = { " is bleeding. Go kill him!", " needs some first aid.", " is vulnerable to pvp attack.",
						" should have been more carefully." };
				p.getServer().broadcastMessage(player.getName() + s[hc.random.nextInt(s.length)]);
				lpd = player;
			}
		}
		return true;
	}
}
