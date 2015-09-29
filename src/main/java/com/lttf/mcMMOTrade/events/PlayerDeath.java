package com.lttf.mcMMOTrade.events;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Nick (lttf) on 27/09/2015.
 */
public class PlayerDeath implements Listener {

    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity().getPlayer();

        // Calculates 5% Chance
        Double random = Math.random() * 100;
        if (random <= 5) {
            giveDeathCertificate(player);
        }
    }

    private void giveDeathCertificate(Player player) {
        // Date of Death
        String timeStamp = new SimpleDateFormat("MMddyyyy").format(Calendar.getInstance().getTime());

        // Death Paper
        ItemStack deathpaper = new ItemStack(Material.PAPER);
        ItemMeta deathpapermeta = deathpaper.getItemMeta();
        deathpapermeta.setDisplayName(String.format("%s%s%s's Death Certificate", ChatColor.RED, ChatColor.BOLD,
                player.getName()));
        List<String> deathpaperlore = new ArrayList<String>();
        deathpaperlore.add("");
        deathpaperlore.add(ChatColor.GREEN + "Date of Death: " + ChatColor.GRAY + (timeStamp));
        deathpaperlore.add("");
        deathpapermeta.setLore(deathpaperlore);
        deathpaper.setItemMeta(deathpapermeta);

        player.getKiller().getInventory().addItem(deathpaper);
    }

}
