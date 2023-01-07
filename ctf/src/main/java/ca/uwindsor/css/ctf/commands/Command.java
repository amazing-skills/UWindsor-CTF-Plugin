package ca.uwindsor.css.ctf.commands;

import java.util.List;

import org.bukkit.command.CommandSender;

public abstract class Command {
    public abstract String getName();

    public abstract List<String> onTabComplete(String[] args);

    public abstract boolean onCommand(CommandSender sender, String[] args);
}
