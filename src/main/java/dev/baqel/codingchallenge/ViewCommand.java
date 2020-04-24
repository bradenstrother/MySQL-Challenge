package dev.baqel.codingchallenge;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.*;

public class ViewCommand implements CommandExecutor {

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
                        ResultSet rs = CodingChallenge.preparedStatement("SELECT COUNT(UUID) FROM player_info WHERE UUID = '" + args[0] + "';").executeQuery();
                        rs.next();
                        if (rs.getInt(1) == 0) {
                            player.sendMessage(ChatColor.RED + "Player UUID not found!");
                        } else {
                            player.sendMessage(ChatColor.GRAY + "Returning results for " + ChatColor.GREEN + args[0] + ChatColor.GRAY + ".");
                            ResultSet rs1 = CodingChallenge.preparedStatement("SELECT * FROM player_info WHERE UUID = '" + args[0] + "';").executeQuery();
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
                    } finally {

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

                    PreparedStatement info = null;
                    PreparedStatement count = null;

                    String infoQuery =
                            "SELECT * FROM player_info" +
                                    "WHERE ID = ?;";
                    String countQuery =
                            "SELECT COUNT(ID) FROM player_info WHERE ID = ?;";

                    try {
                        CodingChallenge.connection.setAutoCommit(false);
                        info = CodingChallenge.connection.prepareStatement(infoQuery);
                        count = CodingChallenge.connection.prepareStatement(countQuery);
                        info.setString(1, args[0]);
                        count.setString(1, args[0]);
                        CodingChallenge.connection.commit();
                        ResultSet rsCount = count.executeQuery(countQuery);
                        rsCount.next();
                        if (rsCount.getInt(1) == 0) {
                            player.sendMessage(ChatColor.RED + "Suggestion ID not found in database!");
                        } else {
                            ResultSet rsInfo = info.executeQuery(infoQuery);
                            while (rsInfo.next()) {
                                int id = rsInfo.getInt("ID");
                                String UUID = rsInfo.getString("UUID");
                                String Suggestion = rsInfo.getString("MESSAGE");
                                Timestamp ts = rsInfo.getTimestamp("TIMESTAMP");
                            }
                        }

                        ResultSet rs2 = CodingChallenge.preparedStatement("SELECT COUNT(ID) FROM player_info WHERE ID = '" + args[0] + "';").executeQuery();
                        rs2.next();
                        if (rs2.getInt(1) == 0) {
                            player.sendMessage(ChatColor.RED + "Suggestion ID not found in database!");
                        } else {
                            ResultSet rs3 = CodingChallenge.preparedStatement("SELECT * FROM player_info WHERE ID = '" + args[0] + "';").executeQuery();
                            rs3.next();
                            int id = rs3.getInt("ID");
                            String UUID = rs3.getString("UUID");
                            String Suggestion = rs3.getString("MESSAGE");
                            Timestamp ts = rs3.getTimestamp("TIMESTAMP");
                            player.sendMessage(ChatColor.GRAY + "Suggestion Details");
                            player.sendMessage(ChatColor.GRAY + "ID: #" + ChatColor.GREEN + id);
                            player.sendMessage(ChatColor.GRAY + "User: " + ChatColor.GREEN + Bukkit.getPlayer(java.util.UUID.fromString(UUID)).getDisplayName());
                            player.sendMessage(ChatColor.GRAY + "Message: " + ChatColor.GREEN + Suggestion);
                            player.sendMessage(ChatColor.GRAY + "Submitted @ " + ChatColor.GREEN + ts);
                        }
                        return true;
                    } catch (SQLException e) {
                        e.printStackTrace();
                    } finally {
                        if (info != null) {
                            try {
                                info.close();
                            } catch (SQLException throwables) {
                                throwables.printStackTrace();
                            }
                        }
                        if (count != null) {
                            try {
                                count.close();
                            } catch (SQLException throwables) {
                                throwables.printStackTrace();
                            }
                        }
                        try {
                            CodingChallenge.connection.setAutoCommit(true);
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }
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
