package ca.uwindsor.css.ctf;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ca.uwindsor.css.ctf.commands.CommandAddTeam;
import ca.uwindsor.css.ctf.commands.CommandDeleteTeam;
import ca.uwindsor.css.ctf.commands.CommandGetFlag;
import ca.uwindsor.css.ctf.commands.CommandGetTeam;
import ca.uwindsor.css.ctf.commands.CommandHome;
import ca.uwindsor.css.ctf.commands.CommandList;
import ca.uwindsor.css.ctf.commands.CommandPvP;
import ca.uwindsor.css.ctf.commands.CommandRemovePlayer;
import ca.uwindsor.css.ctf.commands.CommandSetFlag;
import ca.uwindsor.css.ctf.commands.CommandSetScore;
import ca.uwindsor.css.ctf.commands.CommandSetTeam;
import ca.uwindsor.css.ctf.commands.CommandStart;
import ca.uwindsor.css.ctf.commands.Reset;
import net.md_5.bungee.api.ChatColor;

public class CommandManager implements CommandExecutor {

	private List<ca.uwindsor.css.ctf.commands.Command> commands = Arrays.asList(new Reset());
	private HashMap<String, ca.uwindsor.css.ctf.commands.Command> commandsMap = new HashMap<>();

	public CommandManager() {
		for (ca.uwindsor.css.ctf.commands.Command command : commands) {
			commandsMap.put(command.getName(), command);
		}
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length == 0) {
			// HELP PAGE
			sender.sendMessage(helpMessage(sender));
			return true;
		}

		String subCommand = args[0];

		if (commandsMap.containsKey(subCommand))
			return commandsMap.get(subCommand).onCommand(sender, args);

