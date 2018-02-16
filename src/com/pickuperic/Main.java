package com.pickuperic;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;

public class Main extends JavaPlugin {
	
	// Fired when first enabled
    @Override
    public void onEnable() {
    	this.getCommand("placeflag").setExecutor(new CommandPlaceFlag());
    	this.getCommand("getflag").setExecutor(new CommandGetFlag());
    	this.getCommand("setteam").setExecutor(new CommandSetTeam());
    	this.getCommand("getteam").setExecutor(new CommandGetTeam());
    	this.getCommand("addteam").setExecutor(new CommandAddTeam());
    	this.getCommand("getteams").setExecutor(new CommandGetTeams());
    	
    	Teams.availableColors.add("RED");
		Teams.availableColors.add("AQUA");
		Teams.availableColors.add("GOLD");
		Teams.availableColors.add("GRAY");
		Teams.availableColors.add("GREEN");
		Teams.availableColors.add("YELLOW");
		Teams.availableColors.add("BLUE");
		Teams.availableColors.add("LIGHT_PURPLE");
		Teams.availableColors.add("DARK_PURPLE");
		Teams.availableColors.add("WHITE");
		System.out.println(Teams.availableColors);
		
		
		
		Teams.addTeam("BASE", "WHITE");
		
		Scoreboard board = Bukkit.getScoreboardManager().getMainScoreboard();
		for (org.bukkit.scoreboard.Team team : board.getTeams()) {
			team.unregister();
		}
		
		for (String entry : board.getEntries()) {
			Player player = Bukkit.getServer().getPlayerExact(entry);
			if (player != null) {
				player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
			}
		}
		
		
    	getServer().getPluginManager().registerEvents(new FlagBreakListener(), this);
    	getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
    	getServer().getPluginManager().registerEvents(new FlagIndirectBreakListener(), this);
    }
    // Fired when disabled
    @Override
    public void onDisable() {

    }    

}

