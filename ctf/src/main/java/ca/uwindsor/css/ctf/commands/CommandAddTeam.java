package ca.uwindsor.css.ctf.commands;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import ca.uwindsor.css.ctf.TeamManager;
import net.md_5.bungee.api.ChatColor;

public class CommandAddTeam {
	
	public static List<String> onTabComplete(String[] args) {
		if (args.length == 2) {
			return Arrays.asList("<teamName>");
		} else if (args.length == 3) {
			return TeamManager.availableColors;
		}

		return Collections.emptyList();
	}

	public static boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		//Check if missing teamName or teamColor
		if (args.length < 3) {
			sender.sendMessage(ChatColor.RED + "Invalid arguments: /teams create <teamName> <teamColor>");
			return true;
		} 
		
		try {
			ChatColor.valueOf(args[2].toUpperCase());
			String teamName = args[1];
			String teamColor = args[2].toUpperCase();
			if (TeamManager.addTeam(teamName, teamColor) == true) {
				sender.sendMessage(ChatColor.GREEN + "Successfully added team " + ChatColor.valueOf(teamColor) + ChatColor.BOLD + teamName + 
				ChatColor.RESET + ChatColor.GREEN + " to list of teams.");
			} else {
				sender.sendMessage(ChatColor.RED + "Error: Team name or color already in use.");
			}
			
		} catch (IllegalArgumentException e) {
			sender.sendMessage(ChatColor.RED + "Invalid color. Please choose from: " + String.join(", ", TeamManager.availableColors));
		}
		return true; 
		
	}

}
