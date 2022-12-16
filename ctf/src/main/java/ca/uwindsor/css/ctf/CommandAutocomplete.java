package ca.uwindsor.css.ctf;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class CommandAutocomplete implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return Arrays.asList("create", "list", "add", "remove", "get", "info", "getflag", "setflag", "help", "delete", "home", "setscore", "pvp", "start");
        }

        if (args.length >= 2) {
            if (args[0].equalsIgnoreCase("create")) {
                return CommandAddTeam.onTabComplete(args);
            } else if (args[0].equalsIgnoreCase("list")) {
                return Arrays.asList("");
            } else if (args[0].equalsIgnoreCase("add")) {
                return CommandSetTeam.onTabComplete(args);
            } else if (args[0].equalsIgnoreCase("remove")) {
                return CommandSetTeam.onTabComplete(args);
            } else if (args[0].equalsIgnoreCase("get")) {
                return CommandGetTeam.onTabComplete(args);
            } else if (args[0].equalsIgnoreCase("info")) {
                return CommandGetFlag.onTabComplete(args);
            } else if (args[0].equalsIgnoreCase("getflag")) {
                return CommandGetFlag.onTabComplete(args);
            } else if (args[0].equalsIgnoreCase("setflag")) {
                return CommandGetFlag.onTabComplete(args);
            } else if (args[0].equalsIgnoreCase("help")) {
                return Arrays.asList("");
            } else if (args[0].equalsIgnoreCase("delete")) {
                return CommandGetFlag.onTabComplete(args);
            } else if (args[0].equalsIgnoreCase("home")) {
                return CommandGetFlag.onTabComplete(args);
            } else if (args[0].equalsIgnoreCase("setscore")) {
                return Arrays.asList("<team>", "<score>");
            } else if (args[0].equalsIgnoreCase("pvp")) {
                return Arrays.asList("<team>", "<true/false>");
            } else if (args[0].equalsIgnoreCase("start")) {
                return Arrays.asList("");
            }
        }

        return Collections.emptyList();
    }

}
