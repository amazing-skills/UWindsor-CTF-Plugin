package ca.uwindsor.css.ctf;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Banner;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import net.md_5.bungee.api.ChatColor;

public class Team {
	
	private String teamName;
	private String teamColor;
	public ArrayList<String> members = new ArrayList<String>();
	private Block bannerBlock;
	private Block stolenBannerBlock;
	private DyeColor bannerColor;
	private Location bannerSpawnLocation;	//Needed to restore banner
	public static Scoreboard sb = Bukkit.getScoreboardManager().getMainScoreboard();
	private org.bukkit.scoreboard.Team scoreboardTeam;
	private boolean flagStolen;
	private Score score;
	
	//Constructor
	public Team (String teamName, String teamColor) {
		
		this.teamName = teamName;
		this.teamColor = teamColor.toUpperCase();
		this.bannerBlock = null;
		this.teamColor = teamColor.toUpperCase();
		this.scoreboardTeam = sb.registerNewTeam(teamName.toUpperCase());
		this.scoreboardTeam.setPrefix(getColor().toString());
		this.flagStolen = false;
		this.bannerSpawnLocation = new Location(Bukkit.getServer().getWorlds().get(0),0,0,0);
		this.score = Main.board.getObjective("Scoreboard").getScore(printTeamName() + ":");
		this.score.setScore(0);
		setBannerColor();
	}
	
	public int getScore() {
		return this.score.getScore();
	}
	
	public void setScore(int newScore) {
		this.score.setScore(newScore);
	}
	
