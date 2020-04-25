package dev.baqel.codingchallenge;

import dev.baqel.codingchallenge.commands.SuggestCommand;
import dev.baqel.codingchallenge.commands.ViewCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.*;
import java.util.logging.Logger;

public final class CodingChallenge extends JavaPlugin {
    public Logger log = Logger.getLogger("Minecraft");
    public MySQLConnection mysql;
    public Connection con = null;
    public Statement stmt;
//    private String dbName;
//    private String dbms;
//
//    public CodingChallenge(Connection connArg, String dbNameArg, String dbmsArg) {
//        super();
//        this.connection = connArg;
//        this.dbName = dbNameArg;
//        this.dbms = dbmsArg;
//    }

    @Override
    public void onEnable() {
        getCommand("view").setExecutor(new ViewCommand(this));
        getCommand("suggest").setExecutor(new SuggestCommand(this));

        String hostname = "localhost";
        String port = "3306";
        String database = "challenge_database";
        String user = "root";
        String password = "";
        MySQLConnection mysql = new MySQLConnection(hostname, port, database, user, password);
        try {
            mysql.open();
            log.info("[CodingChallenge] Connected to MySQL Database");
        } catch (Exception e) {
            log.info(e.getMessage());
        }
    }

    public void onDisable() {
    }

//    public void openConnection() {
//        String host = "localhost";
//        int port = 3306;
//        String database = "challenge_database";
//        String username = "root";
//        String password = "";
//
//        try {
//            synchronized (this) {
//                if (getConnection() != null && !this.con.().isClosed()) {
//                    return;
//                }
//                Class.forName("com.mysql.jdbc.Driver");
//                setConnection(DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password));
//            }
//        } catch (SQLException | ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//    }
//    public static PreparedStatement preparedStatement(String query) {
//        PreparedStatement ps = null;
//        try {
//            ps = con.prepareStatement(query);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return ps;
//    }

//    public Connection getConnection() throws SQLException {
//        return con;
//    }


//    public void setConnection(Connection connection) {
//        this.con = connection;
//    }

}
