package net.worldmc.worldmc.database;

import net.worldmc.worldmc.Worldmc;

import java.sql.*;
import java.util.UUID;

public class MySQL {

    private static Connection connection;

    private final String host = Worldmc.getInstance().getConfig().getString("MySQL.Host");
    private final String port = Worldmc.getInstance().getConfig().getString("MySQL.Port");
    private final String database = Worldmc.getInstance().getConfig().getString("MySQL.Database");
    private final String username = Worldmc.getInstance().getConfig().getString("MySQL.Username");
    private final String password = Worldmc.getInstance().getConfig().getString("MySQL.Password");

    public boolean isConnected() {
        return (connection != null);
    }

    public void connect() throws ClassNotFoundException, SQLException {
        if (!isConnected()) {
            connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?useSSL=false&autoReconnect=true", username, password);
        }
    }

    public void disconnect() {
        if (isConnected()) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static Connection getConnection() {
        return connection;
    }

    public void createTables() {
        PreparedStatement preparedStatement;
        try {
            preparedStatement = getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS players (uuid VARCHAR(100), votes INTEGER(100), ghost BOOLEAN, PRIMARY KEY (uuid))");
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void createPlayer(UUID uuid) {
        try {
            if (!findPlayer(uuid)) {
                PreparedStatement preparedStatement = getConnection().prepareStatement("INSERT INTO players (uuid) VALUES (?)");
                preparedStatement.setString(1, uuid.toString());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean findPlayer(UUID uuid) {
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement("SELECT * FROM players WHERE uuid=?");
            preparedStatement.setString(1, uuid.toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void setGhost(UUID uuid, boolean enabled) {
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement("UPDATE players SET ghost=? WHERE uuid=?");
            preparedStatement.setBoolean(1, enabled);
            preparedStatement.setString(2, uuid.toString());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean getGhost(UUID uuid) {
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement("SELECT ghost FROM players WHERE uuid=?");
            preparedStatement.setString(1, uuid.toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getBoolean("ghost");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void addVote(UUID uuid) {
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement("UPDATE players SET votes=? WHERE uuid=?");
            preparedStatement.setInt(1, getVotes(uuid) + 1);
            preparedStatement.setString(2, uuid.toString());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static int getVotes(UUID uuid) {
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement("SELECT votes FROM players WHERE uuid=?");
            preparedStatement.setString(1, uuid.toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("votes");
            }
            return 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}