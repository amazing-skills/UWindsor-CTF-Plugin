package ca.uwindsor.css.ctf.commands;

import java.util.*;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ca.uwindsor.css.ctf.TeamManager;
import net.md_5.bungee.api.ChatColor;

public class CommandSetTeam {

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
		} else if (args.length == 3) {
			return new ArrayList<String>(TeamManager.getTeamNames());
		}

		return Collections.emptyList();
	}

	public static boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if (args.length < 3) {
			sender.sendMessage(ChatColor.RED + "Invalid arguments: /teams add <player> <team>");

		} else {

			String playerName = args[1];
			String team = args[2].toUpperCase();
			Player player = Bukkit.getServer().getPlayerExact(playerName);

			if (player == null) {
				sender.sendMessage(ChatColor.RED + "Invalid user.");
			} else if (!TeamManager.containsTeam(team)) {
				sender.sendMessage(ChatColor.RED + "Invalid team name. Please choose from: "
						+ String.join(", ", TeamManager.getTeamNames()));
			} else {
				TeamManager.purgePayer(player);// Remove player from all other teams
				TeamManager.getTeam(team.toUpperCase()).addPlayer(player);
				sender.sendMessage(ChatColor.GREEN + "Successfully added " + player.getDisplayName() + ChatColor.GREEN
						+ " to team " + TeamManager.getTeam(team.toUpperCase()).printTeamName() + ChatColor.GREEN
						+ ".");
			}
		}

		return true;
	}

}
