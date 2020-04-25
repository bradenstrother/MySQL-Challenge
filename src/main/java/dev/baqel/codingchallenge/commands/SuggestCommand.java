package dev.baqel.codingchallenge.commands;

import dev.baqel.codingchallenge.CodingChallenge;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.*;
import java.util.Arrays;

public class SuggestCommand implements CommandExecutor {
    public CodingChallenge plugin;
//    ResultSet rs;
//    Connection con;
//    Statement stmt;

    public SuggestCommand(CodingChallenge plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (label.equalsIgnoreCase("suggest") && player.hasPermission("challenge.suggest")) {
                if (args.length == 0) {
                    player.sendMessage(ChatColor.RED + "Invalid Arguments: /suggest (message)");
                    return true;
                } else {


                    StringBuilder sb = new StringBuilder();
                    for (String arg : args) { sb.append(arg).append(" "); }
                    String[] temp = sb.toString().split(" ");
                    String[] temp2 = Arrays.copyOfRange(temp, 0, temp.length);
                    sb.delete(0, sb.length());
                    for (String details : temp2) {
                        sb.append(details);
                        sb.append(" ");
                    }
                        String details = sb.toString();
                    try {
                        this.con.setAutoCommit(false);
                        this.con = this.plugin.mysql.getConnection();
                        this.stmt = this.con.createStatement();
                        PreparedStatement statement = this.con.prepareStatement("INSERT INTO player_info(ID, UUID, MESSAGE, TIMESTAMP) VALUES (DEFAULT, ?, ?, DEFAULT");
                        statement.setString(1, player.getUniqueId().toString());
                        statement.setString(2, details);
                        statement.executeUpdate();
                        statement.close();

                        player.sendMessage(ChatColor.GREEN + "Submitted!");
                        if (player.hasPermission("test.alert")) {
                            for (Player staff : Bukkit.getOnlinePlayers()) {
                                staff.sendMessage(ChatColor.GRAY + "[!] " + ChatColor.GREEN + player.getName() + ChatColor.GRAY + " has submitted a new suggestion.");
                            }
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    try {
                        this.con.setAutoCommit(true);
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }
                return true;
            }
        } else {
            sender.sendMessage(ChatColor.RED + "Only players may execute this command!");
            return true;
        }
        return false;
    }
}
