package ca.uwindsor.css.ctf;

import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

// This is a custom ruleset primarily for my use case where the following are true:
// - Players start in creative and move to survival
// - Some players have access to commands
// To prevent this from destroying the flow of the game, the following rules are
// enforced upon the players
// - The player is not allowed to have any enchanted items

public class Rules implements Listener {
    BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
    Plugin plugin = JavaPlugin.getPlugin(Main.class);

    private Boolean isEnchanted(ItemStack item) {
        return item.getEnchantments().size() != 0;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClick(InventoryClickEvent event) {
        HumanEntity entity = event.getWhoClicked();

        if (entity instanceof Player) {
            Player player = (Player) entity;
            System.out.println("isPlayer");

            // If the player is on a team, do nothing
            if (!TeamManager.playerHasTeam(player))
                return;

            System.out.println("hasTeam");
            System.out.println(isEnchanted(event.getCurrentItem()));
            // Warn the player and unenchant the item. Note that the item will
            // be null if it is thrown out of the inventory
            if (event.getCurrentItem() != null && isEnchanted(event.getCurrentItem())) {
                event.setCurrentItem(warnAndRemoveEnchant(player, event.getCurrentItem()));
                event.setCancelled(true);

                scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {
                    @Override
                    public void run() {
                        player.updateInventory();
                    }
                });
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        // If the player is on a team, do nothing
        if (!TeamManager.playerHasTeam(player))
            return;

        System.out.println("hasTeam");
        System.out.println(isEnchanted(event.getItem()));
        // Warn the player and unenchant the item
        if (isEnchanted(event.getItem())) {
            player.sendMessage(
                    "You are not allowed to equipt enchanted items!");
            event.setCancelled(true);
        }
    }

    public ItemStack warnAndRemoveEnchant(Player player, ItemStack item) {
        player.sendMessage(
                "You are not allowed to posses enchanted items during a capture the flag game! They will now be unenchanted");

        for (Enchantment ench : item.getEnchantments().keySet()) {
            item.removeEnchantment(ench);
        }

        return item;
    }

    public void enforceRules() {
        for (Team team : TeamManager.getTeams()) {
            for (Player player : team.getPlayers()) {
                ItemStack[] inventoryContents = player.getInventory().getContents();

                for (int i = 0; i < inventoryContents.length; i++) {
                    ItemStack item = inventoryContents[i];

                    if (item != null && isEnchanted(item)) {
                        player.getInventory().setItem(i, warnAndRemoveEnchant(player, item));
                    }
                }
            }
        }
    }
}
