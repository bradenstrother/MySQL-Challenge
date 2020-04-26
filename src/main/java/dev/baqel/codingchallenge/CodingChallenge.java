package dev.baqel.codingchallenge;

import dev.baqel.codingchallenge.commands.SuggestCommand;
import dev.baqel.codingchallenge.commands.ViewCommand;
import dev.baqel.codingchallenge.database.MySQLConnection;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class CodingChallenge extends JavaPlugin {
    public Logger log = Logger.getLogger("Minecraft");
    public MySQLConnection mysql;

    @Override
    public void onEnable() {
        getCommand("view").setExecutor(new ViewCommand(this));
        getCommand("suggest").setExecutor(new SuggestCommand(this));

        String hostname = "localhost";
        String port = "3306";
        String database = "challenge_database";
        String user = "root";
        String password = "";
        try {
            mysql = new MySQLConnection(hostname, port, database, user, password);
            mysql.openConnection();
            log.info("[CodingChallenge] Connected to MySQL Database.");
        } catch (Exception e) {
            log.info(e.getMessage());
        }
    }

    public void onDisable() {
        if (mysql.checkConnection()) {
            mysql.checkConnection();
            mysql.close();
            log.info("[CodingChallenge] Disconnected from MySQL Database.");
            log.info("[CodingChallenge] Shutting down.");
        }
    }
}