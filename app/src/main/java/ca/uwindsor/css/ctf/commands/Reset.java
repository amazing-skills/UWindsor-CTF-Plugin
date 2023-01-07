package ca.uwindsor.css.ctf.commands;

import java.util.Arrays;
import java.util.List;

import org.bukkit.command.CommandSender;

import ca.uwindsor.css.ctf.Team;
import ca.uwindsor.css.ctf.TeamManager;

public class Reset extends Command {

    @Override
    public String getName() {
        return "reset";
    }

    @Override
    public List<String> onTabComplete(String[] args) {
        return Arrays.asList("");
    }

    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
        for (Team team : TeamManager.getTeams()) {
            TeamManager.removeTeam(team);
        }

        sender.sendMessage("Teams reset.");

        return true;
    }

}