		if (subCommand.equalsIgnoreCase("create")) { // /teams create <teamName> <teamColor>
			if (sender.isOp()) {
				CommandAddTeam.onCommand(sender, command, label, args);
			} else {
				denyPermissions(sender);
			}
		} else if (subCommand.equalsIgnoreCase("enforce")) {
			Rules rules = new Rules();
			rules.enforceRules();
		} else if (subCommand.equalsIgnoreCase("list")) { // /teams list
			// list teams
			CommandList.onCommand(sender, command, label, args);
		} else if (subCommand.equalsIgnoreCase("add")) { // /teams add <player> <toTeam>
			// add player to team
			if (sender.isOp()) {
				CommandSetTeam.onCommand(sender, command, label, args);
			} else {
				denyPermissions(sender);
			}
		} else if (subCommand.equalsIgnoreCase("remove")) { // /teams remove <player> <fromTeam>
			// remove player from team
			if (sender.isOp()) {
				CommandRemovePlayer.onCommand(sender, command, label, args);
			} else {
				denyPermissions(sender);
			}
		} else if (subCommand.equalsIgnoreCase("get")) { // /teams get <player>
			// get user's team
			CommandGetTeam.onCommand(sender, command, label, args);
		} else if (subCommand.equalsIgnoreCase("info")) { // /teams info <team>
			// list team's users
		} else if (subCommand.equalsIgnoreCase("getflag")) { // /teams getflag <team>
			// get flag location
			if (sender.isOp()) {
				CommandGetFlag.onCommand(sender, command, label, args);
			} else {
				denyPermissions(sender);
			}
		} else if (subCommand.equalsIgnoreCase("setflag")) { // /teams setflag <team>
			// sets flag location for team
			CommandSetFlag.onCommand(sender, command, label, args);
		} else if (subCommand.equalsIgnoreCase("help")) {
			sender.sendMessage(helpMessage(sender));
		} else if (subCommand.equalsIgnoreCase("delete")) {
			if (sender.isOp()) {
				CommandDeleteTeam.onCommand(sender, command, label, args);
			} else {
				denyPermissions(sender);
			}
		} else if (subCommand.equalsIgnoreCase("home")) {
			CommandHome.onCommand(sender, command, label, args);
		} else if (subCommand.equalsIgnoreCase("setscore")) { // /teams getflag <team>
			if (sender.isOp()) {
				CommandSetScore.onCommand(sender, command, label, args);
			} else {
				denyPermissions(sender);
			}
		} else if (subCommand.equalsIgnoreCase("pvp")) {
			if (sender.isOp()) {
				CommandPvP.onCommand(sender, command, label, args);
			} else {
				denyPermissions(sender);
			}
		} else if (subCommand.equalsIgnoreCase("start")) {
			if (sender.isOp()) {
				CommandStart.onCommand(sender, command, label, args);
			} else {
				denyPermissions(sender);
			}
		}
		return true;
	}

	public String helpMessage(CommandSender sender) {

		String message = ChatColor.GREEN + "=============WindsorCTF===============";

		message += "\nCommands:";
		// ADMIN COMMANDS
		if (sender.isOp()) {
			message += "\n" + ChatColor.AQUA + "/teams help" + ChatColor.GREEN + " - list help menu for plugin";
			message += "\n" + ChatColor.AQUA + "/teams home" + ChatColor.GREEN + " - teleport to your flag";
			message += "\n" + ChatColor.AQUA + "/teams create <teamName> <teamColor>" + ChatColor.GREEN
					+ " - create new a team with the specified name/color";
			message += "\n" + ChatColor.AQUA + "/teams delete <teamName>" + ChatColor.GREEN
					+ " - delete a team completely (removes members as well)";
			message += "\n" + ChatColor.AQUA + "/teams enforce" + ChatColor.GREEN
					+ " - Enforces the rules upon all players (i.e. unenchants everything)";
			message += "\n" + ChatColor.AQUA + "/teams list" + ChatColor.GREEN + " - lists all of the current teams";
			message += "\n" + ChatColor.AQUA + "/teams add/remove <player> <team>" + ChatColor.GREEN
					+ " - add/remove user to/from specified team";
			message += "\n" + ChatColor.AQUA + "/teams setFlag <teamName>" + ChatColor.GREEN
					+ " - set the location of the specified team's flag";
			message += "\n" + ChatColor.AQUA + "/teams getFlag <teamName>" + ChatColor.GREEN
					+ " - get the location of the specified team's flag";
			message += "\n" + ChatColor.AQUA + "/teams setScore <teamName> <score>" + ChatColor.GREEN
					+ " - set specified team's score";
			message += "\n" + ChatColor.AQUA + "/teams get <player>" + ChatColor.GREEN
					+ " - get the specified user's team name";
			message += "\n" + ChatColor.AQUA + "/teams info <teamName>" + ChatColor.GREEN
					+ " - list info about the specified team";
			message += "\n" + ChatColor.AQUA + "/teams pvp true/false" + ChatColor.GREEN
					+ " - enable or disable pvp manually";
			message += "\n" + ChatColor.AQUA + "/teams start <timeInSeconds>" + ChatColor.GREEN
					+ " - start the countdown to game";
		} else {
			// REGULAR COMMANDS
			message += "\n" + ChatColor.AQUA + "/teams help" + ChatColor.GREEN + " - list help menu for plugin";
			message += "\n" + ChatColor.AQUA + "/teams home" + ChatColor.GREEN + " - teleport to your flag";
			message += "\n" + ChatColor.AQUA + "/teams list" + ChatColor.GREEN + " - lists all of the current teams";
			message += "\n" + ChatColor.AQUA + "/teams setFlag" + ChatColor.GREEN
					+ " - set the location of your team's flag";
			message += "\n" + ChatColor.AQUA + "/teams getFlag" + ChatColor.GREEN
					+ " - get the location of your team's flag";
			message += "\n" + ChatColor.AQUA + "/teams get" + ChatColor.GREEN + " - get your team's name";
			message += "\n" + ChatColor.AQUA + "/teams info <teamName>" + ChatColor.GREEN
					+ " - list info about your team";
		}
		message += "\n=====================================";
		return message;
	}

	public void denyPermissions(CommandSender sender) {
		sender.sendMessage(ChatColor.RED + "You do not have permission to do that!");
	}

	public static boolean isConsole(CommandSender sender) {
		return (sender instanceof Player);
	}

}
