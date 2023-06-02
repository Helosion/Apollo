package com.lunarclient.apollo.example.commands;

import com.lunarclient.apollo.example.ApolloExamplePlugin;
import com.lunarclient.apollo.example.modules.TeamExample;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TeamCommand implements CommandExecutor {

    private final TeamExample teamExample = ApolloExamplePlugin.getPlugin().getTeamExample();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Player only!");
            return true;
        }

        if (args.length == 1) {
            switch (args[0].toLowerCase()) {
                case "create" -> {
                    var team = this.teamExample.createTeam();
                    team.addMember(player);

                    player.sendMessage("Creating team...");
                }

                case "delete" ->
                    this.teamExample.getByPlayerUuid(player.getUniqueId()).ifPresentOrElse(team -> {
                        this.teamExample.deleteTeam(team.getTeamId());
                        player.sendMessage("Deleting team...");
                    }, () -> player.sendMessage("No team found..."));

                default -> {
                    player.sendMessage("Usage: /team <create|delete>");
                    player.sendMessage("Usage: /team <addMember|removeMember> <player>");
                }
            }

            return true;
        }

        if (args.length == 2) {
            var target = Bukkit.getPlayer(args[1]);

            if(target == null) {
                player.sendMessage("Player '" + args[1] + "' not found!");
                return true;
            }

            switch (args[0].toLowerCase()) {
                case "addmember" ->
                    this.teamExample.getByPlayerUuid(player.getUniqueId()).ifPresentOrElse(team -> {
                        team.addMember(target);
                        player.sendMessage("Added " + target.getName() + " to your team...");
                    }, () -> player.sendMessage("Team not found!"));

                case "removemember" ->
                    this.teamExample.getByPlayerUuid(player.getUniqueId()).ifPresentOrElse(team -> {
                        team.removeMember(target);
                        player.sendMessage("Removed " + target.getName() + " from your team...");
                    }, () -> player.sendMessage("Team not found!"));

                default -> {
                    player.sendMessage("Usage: /team <create|delete>");
                    player.sendMessage("Usage: /team <addMember|removeMember> <player>");
                }
            }

            return true;
        }

        player.sendMessage("Usage: /team <create|delete>");
        player.sendMessage("Usage: /team <addMember|removeMember> <player>");

        return true;
    }
}
