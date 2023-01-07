package ca.uwindsor.css.ctf;

import java.util.*;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class CommandGetTeam {

	public static List<String> onTabComplete(String[] args) {
		if (args.length == 2) {
			// <player>

			// Create a list to hold the player names
			List<String> playerNames = new ArrayList<>();

			// Iterate over the collection of players and add each player's name to the list
			for (Player player : Bukkit.getServer().getOnlinePlayers()) {
				if (player.getName().contains(args[1]))
					playerNames.add(player.getName());
			}

			return playerNames;
		}

		return Collections.emptyList();
	}

	public static boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		Player player;

		if (args.length == 1) {
			player = (Player) sender;
		} else {
			player = Bukkit.getServer().getPlayerExact(args[1]);
		}

		if (player == null) {
			sender.sendMessage(ChatColor.RED + "Invalid user!");
		} else if (TeamManager.getPlayerTeam(player) == null) {
			sender.sendMessage(
					ChatColor.GREEN + "User " + player.getDisplayName() + ChatColor.GREEN + " is not part of a team.");
		} else {
			sender.sendMessage(ChatColor.GREEN + "Player " + player.getDisplayName() + ChatColor.GREEN
					+ " is part of team " + TeamManager.getPlayerTeam(player).printTeamName() + ChatColor.GREEN + ".");
		}

		return true;
	}

}
