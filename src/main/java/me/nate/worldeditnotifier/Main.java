package me.nate.worldeditnotifier;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.Set;

public class Main extends JavaPlugin implements Listener {
    private Set<String> notifiedPlayers = new HashSet<>();

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        String message = event.getMessage();
        Player player = event.getPlayer();

        if (!message.startsWith("//") && !message.startsWith("/we:") && !message.startsWith("/worldedit:")) {
            return;
        }

        String permissionToCheck = "worldeditnotifier.notify";

        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.hasPermission(permissionToCheck) && !notifiedPlayers.contains(p.getName())) {
                p.sendMessage(ChatColor.RED + player.getName() + " is using WorldEdit. Command: " + message);
                notifiedPlayers.add(p.getName());
            }
        }

        // Clear the notifiedPlayers set after the command is executed
        Bukkit.getScheduler().runTaskLater(this, () -> {
            notifiedPlayers.clear();
        }, 1);
    }
}
