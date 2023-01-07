package ca.uwindsor.css.ctf.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import ca.uwindsor.css.ctf.Main;
import ca.uwindsor.css.ctf.Team;
import ca.uwindsor.css.ctf.TeamManager;
import ca.uwindsor.css.ctf.commands.CommandHome;
import net.md_5.bungee.api.ChatColor;

public class PlayerDamageListener implements Listener {

	@EventHandler
	public void onDamage(EntityDamageByEntityEvent event) {

		// Check for team-hitting
		if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) { // If the attacker and victim
																							// are players

			Player attacker = (Player) event.getDamager();
			Player victim = (Player) event.getEntity();

			Team attackerTeam = TeamManager.getPlayerTeam(attacker);
			Team victimTeam = TeamManager.getPlayerTeam(victim);

			// If neither player is on a team, it will return null, which isn't
			// the best for a clean console output
			if (attackerTeam == null || victimTeam == null) {
				return;
			}

			if (attackerTeam.equals(victimTeam)) {
				attacker.sendMessage(ChatColor.RED + "You cannot attack your own team members!");
				event.setCancelled(true);
				return;
			}

			// Check if pvp is enabled
			if (Main.pvpEnabled == false) {
				event.setCancelled(true);
				event.getDamager().sendMessage(ChatColor.RED + "PvP is currently disabled!");
				return;
			}

		}
	}

	@EventHandler
	public void onDeath(PlayerDeathEvent event) {
		Player player = event.getEntity();
		CommandHome.cooldown.add(player);
		BukkitScheduler scheduler = Bukkit.getServer().getScheduler();

		scheduler.scheduleSyncDelayedTask(JavaPlugin.getPlugin(Main.class), new Runnable() {
			@Override
			public void run() {
				CommandHome.cooldown.remove(player);
			}
		}, 2400);
	}

	@EventHandler
	public void onRespawn(PlayerRespawnEvent event) {
		Player player = event.getPlayer();
		
		// Respawn the player at the banners spawnpoint
		if (TeamManager.playerHasTeam(player)) {
			event.setRespawnLocation(TeamManager.getPlayerTeam(player).getBannerSpawn());
		}
	}
}
