package dev.baqel.codingchallenge.database;

import dev.baqel.codingchallenge.CodingChallenge;

import java.sql.*;

public class MySQLConnection extends Database {
    private final String hostname;
    private final String port;
    private final String username;
    private final String password;
    private final String database;
    private Connection connection;
    private CodingChallenge plugin;

    public MySQLConnection(String hostname, String port, String database, String username, String password) {
        this.hostname = hostname;
        this.port = port;
        this.database = database;
        this.username = username;
        this.password = password;
    }

    @Override
    public Connection openConnection() throws SQLException, ClassNotFoundException {
        if (checkConnection()) {
            return connection;
        }

        String connectionURL = "jdbc:mysql://"
                + this.hostname + ":" + this.port;
        if (database != null) {
            connectionURL = connectionURL + "/" + this.database;
        }

        Class.forName("com.mysql.jdbc.Driver");
        connection = DriverManager.getConnection(connectionURL,
                this.username, this.password);
        return  connection;
    }

    public boolean checkConnection() {
        try {
            return connection != null && connection.isValid(2);
        } catch (SQLException e) {
            return false;
        }
    }

    public void close() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the connection with the database
     *
     * @return Connection with the database, re-opens if none
     */
    @Override
    public Connection getConnection() {
        if (!checkConnection()) {
            try {
                close();
                openConnection();
            } catch (Exception e) {
                plugin.log.info("[CodingChallenge] Error: " + e);
            }
        }
        return connection;
    }
}