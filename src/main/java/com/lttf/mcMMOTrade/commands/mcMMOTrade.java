package com.lttf.mcMMOTrade.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Nick (lttf) on 29/09/2015.
 */
public class mcMMOTrade implements CommandExecutor{

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Player player = (Player) commandSender;
        player.sendMessage("");
        player.sendMessage(String.format("%s---[]%smcMMOTrade COMMANDS%s%s[]---", ChatColor.RED, ChatColor.GREEN, ChatColor.RESET, ChatColor.RED));
        player.sendMessage(String.format("%s/exptrade %s- View the EXP trading menu.", ChatColor.DARK_AQUA, ChatColor.GREEN));
        player.sendMessage(String.format("%s/leveltrade%s- View the LEVEL trading menu.", ChatColor.DARK_AQUA, ChatColor.GREEN));
        return true;
    }
}
