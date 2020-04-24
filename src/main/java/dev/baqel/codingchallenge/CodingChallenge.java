package dev.baqel.codingchallenge;

import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

public final class CodingChallenge extends JavaPlugin {
    public static Connection connection;
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
        openConnection();

        getCommand("view").setExecutor(new ViewCommand());
        getCommand("suggest").setExecutor(new SuggestCommand());
    }

    public void openConnection() {
        String host = "localhost";
        int port = 3306;
        String database = "challenge_database";
        String username = "root";
        String password = "";

        try {
            synchronized (this) {
                if (getConnection() != null && !getConnection().isClosed()) {
                    return;
                }
                Class.forName("com.mysql.jdbc.Driver");
                setConnection(DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password));
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
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

    public Connection getConnection() throws SQLException {
        return connection;
    }

    public void setConnection(Connection connection) {
        CodingChallenge.connection = connection;
    }

}