	public void addPoint() {
		this.score.setScore(this.score.getScore() + 1);
		for (Player player : Bukkit.getServer().getOnlinePlayers()) {
			player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 10f, 0.5f);
		}
	}
	
	public void addPlayer(Player player) {
		
		members.add(player.getName());
		player.sendMessage(ChatColor.GREEN + "You've been added to team " + printTeamName() + ChatColor.GREEN + ".");
		scoreboardTeam.addEntry(player.getName());
		player.setDisplayName(getColor() + player.getName() + ChatColor.RESET);
		player.setPlayerListName(getColor() + player.getName() + ChatColor.RESET);
	}
	
	public void addPlayerByName(String playerName) {
		members.add(playerName);
		Player player = Bukkit.getServer().getPlayerExact(playerName);
		if (player != null) {
			scoreboardTeam.addEntry(player.getName());
			player.setDisplayName(getColor() + player.getName() + ChatColor.RESET);
			player.setPlayerListName(getColor() + player.getName() + ChatColor.RESET);
		}
	}
	
	public void removePlayer(Player player) {
		if (members.remove(player.getName())) {	//If the player was successfully removed
			player.sendMessage(ChatColor.GREEN + "You've been removed from team " + ChatColor.valueOf(teamColor) + ChatColor.BOLD + teamName + 
			ChatColor.RESET + ChatColor.GREEN + ".");
			scoreboardTeam.removeEntry(player.getName());
			player.setDisplayName(player.getName());
			player.setPlayerListName(player.getName());
		}
	}
	
	public void addBanner(Block bannerBlock) {
		
			this.bannerBlock = bannerBlock;
			this.bannerSpawnLocation = bannerBlock.getLocation();
		
	}
	
	public void addStolenBanner(Block bannerBlock) {	//Temp banner is when banner is captured and the capturer is killed
		this.stolenBannerBlock = bannerBlock;
	}
	
	public Block getStolenBanner() {
		return this.stolenBannerBlock;
	}
	
	public void removeStolenBanner() {
		if (stolenBannerBlock != null) {
			stolenBannerBlock.setType(Material.AIR);
			this.stolenBannerBlock = null;
		}
	}

	public void setStolenStatus(boolean status) {
		this.flagStolen = status;
	}
	
	public boolean getStolenStatus() {
		return this.flagStolen;
	}
	
	public Location getBannerSpawn() {
		return this.bannerSpawnLocation;
	}
	
	public void restoreBanner() {
		removeStolenBanner();
		World w = Bukkit.getServer().getWorlds().get(0);
		Block block = w.getBlockAt(this.bannerSpawnLocation);
		block.setType(getBannerMaterial());
		Banner banner = (Banner) block.getState();
		banner.setBaseColor(getBannerColor());
		banner.update();
		addBanner(block);
		flagStolen = false;
		System.out.println("Restored " + getName() + " banner at " + this.bannerSpawnLocation);
	}
	
	public void addBannerByCoords(String[] coords) {
		if (this.bannerBlock != null) {
			removeBanner();
		} else {
			World w = Bukkit.getServer().getWorlds().get(0);
			Block block = w.getBlockAt(Integer.parseInt(coords[0]), Integer.parseInt(coords[1]), Integer.parseInt(coords[2]));
			block.setType(getBannerMaterial());
			Banner banner = (Banner) block.getState();
			banner.setBaseColor(getBannerColor());
			banner.update();
			this.bannerBlock = block;
			this.bannerSpawnLocation = bannerBlock.getLocation();
		}
	}
	
	public void removeBanner() {
		this.bannerBlock.setType(Material.AIR);
		this.bannerBlock = null;
	}
	
	public boolean hasBanner() {
		if (bannerBlock == null) {
			return false;
		} else {
			return true;
		}
	}
	
	public void setBannerColor() {
		if (this.teamColor.equals("GOLD"))
			this.bannerColor = DyeColor.ORANGE;
		else if (this.teamColor.equals("DARK_PURPLE"))
			this.bannerColor = DyeColor.PURPLE;
		else if (this.teamColor.equals("AQUA"))
			this.bannerColor = DyeColor.CYAN;
		else if (this.teamColor.equals("LIGHT_PURPLE"))
			this.bannerColor = DyeColor.PINK;
		else
			this.bannerColor = DyeColor.valueOf(teamColor);
	}
	
	public DyeColor getBannerColor() {
		return this.bannerColor;
	}
	
	public Material getBannerMaterial() {
		if (this.teamColor.equals("GOLD"))
			return Material.ORANGE_BANNER;
		else if (this.teamColor.equals("DARK_PURPLE"))
			return Material.PURPLE_BANNER;
		else if (this.teamColor.equals("AQUA"))
			return Material.CYAN_BANNER;
		else if (this.teamColor.equals("LIGHT_PURPLE"))
			return Material.PINK_BANNER;
		else
			return Material.valueOf(teamColor + "_BANNER");
	}
	
	public boolean containsPlayer(Player player) {
		return members.contains(player.getName());
	}
	
	public List<Player> getPlayers() {
		List<Player> players = new ArrayList<Player>();
		Player player;
		for (String member : this.members) {
			player = Bukkit.getServer().getPlayerExact(member);
			if (player != null)
				players.add(player);
		}
		return players;
	}
	
	public ChatColor getColor() {
		return ChatColor.valueOf(teamColor);
	}
	
	public String getColorString() {
		return this.teamColor;
	}
	
	public String getName() {
		return teamName;
	}
	
	public String getBannerCoordinates() {
		if (hasBanner()) {
			return ("X: " + bannerBlock.getX() + " Y: " + bannerBlock.getY() + " Z: " + bannerBlock.getZ());
		} else {
			return null;
		}
	}
	
	public String getBannerCoordinatesConfig() {
		if (bannerBlock == null) {
			return null;
		} else {
			return (this.bannerSpawnLocation.getBlockX() + " " + this.bannerSpawnLocation.getBlockY() + " " + this.bannerSpawnLocation.getBlockZ());
		}
	}
	
	public String printTeamName() {
		return ("" + getColor() + ChatColor.BOLD + this.teamName + ChatColor.RESET);
	}
	
	public Block getBannerBlock() {
		return bannerBlock;
	}
	
	public void purge() {
		Player player;
		for (String playerName : members) {
			player = Bukkit.getServer().getPlayerExact(playerName);
			//If player is in-game, strip them of their chat/nametag color
			if (player != null) {
				player.sendMessage(ChatColor.GREEN + "You've been removed from team " + printTeamName() + ChatColor.GREEN + ".");
				scoreboardTeam.removeEntry(player.getName());
				player.setDisplayName(player.getName());
				player.setPlayerListName(player.getName());
			}
		}
		if (this.bannerBlock != null) {
			this.bannerBlock.setType(Material.AIR);
			this.bannerBlock = null;
		}
		this.scoreboardTeam.unregister();
		Main.board.resetScores(printTeamName() + ":");
		TeamManager.availableColors.add(getColorString());
	}
	
}
