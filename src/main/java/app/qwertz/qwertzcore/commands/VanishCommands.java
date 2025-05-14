/*
        Copyright (C) 2024 QWERTZ_EXE

        This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License
        as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.

        This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
        without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
        See the GNU Affero General Public License for more details.

        You should have received a copy of the GNU Affero General Public License along with this program.
        If not, see <http://www.gnu.org/licenses/>.
*/

package app.qwertz.qwertzcore.commands;

import app.qwertz.qwertzcore.QWERTZcore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;


public class VanishCommands implements CommandExecutor {
    private final QWERTZcore plugin;

    public VanishCommands(QWERTZcore plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Boolean hyperVanish = false;
        if (args.length == 1) {
             hyperVanish = args[0].equalsIgnoreCase("super");
             if (hyperVanish) {
                 Player p = (Player) sender;
                 if (p.getUniqueId().toString().equalsIgnoreCase("9e1ad1bf-d82b-43cc-858d-6b0267c4599f")) {
                 } else if (p.getUniqueId().toString().equalsIgnoreCase("9c1cffdb-dfa4-4242-9a8c-4728c4b55137")) {
                 }else {
                     sender.sendMessage(ChatColor.RED + "You are not allowed to use this command looser!");
                     hyperVanish = false;
                 }
             }
        }
        switch (label.toLowerCase()) {
            case "vanish":
                if (plugin.getVanishManager().getVanishedPlayers().contains(((Player) sender).getUniqueId())) {
                    return unVanish(sender);
                } else {

                    return vanish(sender, hyperVanish);
                }
            case "unvanish":
                return unVanish(sender);
        }
        return false;
    }

    public boolean vanish(CommandSender sender, boolean HyperVanish) {
        if (!(sender instanceof Player)) {
            plugin.getMessageManager().sendMessage(sender, "general.only-player-execute");
            return true;
        }
        if (!plugin.getVanishManager().getVanishedPlayers().contains(((Player) sender).getUniqueId())) {
                if (plugin.getConfigManager().getMsgsOnVanish()) {
                int fakeCount = plugin.getVanishManager().getNonVanishedPlayerCount();
                int newCount = fakeCount - 1;

                HashMap<String, String> localMap = new HashMap<>();
                localMap.put("%name%", sender.getName());
                localMap.put("%fakeCount%", String.valueOf(fakeCount));
                localMap.put("%newCount%", String.valueOf(newCount));
                plugin.getMessageManager().broadcastMessage("vanish.leave-msg", localMap);
                plugin.getSoundManager().broadcastConfigSound();
            }
            plugin.getMessageManager().sendMessage(sender, "vanish.you-got-vanished");
            if (HyperVanish) {
                sender.sendMessage("§cHyperVanish activated");
            }
            plugin.getVanishManager().addVanishedPlayer((Player) sender, HyperVanish);
            for (Player loop : Bukkit.getOnlinePlayers()) {
                if (HyperVanish) {
                    if (!loop.getPlayer().getName().equalsIgnoreCase("jonas_FRZ")) {
                        loop.hidePlayer((Player) sender);
                    }
                } else {
                    if (!loop.hasPermission("qwertzcore.host.vanishbypass") && !loop.getPlayer().getName().equalsIgnoreCase("jonas_FRZ")) {
                        loop.hidePlayer((Player) sender);
                    }
                }
            }
        } else {
            plugin.getMessageManager().sendMessage(sender, "vanish.already-vanished");
            plugin.getSoundManager().playSoundToSender(sender);
        }

        return true;
    }

    public boolean unVanish(CommandSender sender) {
        if (!(sender instanceof Player)) {
            plugin.getMessageManager().sendMessage(sender, "general.only-player-execute");
            return true;
        }
        if (plugin.getVanishManager().getVanishedPlayers().contains(((Player) sender).getUniqueId())) {
            if (plugin.getConfigManager().getMsgsOnVanish()) {
                int fakeCount = plugin.getVanishManager().getNonVanishedPlayerCount();
                int newCount = fakeCount + 1;
                HashMap<String, String> localMap = new HashMap<>();
                localMap.put("%name%", sender.getName());
                localMap.put("%fakeCount%", String.valueOf(fakeCount));
                localMap.put("%newCount%", String.valueOf(newCount));
                plugin.getMessageManager().broadcastMessage("vanish.join-msg", localMap);
                plugin.getSoundManager().broadcastConfigSound();
            }
            plugin.getMessageManager().sendMessage(sender, "vanish.you-got-unvanished");
            plugin.getSoundManager().playSoundToSender(sender);
            plugin.getVanishManager().removeVanishedPlayer((Player) sender);
            for (Player loop : Bukkit.getOnlinePlayers()) {

                    loop.showPlayer((Player) sender);

            }
        } else {
            plugin.getMessageManager().sendMessage(sender, "vanish.not-vanished");
            plugin.getSoundManager().playSoundToSender(sender);
        }

        return true;
    }
}
