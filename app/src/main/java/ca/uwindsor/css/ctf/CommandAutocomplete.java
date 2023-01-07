package ca.uwindsor.css.ctf;

import java.util.ArrayList;
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
            List<String> commands = Arrays.asList("create", "enforce", "list", "add", "remove", "get", "info",
                    "getflag", "setflag",
                    "help", "delete", "home", "setscore", "pvp", "start");
            ArrayList<String> completions = new ArrayList<>();

            for (String s : commands)
                if (s.toLowerCase().contains(args[0].toLowerCase()))
                    completions.add(s);

            return completions;
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
                if (args.length == 2)
                    return Arrays.asList("true", "false");
            } else if (args[0].equalsIgnoreCase("start")) {
                List<Integer> commonTimeMinutes = Arrays.asList(0, 1, 2, 3, 5, 10, 15, 20, 30);
                ArrayList<String> commonTimeString = new ArrayList<>();

                for (int i : commonTimeMinutes) {
                    commonTimeString.add(Integer.toString(i * 60));
                }

                return commonTimeString;
            }
        }

        return Collections.emptyList();
    }

}
