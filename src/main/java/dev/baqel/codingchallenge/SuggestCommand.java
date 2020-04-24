package dev.baqel.codingchallenge;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

public class SuggestCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (label.equalsIgnoreCase("suggest") && player.hasPermission("challenge.suggest")) {
                if (args.length == 0) {
                    player.sendMessage(ChatColor.RED + "Invalid Arguments: /suggest (message)");
                    return true;
                } else {
                    PreparedStatement insertSubmit = null;

                    String insertString =
                            "INSERT INTO player_info(ID, UUID, MESSAGE, TIMESTAMP) " +
                                    "VALUES (DEFAULT, ?, ?, DEFAULT";
                    try {
                        CodingChallenge.connection.setAutoCommit(false);
                        insertSubmit = CodingChallenge.connection.prepareStatement(insertString);

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

                        insertSubmit.setString(1, player.getUniqueId().toString());
                        insertSubmit.setString(2, details);
                        insertSubmit.executeQuery();
                        CodingChallenge.connection.commit();

//                        CodingChallenge.preparedStatement("INSERT INTO player_info(ID, UUID, MESSAGE, TIMESTAMP) VALUES (DEFAULT, '" + player.getUniqueId() + "', '" + details + "', DEFAULT);").executeUpdate();
                        player.sendMessage(ChatColor.GREEN + "Submitted!");
                        if (player.hasPermission("test.alert")) {
                            for (Player staff : Bukkit.getOnlinePlayers()) {
                                staff.sendMessage(ChatColor.GRAY + "[!] " + ChatColor.GREEN + player.getName() + ChatColor.GRAY + " has submitted a new suggestion.");
                            }
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    } finally {
                        if (insertSubmit != null) {
                            try {
                                insertSubmit.close();
                            } catch (SQLException throwables) {
                                throwables.printStackTrace();
                            }
                        }
                    }
                    try {
                        CodingChallenge.connection.setAutoCommit(true);
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
