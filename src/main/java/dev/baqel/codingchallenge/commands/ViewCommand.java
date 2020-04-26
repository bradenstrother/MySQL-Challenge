package dev.baqel.codingchallenge.commands;

import dev.baqel.codingchallenge.CodingChallenge;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.*;
import java.util.UUID;

public class ViewCommand implements CommandExecutor {
    private final CodingChallenge plugin;

    public ViewCommand(CodingChallenge plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (label.equalsIgnoreCase("view") && player.hasPermission("challenge.view")) {
                if (args.length == 0) {
                    player.sendMessage(ChatColor.RED + "Invalid Arguments: (-a, ID, UUID) [page]");
                    return true;
                } else if (args[0].length() == 36) { // UUID
                    try (Connection con = plugin.mysql.getConnection();
                         PreparedStatement stmt = con.prepareStatement("SELECT * FROM player_info WHERE UUID = ?;")) {
                        stmt.setString(1, args[0]);
                        ResultSet rs = stmt.executeQuery();
                        player.sendMessage(ChatColor.GRAY + "Returning results for " + ChatColor.GREEN + args[0] + ChatColor.GRAY + ".");
                        int rows;
                        rs.last();
                        rows = rs.getRow();
                        rs.beforeFirst();
                        for (int i = 0; i <= rows - 1; ++i) {
                            rs.next();
                            int id = rs.getInt("ID");
                            String UUID2 = rs.getString("UUID");
                            player.sendMessage(ChatColor.GRAY + "ID: " + ChatColor.GREEN + id + ChatColor.GRAY + " UUID: " + ChatColor.GREEN + Bukkit.getPlayer(UUID.fromString(UUID2)).getDisplayName());
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    return true;
                }
                    else if (args[0].equalsIgnoreCase("-a")) { // Suggestions List for Page 1+
                        if (args.length == 2) {
                            player.sendMessage(ChatColor.GREEN + "Page: " + args[1]);
                            for (int i = 1; i <= 10 * Integer.parseInt(args[1]); i++) {
                                if (Integer.parseInt(args[1]) != 1) {
                                    try (Connection con = plugin.mysql.getConnection();
                                         PreparedStatement stmt = con.prepareStatement("SELECT COUNT(ID) FROM player_info WHERE ID = ?;");
                                         PreparedStatement stmt2 = con.prepareStatement("SELECT * FROM player_info WHERE ID = ?;")) {
                                        stmt.setInt(1, i);
                                        ResultSet rs = stmt.executeQuery();
                                        rs.next();

                                        if (!(rs.getInt(1) == 0)) {

                                            stmt2.setInt(1, i);
                                            ResultSet rs2 = stmt2.executeQuery();
                                            rs2.next();
                                            int id = rs2.getInt("ID");
                                            String UUID = rs2.getString("UUID");
                                            if (i >= (10 * Integer.parseInt(args[1])) - 9) {
                                                player.sendMessage(ChatColor.GRAY + "ID: " + ChatColor.GREEN + id + ChatColor.GRAY + " User: " + ChatColor.GREEN + Bukkit.getPlayer(java.util.UUID.fromString(UUID)).getDisplayName());
                                            }
                                        }
                                    } catch (SQLException e) {
                                        e.printStackTrace();
                                    }
                                } else { // Suggestions List for Page 1
                                    try (Connection con = plugin.mysql.getConnection();
                                         PreparedStatement stmt = con.prepareStatement("SELECT * FROM player_info WHERE ID = ?;")) {
                                        stmt.setInt(1, i);
                                        ResultSet rs = stmt.executeQuery();
                                        rs.next();

                                        int id = rs.getInt("ID");
                                        String UUID = rs.getString("UUID");
                                        player.sendMessage(ChatColor.GRAY + "ID: " + ChatColor.GREEN + id + ChatColor.GRAY + " User: " + ChatColor.GREEN + Bukkit.getPlayer(java.util.UUID.fromString(UUID)).getDisplayName());
                                        } catch (SQLException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        } else { // Suggestions List no arguments
                            player.sendMessage(ChatColor.GREEN + "Page: 1");
                            for (int i = 1; i <= 10; i++) {
                                try (Connection con = plugin.mysql.getConnection();
                                     PreparedStatement stmt = con.prepareStatement("SELECT * FROM player_info WHERE ID = ?;")) {
                                    stmt.setInt(1, i);
                                    ResultSet rs = stmt.executeQuery();
                                    rs.next();

                                    int id = rs.getInt("ID");
                                    String UUID = rs.getString("UUID");
                                    player.sendMessage(ChatColor.GRAY + "ID: " + ChatColor.GREEN + id + ChatColor.GRAY + " User: " + ChatColor.GREEN + Bukkit.getPlayer(java.util.UUID.fromString(UUID)).getDisplayName());
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        return true;
            }else if (args.length == 1) { // View Suggestion by ID
                    try (Connection con = plugin.mysql.getConnection();
                         PreparedStatement statement = con.prepareStatement("SELECT * FROM player_info WHERE ID = ?;")) {
                        statement.setInt(1, Integer.parseInt(args[0]));
                        ResultSet rs = statement.executeQuery();
                        rs.next();

                        int id = rs.getInt("ID");
                        String UUID = rs.getString("UUID");
                        String Suggestion = rs.getString("MESSAGE");
                        Timestamp ts = rs.getTimestamp("TIMESTAMP");

                        player.sendMessage(ChatColor.GRAY + "Suggestion Details");
                        player.sendMessage(ChatColor.GRAY + "ID: #" + ChatColor.GREEN + id);
                        player.sendMessage(ChatColor.GRAY + "User: " + ChatColor.GREEN + Bukkit.getPlayer(java.util.UUID.fromString(UUID)).getDisplayName());
                        player.sendMessage(ChatColor.GRAY + "Message: " + ChatColor.GREEN + Suggestion);
                        player.sendMessage(ChatColor.GRAY + "Submitted @ " + ChatColor.GREEN + ts);
                        return true;
                    } catch (SQLException e) {
                        plugin.log.info(e.getMessage());
                    }
                }
            }
            return false;
        } else {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Only players may execute this command!");
            return true;
        }
    }
}