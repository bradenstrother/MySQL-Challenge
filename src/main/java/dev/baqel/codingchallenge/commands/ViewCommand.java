package dev.baqel.codingchallenge.commands;

import dev.baqel.codingchallenge.CodingChallenge;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.*;

public class ViewCommand implements CommandExecutor {
    public CodingChallenge plugin;
    ResultSet rs;
    Connection con;
    Statement stmt;

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
                    try {
                        ResultSet rs = this.con.preparedStatement("SELECT COUNT(UUID) FROM player_info WHERE UUID = '" + args[0] + "';").executeQuery();
                        rs.next();
                        if (rs.getInt(1) == 0) {
                            player.sendMessage(ChatColor.RED + "Player UUID not found!");
                        } else {
                            player.sendMessage(ChatColor.GRAY + "Returning results for " + ChatColor.GREEN + args[0] + ChatColor.GRAY + ".");
                            ResultSet rs1 = this.con.preparedStatement("SELECT * FROM player_info WHERE UUID = '" + args[0] + "';").executeQuery();
                            int rows = 0;
                            rs1.last();
                            rows = rs1.getRow();
                            rs1.beforeFirst();
                            for (int i = 0; i <= rows - 1; i++) {
                                rs1.next();
                                int id = rs1.getInt("ID");
                                String UUID = rs1.getString("UUID");
                                player.sendMessage(ChatColor.GRAY + "ID: " + ChatColor.GREEN + id + ChatColor.GRAY + " UUID: " + ChatColor.GREEN + Bukkit.getPlayer(java.util.UUID.fromString(UUID)).getDisplayName());
                            }
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    return true;
                }
//                    else if (args[0].equalsIgnoreCase("-a")) { // Suggestions List for Page 2+
//                        if (args.length == 2) {
//                            player.sendMessage(ChatColor.GREEN + "Page: " + args[1]);
//                            for (int i = 1; i <= 10 * Integer.parseInt(args[1]); i++) {
//                                if (Integer.parseInt(args[1]) != 1) {
//                                    ResultSet rs = CodingChallenge.preparedStatement("SELECT COUNT(ID) FROM player_INFO WHERE ID = '" + i + "';").executeQuery();
//                                    rs.next();
//                                    if (!(rs.getInt(1) == 0)) {
//                                        ResultSet result = CodingChallenge.preparedStatement("SELECT * FROM player_info WHERE ID = '" + i + "';").executeQuery();
//                                        result.next();
//                                        int id = result.getInt("ID");
//                                        String UUID = result.getString("UUID");
//                                        if (i >= (10 * Integer.parseInt(args[1])) - 9) {
//                                            player.sendMessage(ChatColor.GRAY + "ID: " + ChatColor.GREEN + id + ChatColor.GRAY + " User: " + ChatColor.GREEN + Bukkit.getPlayer(java.util.UUID.fromString(UUID)).getDisplayName());
//                                        }
//                                    }
//                                } else { // Suggestions List for Page 1
//                                    ResultSet rs = CodingChallenge.preparedStatement("SELECT COUNT(ID) FROM player_INFO WHERE ID = '" + i + "';").executeQuery();
//                                    rs.next();
//                                    if (!(rs.getInt(1) == 0)) {
//                                        ResultSet result = CodingChallenge.preparedStatement("SELECT * FROM player_info WHERE ID = '" + i + "';").executeQuery();
//                                        result.next();
//                                        int id = result.getInt("ID");
//                                        String UUID = result.getString("UUID");
//                                        player.sendMessage(ChatColor.GRAY + "ID: " + ChatColor.GREEN + id + ChatColor.GRAY + " User: " + ChatColor.GREEN + Bukkit.getPlayer(java.util.UUID.fromString(UUID)).getDisplayName());
//                                    }
//                                }
//                            }
//                        } else { // Suggestions List no arguments
//                            player.sendMessage(ChatColor.GREEN + "Page: 1");
//                            for (int i = 1; i <= 10; i++) {
//                                ResultSet rs = CodingChallenge.preparedStatement("SELECT COUNT(ID) FROM player_INFO WHERE ID = '" + i + "';").executeQuery();
//                                rs.next();
//                                if (!(rs.getInt(1) == 0)) {
//                                    ResultSet result = CodingChallenge.preparedStatement("SELECT * FROM player_info WHERE ID = '" + i + "';").executeQuery();
//                                    result.next();
//                                    int id = result.getInt("ID");
//                                    String UUID = result.getString("UUID");
//                                    player.sendMessage(ChatColor.GRAY + "ID: " + ChatColor.GREEN + id + ChatColor.GRAY + " User: " + ChatColor.GREEN + Bukkit.getPlayer(java.util.UUID.fromString(UUID)).getDisplayName());
//                                }
//                            }
//                        }
//                        return true;
                else if (args.length == 1) { // View Suggestion by ID
//                        PreparedStatement statement = this.con.prepareStatement("SELECT * FROM player_info WHERE ID = ?;");
//                        statement.setInt(1, Integer.parseInt(args[0]));
//                        statement.executeUpdate();
                    try {
                        this.con = this.plugin.mysql.getConnection();
                        this.stmt = this.con.createStatement();
                        this.rs = this.stmt.executeQuery("SELECT * FROM player_info WHERE ID = '" + Integer.parseInt(args[0]) + "';");
                        this.rs.next();

                        int id = this.rs.getInt("ID");
                        String UUID = this.rs.getString("UUID");
                        String Suggestion = this.rs.getString("MESSAGE");
                        Timestamp ts = this.rs.getTimestamp("TIMESTAMP");

                        player.sendMessage(ChatColor.GRAY + "Suggestion Details");
                        player.sendMessage(ChatColor.GRAY + "ID: #" + ChatColor.GREEN + id);
                        player.sendMessage(ChatColor.GRAY + "User: " + ChatColor.GREEN + Bukkit.getPlayer(java.util.UUID.fromString(UUID)).getDisplayName());
                        player.sendMessage(ChatColor.GRAY + "Message: " + ChatColor.GREEN + Suggestion);
                        player.sendMessage(ChatColor.GRAY + "Submitted @ " + ChatColor.GREEN + ts);
                        return true;
                    } catch (SQLException e) {
                        this.plugin.log.info(e.getMessage());
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
