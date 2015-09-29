package com.lttf.mcMMOTrade.commands;

import com.gmail.nossr50.api.ExperienceAPI;
import com.gmail.nossr50.api.exceptions.InvalidSkillException;
import com.lttf.mcMMOTrade.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by Nick (lttf) on 27/09/2015.
 */
public class ExpTrade implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Player player = (Player) commandSender;

        if (Objects.equals(Main.experience, "TRUE")) {

            if (strings.length < 1) {
                player.sendMessage("");
                player.sendMessage(String.format("%s---[]%sEXPTRADE COMMANDS%s%s[]---", ChatColor.RED, ChatColor.GREEN, ChatColor.RESET, ChatColor.RED));
                player.sendMessage(String.format("%s/exptrade %s- View this current help menu.", ChatColor.DARK_AQUA, ChatColor.GREEN));
                player.sendMessage(String.format("%s/exptrade info %s- Information about how to use plugin.", ChatColor.DARK_AQUA, ChatColor.GREEN));
                player.sendMessage(String.format("%s/exptrade create <skill> <experience>%s - Create voucher.", ChatColor.DARK_AQUA, ChatColor.GREEN));
                if (player.hasPermission("mcMMOTrade.Experience.Admin")) {
                    player.sendMessage(String.format("%s/exptrade give <name> <skill> <experience>%s - Give voucher.", ChatColor.DARK_AQUA, ChatColor.GREEN));
                }
                return false;
            } else if (strings.length > 4) {
                player.sendMessage(String.format("%s[%s%s%sExpTrade%s] %s%sSyntax Error! Type /exptrade to view help menu.", ChatColor.DARK_GRAY, ChatColor.RESET,
                        ChatColor.GREEN, ChatColor.BOLD, ChatColor.DARK_GRAY, ChatColor.RESET, ChatColor.RED));
                return false;
            }

            if (strings[0].equals("give")) {

                if (player.hasPermission("mcMMOTrade.Experience.Admin")) {

                    if (!(strings.length == 4)) {
                        player.sendMessage(String.format("%s[%s%s%sExpTrade%s] %s%sSyntax Error! Not enough arugements given!", ChatColor.DARK_GRAY, ChatColor.RESET,
                                ChatColor.GREEN, ChatColor.BOLD, ChatColor.DARK_GRAY, ChatColor.RESET, ChatColor.RED));
                        return false;
                    }

                    String name = strings[1];
                    String skill = strings[2].toUpperCase();
                    Integer amount;

                    if (Bukkit.getServer().getPlayer(name) == null) {
                        player.sendMessage(String.format("%s[%s%s%sExpTrade%s] %s%sError - Player is currently not online!", ChatColor.DARK_GRAY, ChatColor.RESET,
                                ChatColor.GREEN, ChatColor.BOLD, ChatColor.DARK_GRAY, ChatColor.RESET, ChatColor.RED));
                        return false;
                    }

                    try {
                        amount = Integer.parseInt(strings[3]);
                    } catch (NumberFormatException exception) {
                        player.sendMessage(String.format("%s[%s%s%sExpTrade%s] %s%sError - Please only enter integers (whole numbers)!", ChatColor.DARK_GRAY, ChatColor.RESET,
                                ChatColor.GREEN, ChatColor.BOLD, ChatColor.DARK_GRAY, ChatColor.RESET, ChatColor.RED));
                        return false;
                    }

                    if (!(ExperienceAPI.isValidSkillType(skill))) {
                        player.sendMessage(String.format("%s[%s%s%sExpTrade%s] %s%sError - You've entered an invalid skill!", ChatColor.DARK_GRAY, ChatColor.RESET,
                                ChatColor.GREEN, ChatColor.BOLD, ChatColor.DARK_GRAY, ChatColor.RESET, ChatColor.RED));
                        return false;
                    }

                    player.sendMessage(String.format("%s[%s%s%sExpTrade%s]%s%s %s has received a note for %s experience for %s", ChatColor.DARK_GRAY, ChatColor.RESET,
                            ChatColor.GREEN, ChatColor.BOLD, ChatColor.DARK_GRAY, ChatColor.RESET, ChatColor.GRAY, name, amount, skill));

                    giveMcMMOPaper((Player) Bukkit.getServer().getPlayer(name), skill, amount);
                    player.getPlayer().playSound(player.getLocation(), Sound.NOTE_PLING, 1, 1);
                    return true;
                }

                player.sendMessage(String.format("%s[%s%s%sExpTrade%s] %s%sError! You do not have permission to use this command!", ChatColor.DARK_GRAY,
                        ChatColor.RESET, ChatColor.GREEN, ChatColor.BOLD, ChatColor.DARK_GRAY, ChatColor.RESET, ChatColor.RED));
                return false;
            }

            if (strings[0].equals("create")) {

                if (player.hasPermission("mcMMOTrade.Experience.User")) {

                    if (!(strings.length == 3)) {
                        player.sendMessage(String.format("%s[%s%s%sExpTrade%s] %s%sSyntax Error! Not enough arugements given!", ChatColor.DARK_GRAY, ChatColor.RESET,
                                ChatColor.GREEN, ChatColor.BOLD, ChatColor.DARK_GRAY, ChatColor.RESET, ChatColor.RED));
                        return false;
                    }

                    String skill = strings[1].toUpperCase();
                    Integer amount;
                    Integer experience;

                    try {
                        amount = Integer.parseInt(strings[2]);
                    } catch (NumberFormatException exception) {
                        player.sendMessage(String.format("%s[%s%s%sExpTrade%s] %s%sError - Please only enter integers (whole numbers)!",
                                ChatColor.DARK_GRAY, ChatColor.RESET, ChatColor.GREEN, ChatColor.BOLD, ChatColor.DARK_GRAY,
                                ChatColor.RESET, ChatColor.RED));
                        return false;
                    }

                    try {
                        experience = ExperienceAPI.getXP((Player) commandSender, skill);
                    } catch (InvalidSkillException exception) {
                        player.sendMessage(String.format("%s[%s%s%sExpTrade%s] %s%sError - You've entered an invalid skill!",
                                ChatColor.DARK_GRAY, ChatColor.RESET, ChatColor.GREEN, ChatColor.BOLD, ChatColor.DARK_GRAY,
                                ChatColor.RESET, ChatColor.RED));
                        return false;
                    }

                    if (amount > experience) {
                        player.sendMessage(String.format("%s[%s%s%sExpTrade%s] %s%sError - Do not withdraw amount greater than current XP!",
                                ChatColor.DARK_GRAY, ChatColor.RESET, ChatColor.GREEN, ChatColor.BOLD, ChatColor.DARK_GRAY,
                                ChatColor.RESET, ChatColor.RED));
                        return false;
                    }

                    try {
                        ExperienceAPI.setXP((Player) commandSender, skill, (experience - amount));
                    } catch (InvalidSkillException exception) {
                        player.sendMessage(String.format("%s[%s%s%sExpTrade%s] %s%sError - You've entered an invalid skill!",
                                ChatColor.DARK_GRAY, ChatColor.RESET, ChatColor.GREEN, ChatColor.BOLD, ChatColor.DARK_GRAY,
                                ChatColor.RESET, ChatColor.RED));
                        return false;
                    }

                    player.sendMessage(String.format("%s[%s%s%sExpTrade%s] %s%s%s experience has been withdrawn from %s",
                            ChatColor.DARK_GRAY, ChatColor.RESET, ChatColor.GREEN, ChatColor.BOLD, ChatColor.DARK_GRAY,
                            ChatColor.RESET, ChatColor.GRAY, amount, skill));

                    giveMcMMOPaper((Player) commandSender, skill, amount);
                    player.getPlayer().playSound(player.getLocation(), Sound.NOTE_PLING, 1, 1);
                    return true;
                }
                player.sendMessage(String.format("%s[%s%s%sExpTrade%s] %s%sError! You do not have permission to use this command!", ChatColor.DARK_GRAY,
                        ChatColor.RESET, ChatColor.GREEN, ChatColor.BOLD, ChatColor.DARK_GRAY, ChatColor.RESET, ChatColor.RED));
                return false;
            }
            if (strings[0].equals("info")) {
                player.sendMessage("");
                player.sendMessage(String.format("%s---[]%sEXPTRADE INFO%s%s[]---", ChatColor.RED, ChatColor.GREEN, ChatColor.RESET, ChatColor.RED));
                player.sendMessage(String.format("%s'ExpTrade' allows players to convert their mcMMO experience into a tradeable form, " +
                                "in this case paper! Simply type the command: '%s/exptrade create <skill> <experience>%s' and the total experience " +
                                "entered will be withdrawn from your selected skill. You'll receive a paper, which can be given to anyone. " +
                                "Once a player receives the paper, they simply need to right click it to receive the experience.",
                        ChatColor.DARK_AQUA, ChatColor.GREEN, ChatColor.DARK_AQUA));
                return true;
            }
            return false;
        }

        if (Objects.equals(Main.experience, "FALSE")) {
            if (player.isOp()) {
                player.sendMessage(String.format("%s[%s%s%smcMMOTrade%s] %s%sExperience trading has been disabled in the config!",
                        ChatColor.DARK_GRAY, ChatColor.RESET, ChatColor.GREEN, ChatColor.BOLD, ChatColor.DARK_GRAY,
                        ChatColor.RESET, ChatColor.RED));
                return false;
            }
        }

        player.sendMessage(String.format("%s[%s%s%smcMMOTrade%s] %s%sError - Incorrect value set for config! Set either true or false.",
                ChatColor.DARK_GRAY, ChatColor.RESET, ChatColor.GREEN, ChatColor.BOLD, ChatColor.DARK_GRAY,
                ChatColor.RESET, ChatColor.RED));
        return false;
    }

    private void giveMcMMOPaper(Player player, String skill, Integer amount) {

        ItemStack mcmmopaper = new ItemStack(Material.PAPER);
        ItemMeta mcmmopapermeta = mcmmopaper.getItemMeta();
        mcmmopapermeta.setDisplayName(String.format("%s%sMCMMO Experience Voucher %s%s(Right Click)", ChatColor.YELLOW, ChatColor.BOLD,
                ChatColor.RESET, ChatColor.GRAY));
        List<String> mcmmopaperlore = new ArrayList<String>();
        mcmmopaperlore.add("");
        mcmmopaperlore.add(ChatColor.GREEN + "Skill: " + ChatColor.GRAY + skill);
        mcmmopaperlore.add(ChatColor.GREEN + "Experience: " + ChatColor.GRAY + amount);
        mcmmopaperlore.add("");
        mcmmopapermeta.setLore(mcmmopaperlore);
        mcmmopaper.setItemMeta(mcmmopapermeta);

        player.getInventory().addItem(mcmmopaper);

    }

}
