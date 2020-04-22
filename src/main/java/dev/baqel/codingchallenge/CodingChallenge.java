package dev.baqel.codingchallenge;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public final class CodingChallenge extends JavaPlugin {

    private static Connection connection;
    private String host, database, username, password;
    private int port;

    @Override
    public void onEnable() {
        host = "127.0.0.1";
        port = 3306;
        database = "challenge_database";
        username = "root";
        password = "";

        try {
            openConnection();
            Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "MySQL Database: Connected");
        } catch (SQLException s) {
            s.printStackTrace();
        }
        getCommand("view").setExecutor(new ViewCommand());
        getCommand("suggest").setExecutor(new SuggestCommand());

    }

    private void openConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            return;
        }
        connection = DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database, this.username, this.password);
    }

    public static PreparedStatement preparedStatement(String query) {
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ps;
    }
}
