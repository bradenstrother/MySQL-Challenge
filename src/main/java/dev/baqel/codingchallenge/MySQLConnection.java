package dev.baqel.codingchallenge;

import java.sql.*;

public class MySQLConnection extends Database {
    private String hostname;
    private String portnmbr;
    private String username;
    private String password;
    private String database;
    private CodingChallenge plugin;

    public MySQLConnection(String hostname, String portnmbr, String database, String username, String password) {
        this.hostname = hostname;
        this.portnmbr = portnmbr;
        this.database = database;
        this.username = username;
        this.password = password;
    }

    public void open() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://" + hostname + ":" + portnmbr + "/" + database;
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            System.out.print("Could not connect to MySQL server!");
        } catch (ClassNotFoundException e) {
            System.out.print("JDBC Driver not found!");
        }
    }

    public void close() {
        try {
            if (connection != null) {
                connection.close();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        if (!checkConnection()) {
            try {
                close();
                open();
            }
            catch (Exception e) {
                plugin.log.info("[CodingChallenge] Error: " + e);
            }
        }
        return connection;
    }

    public boolean checkConnection() {
        try {
            return connection != null && connection.isValid(2);
        }
        catch (SQLException e) {
            return false;
        }
    }

    public ResultSet query(String query) throws SQLException {
        Statement statement = null;
        ResultSet result = null;
        try {
            statement = connection.createStatement();
            result = statement.executeQuery(query);
            return result;
        }
        catch (SQLException e) {
            if (e.getMessage().equals("Can not issue data manipulation statements with executeQuery().")) {
                try {
                    assert statement != null;
                    statement.executeUpdate(query);
                }
                catch (SQLException ex) {
                    if (e.getMessage().startsWith("You have an error in your SQL syntax;")) {
                        String temp = e.getMessage().split(";")[0].substring(0, 36) + e.getMessage().split(";")[1].substring(91);
                        temp = temp.substring(0, temp.lastIndexOf("'"));
                        throw new SQLException(temp);
                    }
                    ex.printStackTrace();
                }
            } else {
                if (e.getMessage().startsWith("You have an error in your SQL syntax;")) {
                    String temp = e.getMessage().split(";")[0].substring(0, 36) + e.getMessage().split(";")[1].substring(91);
                    temp = temp.substring(0, temp.lastIndexOf("'"));
                    throw new SQLException(temp);
                }
                e.printStackTrace();
            }
            return null;
        }
    }
}
