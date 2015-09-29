package com.lttf.mcMMOTrade.events;

import com.gmail.nossr50.api.ExperienceAPI;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


/**
 * Created by Nick (lttf) on 27/09/2015.
 */
public class PlayerInteract implements Listener {

    @EventHandler
    public void onPaperClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getAction() == Action.RIGHT_CLICK_AIR) {
            if (player.getItemInHand().getType() == Material.PAPER) {
                ItemStack i = player.getItemInHand();
                ItemMeta im = i.getItemMeta();
                if (im.getDisplayName().equals(String.format("%s%sMCMMO Experience Voucher %s%s(Right Click)", ChatColor.YELLOW,
                        ChatColor.BOLD, ChatColor.RESET, ChatColor.GRAY))) {

                    // Retrieve Skill Name
                    String firstline = im.getLore().get(1);
                    String skill = firstline.split("\\:")[1];
                    skill = skill.replaceAll("\\s", "");
                    skill = ChatColor.stripColor(skill);

                    // Check if Skill Name is a valid
                    if (!(ExperienceAPI.isValidSkillType(skill))) {
                        player.getInventory().setItemInHand(new ItemStack(Material.AIR, 1));
                        player.sendMessage(String.format("%s[%s%s%smcMMOTrade%s] %s%sError - That note has an invalid skill!",
                                ChatColor.DARK_GRAY, ChatColor.RESET, ChatColor.GREEN, ChatColor.BOLD,
                                ChatColor.DARK_GRAY, ChatColor.RESET, ChatColor.RED));
                    }

                    // Retrieve Experience Integer
                    String secondline = im.getLore().get(2);
                    String exp = secondline.split("\\:")[1];
                    exp = exp.replaceAll("\\s", "");
                    exp = ChatColor.stripColor(exp);
                    Integer intexp = Integer.parseInt(exp);

                    // Set Experience
                    Integer newexp = intexp + ExperienceAPI.getXP(player, skill);
                    MCMMOLevelUp(player, intexp, skill);
                    player.sendMessage(String.format("%s[%s%s%smcMMOTrade%s] %s%s%s experience has been added to %s",
                            ChatColor.DARK_GRAY, ChatColor.RESET, ChatColor.GREEN, ChatColor.BOLD, ChatColor.DARK_GRAY,
                            ChatColor.RESET, ChatColor.GRAY, intexp, skill));
                    player.getPlayer().playSound(player.getLocation(), Sound.NOTE_PLING, 1, 1);

                    // Remove SINGLE item from hand (in case of stacking).
                    if (player.getInventory().getItemInHand().getAmount() > 1) {
                        player.getInventory().getItemInHand().setAmount(player.getInventory().getItemInHand().getAmount() - 1);
                    } else {
                        player.getInventory().setItemInHand(new ItemStack(Material.AIR, 1));
                    }
                }

                if (im.getDisplayName().equals(String.format("%s%sMCMMO Level Voucher %s%s(Right Click)", ChatColor.YELLOW,
                        ChatColor.BOLD, ChatColor.RESET, ChatColor.GRAY))) {

                    // Retrieve Skill Name
                    String firstline = im.getLore().get(1);
                    String skill = firstline.split("\\:")[1];
                    skill = skill.replaceAll("\\s", "");
                    skill = ChatColor.stripColor(skill);

                    // Check if Skill Name is a valid
                    if (!(ExperienceAPI.isValidSkillType(skill))) {
                        player.getInventory().setItemInHand(new ItemStack(Material.AIR, 1));
                        player.sendMessage(String.format("%s[%s%s%smcMMOTrade%s] %s%sError - That note has an invalid skill!",
                                ChatColor.DARK_GRAY, ChatColor.RESET, ChatColor.GREEN, ChatColor.BOLD,
                                ChatColor.DARK_GRAY, ChatColor.RESET, ChatColor.RED));
                    }

                    // Retrieve Level Integer
                    String secondline = im.getLore().get(2);
                    String level = secondline.split("\\:")[1];
                    level = level.replaceAll("\\s", "");
                    level = ChatColor.stripColor(level);
                    Integer intLevel = Integer.parseInt(level);

                    // Set New Level
                    Integer currentLevel = ExperienceAPI.getLevel(player, skill);
                    ExperienceAPI.setLevel(player, skill, currentLevel + intLevel);
                    player.sendMessage(String.format("%s[%s%s%smcMMOTrade%s] %s%s%s level(s) have been added to %s",
                            ChatColor.DARK_GRAY, ChatColor.RESET, ChatColor.GREEN, ChatColor.BOLD, ChatColor.DARK_GRAY,
                            ChatColor.RESET, ChatColor.GRAY, intLevel, skill));
                    player.getPlayer().playSound(player.getLocation(), Sound.NOTE_PLING, 1, 1);

                    // Remove SINGLE item from hand (in case of stacking).
                    if (player.getInventory().getItemInHand().getAmount() > 1) {
                        player.getInventory().getItemInHand().setAmount(player.getInventory().getItemInHand().getAmount() - 1);
                    } else {
                        player.getInventory().setItemInHand(new ItemStack(Material.AIR, 1));
                    }

                }
            }
        }
    }

    // Level players up accordingly to the amount of experience they received.
    public void MCMMOLevelUp(Player player, Integer exp, String skill) {
        Integer remainingexp = ExperienceAPI.getXPRemaining(player, skill);
        Integer currentexp = ExperienceAPI.getXP(player, skill);

        /*
        *   Checks whether the experience added to the player
        *   will lower "Remaining Experience" into negatives, therefore +1 level.
        */

        if (remainingexp - exp <= 0) {
            Integer currentLevel = ExperienceAPI.getLevel(player, skill);
            ExperienceAPI.setLevel(player, skill, currentLevel + 1);
            ExperienceAPI.setXP(player, skill, (exp - remainingexp));

            /*
             *   If the "Remaining Experience" is still negative,
             *   loop until "Remaining Experience" becomes positive.
             */

            if (ExperienceAPI.getXPRemaining(player, skill) <= 0) {

                // Assign variable "exptotal" to total Experience received in Voucher.
                // Also accounts for the previous reduction of experience prior.
                Integer exptotal = (exp - remainingexp);

                while (ExperienceAPI.getXPRemaining(player, skill) < exptotal) {
                    // Assign "level" to current level of player.
                    Integer level = ExperienceAPI.getLevel(player, skill);
                    // Assign "experienceneeded" to XP amount needed to level up of user.
                    Integer experienceneeded = ExperienceAPI.getXpNeededToLevel(level);
                    // Reduce "xptotal" by experience needed. "While" repeats repeats until XPRemaing < "exptotal"
                    exptotal = exptotal - experienceneeded;
                    ExperienceAPI.setLevel(player, skill, (ExperienceAPI.getLevel(player, skill) + 1));
                }
                // Set Experience of user to the "exptotal" + current experience.
                ExperienceAPI.setXP(player, skill, ExperienceAPI.getXP(player, skill) + exptotal);
            }
        } else {
            ExperienceAPI.setXP(player, skill, currentexp + exp);
        }
    }
}
