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
 * Created by Nick (lttf) on 29/09/2015.
 */
public class LevelTrade implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Player player = (Player) commandSender;

        if (Objects.equals(Main.levels, "TRUE")) {

            if (strings.length < 1) {
                player.sendMessage("");
                player.sendMessage(String.format("%s---[]%sLEVELTRADE COMMANDS%s%s[]---", ChatColor.RED, ChatColor.GREEN, ChatColor.RESET, ChatColor.RED));
                player.sendMessage(String.format("%s/leveltrade %s- View this current help menu.", ChatColor.DARK_AQUA, ChatColor.GREEN));
                player.sendMessage(String.format("%s/leveltrade info %s- Information about how to use plugin.", ChatColor.DARK_AQUA, ChatColor.GREEN));
                player.sendMessage(String.format("%s/leveltrade create <skill> <level>%s - Create voucher.", ChatColor.DARK_AQUA, ChatColor.GREEN));
                if (player.hasPermission("mcMMOTrade.Levels.Admin")) {
                    player.sendMessage(String.format("%s/leveltrade give <name> <skill> <level>%s - Give voucher.", ChatColor.DARK_AQUA, ChatColor.GREEN));
                }
                return false;
            } else if (strings.length > 4) {
                player.sendMessage(String.format("%s[%s%s%smcMMOTrade%s] %s%sSyntax Error! Type /exptrade to view help menu.", ChatColor.DARK_GRAY, ChatColor.RESET,
                        ChatColor.GREEN, ChatColor.BOLD, ChatColor.DARK_GRAY, ChatColor.RESET, ChatColor.RED));
                return false;
            }

            if (strings[0].equals("give")) {

                if (player.hasPermission("mcMMOTrade.Levels.Admin")) {

                    if (!(strings.length == 4)) {
                        player.sendMessage(String.format("%s[%s%s%smcMMOTrade%s] %s%sSyntax Error! Not enough arugements given!", ChatColor.DARK_GRAY, ChatColor.RESET,
                                ChatColor.GREEN, ChatColor.BOLD, ChatColor.DARK_GRAY, ChatColor.RESET, ChatColor.RED));
                        return false;
                    }

                    String name = strings[1];
                    String skill = strings[2].toUpperCase();
                    Integer amount;

                    if (Bukkit.getServer().getPlayer(name) == null) {
                        player.sendMessage(String.format("%s[%s%s%smcMMOTrade%s] %s%sError - Player is currently not online!", ChatColor.DARK_GRAY, ChatColor.RESET,
                                ChatColor.GREEN, ChatColor.BOLD, ChatColor.DARK_GRAY, ChatColor.RESET, ChatColor.RED));
                        return false;
                    }

                    try {
                        amount = Integer.parseInt(strings[3]);
                    } catch (NumberFormatException exception) {
                        player.sendMessage(String.format("%s[%s%s%smcMMOTrade%s] %s%sError - Please only enter integers (whole numbers)!", ChatColor.DARK_GRAY, ChatColor.RESET,
                                ChatColor.GREEN, ChatColor.BOLD, ChatColor.DARK_GRAY, ChatColor.RESET, ChatColor.RED));
                        return false;
                    }

                    if (!(ExperienceAPI.isValidSkillType(skill))) {
                        player.sendMessage(String.format("%s[%s%s%smcMMOTrade%s] %s%sError - You've entered an invalid skill!", ChatColor.DARK_GRAY, ChatColor.RESET,
                                ChatColor.GREEN, ChatColor.BOLD, ChatColor.DARK_GRAY, ChatColor.RESET, ChatColor.RED));
                        return false;
                    }

                    player.sendMessage(String.format("%s[%s%s%smcMMOTrade%s]%s%s %s has received a note for %s level(s) for %s", ChatColor.DARK_GRAY, ChatColor.RESET,
                            ChatColor.GREEN, ChatColor.BOLD, ChatColor.DARK_GRAY, ChatColor.RESET, ChatColor.GRAY, name, amount, skill));

                    giveMcMMOPaper((Player) Bukkit.getServer().getPlayer(name), skill, amount);
                    player.getPlayer().playSound(player.getLocation(), Sound.NOTE_PLING, 1, 1);
                    return true;
                }

                player.sendMessage(String.format("%s[%s%s%smcMMOTrade%s] %s%sError! You do not have permission to use this command!", ChatColor.DARK_GRAY,
                        ChatColor.RESET, ChatColor.GREEN, ChatColor.BOLD, ChatColor.DARK_GRAY, ChatColor.RESET, ChatColor.RED));
                return false;
            }

            if (strings[0].equals("create")) {

                if (player.hasPermission("mcMMOTrade.Levels.User")) {

                    if (!(strings.length == 3)) {
                        player.sendMessage(String.format("%s[%s%s%smcMMOTrade%s] %s%sSyntax Error! Not enough arugements given!", ChatColor.DARK_GRAY, ChatColor.RESET,
                                ChatColor.GREEN, ChatColor.BOLD, ChatColor.DARK_GRAY, ChatColor.RESET, ChatColor.RED));
                        return false;
                    }

                    String skill = strings[1].toUpperCase();
                    Integer withdrawnLevel;
                    Integer currentLevel;

                    try {
                        withdrawnLevel = Integer.parseInt(strings[2]);
                    } catch (NumberFormatException exception) {
                        player.sendMessage(String.format("%s[%s%s%smcMMOTrade%s] %s%sError - Please only enter integers (whole numbers)!",
                                ChatColor.DARK_GRAY, ChatColor.RESET, ChatColor.GREEN, ChatColor.BOLD, ChatColor.DARK_GRAY,
                                ChatColor.RESET, ChatColor.RED));
                        return false;
                    }

                    try {
                        currentLevel = ExperienceAPI.getLevel((Player) commandSender, skill);
                    } catch (InvalidSkillException exception) {
                        player.sendMessage(String.format("%s[%s%s%smcMMOTrade%s] %s%sError - You've entered an invalid skill!",
                                ChatColor.DARK_GRAY, ChatColor.RESET, ChatColor.GREEN, ChatColor.BOLD, ChatColor.DARK_GRAY,
                                ChatColor.RESET, ChatColor.RED));
                        return false;
                    }

                    if (withdrawnLevel > currentLevel) {
                        player.sendMessage(String.format("%s[%s%s%smcMMOTrade%s] %s%sError - Do not withdraw amount greater than current level!",
                                ChatColor.DARK_GRAY, ChatColor.RESET, ChatColor.GREEN, ChatColor.BOLD, ChatColor.DARK_GRAY,
                                ChatColor.RESET, ChatColor.RED));
                        return false;
                    }

                    try {
                        ExperienceAPI.setLevel((Player) commandSender, skill, (currentLevel - withdrawnLevel));
                    } catch (InvalidSkillException exception) {
                        player.sendMessage(String.format("%s[%s%s%smcMMOTrade%s] %s%sError - You've entered an invalid skill!",
                                ChatColor.DARK_GRAY, ChatColor.RESET, ChatColor.GREEN, ChatColor.BOLD, ChatColor.DARK_GRAY,
                                ChatColor.RESET, ChatColor.RED));
                        return false;
                    }

                    player.sendMessage(String.format("%s[%s%s%smcMMOTrade%s] %s%s%s level(s) has been withdrawn from %s",
                            ChatColor.DARK_GRAY, ChatColor.RESET, ChatColor.GREEN, ChatColor.BOLD, ChatColor.DARK_GRAY,
                            ChatColor.RESET, ChatColor.GRAY, withdrawnLevel, skill));

                    giveMcMMOPaper((Player) commandSender, skill, withdrawnLevel);
                    player.getPlayer().playSound(player.getLocation(), Sound.NOTE_PLING, 1, 1);
                    return true;
                }
                player.sendMessage(String.format("%s[%s%s%smcMMOTrade%s] %s%sError! You do not have permission to use this command!", ChatColor.DARK_GRAY,
                        ChatColor.RESET, ChatColor.GREEN, ChatColor.BOLD, ChatColor.DARK_GRAY, ChatColor.RESET, ChatColor.RED));
                return false;
            }
            if (strings[0].equals("info")) {
                player.sendMessage("");
                player.sendMessage(String.format("%s---[]%sLEVELTRADE INFO%s%s[]---", ChatColor.RED, ChatColor.GREEN, ChatColor.RESET, ChatColor.RED));
                player.sendMessage(String.format("%s'LevelTrade' allows players to convert their mcMMO levels into a tradeable form, " +
                                "in this case paper! Simply type the command: '%s/leveltrade create <skill> <levels>%s' and the total levels " +
                                "entered will be withdrawn from your selected skill. You'll receive a paper, which can be given to anyone. " +
                                "Once a player receives the paper, they simply need to right click it to receive the level(s).",
                        ChatColor.DARK_AQUA, ChatColor.GREEN, ChatColor.DARK_AQUA));
                return true;
            }
            return false;
        }

        if (Objects.equals(Main.levels, "FALSE")) {
            if (player.isOp()) {
                player.sendMessage(String.format("%s[%s%s%smcMMOTrade%s] %s%sLevel trading has been disabled in the config!",
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
        mcmmopapermeta.setDisplayName(String.format("%s%sMCMMO Level Voucher %s%s(Right Click)", ChatColor.YELLOW, ChatColor.BOLD,
                ChatColor.RESET, ChatColor.GRAY));
        List<String> mcmmopaperlore = new ArrayList<String>();
        mcmmopaperlore.add("");
        mcmmopaperlore.add(ChatColor.GREEN + "Skill: " + ChatColor.GRAY + skill);
        mcmmopaperlore.add(ChatColor.GREEN + "Level(s): " + ChatColor.GRAY + amount);
        mcmmopaperlore.add("");
        mcmmopapermeta.setLore(mcmmopaperlore);
        mcmmopaper.setItemMeta(mcmmopapermeta);

        player.getInventory().addItem(mcmmopaper);

    }
}
