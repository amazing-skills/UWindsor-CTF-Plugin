package ca.uwindsor.css.ctf;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;

import net.md_5.bungee.api.ChatColor;

public class CommandStart {
	public static boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		try {
			int timeInSeconds = Integer.parseInt(args[1]);
			addTimer(timeInSeconds);
			sender.sendMessage(ChatColor.GREEN + "Countdown started for " + ChatColor.AQUA + timeInSeconds
					+ ChatColor.GREEN + " seconds from now. PvP will be automatically enabled.");
		} catch (IndexOutOfBoundsException e) {
			sender.sendMessage(ChatColor.RED + "Invalid use of command. /teams start <countdownTime>");
		}
		return true;
	}

	public static void addTimer(int timeInSeconds) {
		Scoreboard board = Main.board;
		board.getObjective("Scoreboard").getScore(" ").setScore(1);
		new BukkitRunnable() {
			int count = timeInSeconds;

			public void run() {
				count--;
				if (count > 60) {
					board.getObjective("Scoreboard").getScore(ChatColor.BOLD + "Timer (minutes)")
							.setScore((int) Math.ceil(count / 60f));
				} else if (count == 60) {
					board.resetScores(ChatColor.BOLD + "Timer (minutes)");
				} else if (count <= 10) {
					for (Player player : Bukkit.getServer().getOnlinePlayers()) {
						player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 3, 3);
					}
					board.getObjective("Scoreboard").getScore(ChatColor.BOLD + "Timer (seconds)").setScore(count);
				} else {
					board.getObjective("Scoreboard").getScore(ChatColor.BOLD + "Timer (seconds)").setScore(count);
				}
				// Countdown ends
				if (count == 0) {
					board.resetScores(ChatColor.BOLD + "Timer (seconds)");
					board.resetScores(" ");

					// Enforce rules and set players into survival mode

					for (Team team : TeamManager.getTeams()) {
						for (Player player : team.getPlayers()) {
							player.setGameMode(GameMode.SURVIVAL);
						}
					}

					// Note that we want to enforce rules **after** all players
					// are in survival, because creative can cause some issues.
					Rules rules = new Rules();
					rules.enforceRules();

					for (Player player : Bukkit.getServer().getOnlinePlayers()) {
						player.playSound(player.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 10f, 0.5f);
					}
					Bukkit.broadcastMessage(
							ChatColor.DARK_RED + "=====================================================");
					Bukkit.broadcastMessage(ChatColor.GRAY + "LET THE GAMES BEGIN! GOOD LUCK!");
					Bukkit.broadcastMessage(
							ChatColor.DARK_RED + "=====================================================");
					this.cancel();
				}
			}
		}.runTaskTimer(Main.getPlugin(Main.class), 0L, 20L);
	}
}
