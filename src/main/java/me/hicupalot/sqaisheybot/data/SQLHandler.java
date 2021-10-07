package me.hicupalot.sqaisheybot.data;

import com.zaxxer.hikari.HikariDataSource;

import java.sql.*;

public class SQLHandler {

    private final HikariDataSource hikariDataSource;

    public SQLHandler(String username, String password, String database, int port) {

        this.hikariDataSource = new HikariDataSource();
        hikariDataSource.setUsername(username);
        hikariDataSource.setPassword(password);
        hikariDataSource.setJdbcUrl("jdbc:mysql://localhost:" + port + "/" + database);
        hikariDataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        setupTables();

    }

    private void setupTables() {
        try {
            Connection connection = hikariDataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS Bans (" +
                            "discord_id varchar(100) PRIMARY KEY," +
                            "banned_at timestamp" +
                            ");"
            );
            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getLatestBan() {
        try {
            Connection connection = hikariDataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT discord_id FROM Bans ORDER BY banned_at ASC LIMIT 1;");
            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.isBeforeFirst()) {
                resultSet.close();
                preparedStatement.close();
                connection.close();
                return null;
            }
            String id = resultSet.getString("discord_id");
            resultSet.close();
            preparedStatement.close();
            connection.close();
            return id;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Boolean isBanned(String id) {
        try {
            Connection connection = hikariDataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT discord_id FROM Bans WHERE discord_id = ?;");
            preparedStatement.setString(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            boolean result = resultSet.isBeforeFirst();
            resultSet.close();
            preparedStatement.close();
            connection.close();
            return result;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void insertBan(String id, Timestamp timestamp) {
        try {
            Connection connection = hikariDataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Bans (discord_id, banned_at) VALUES (?, ?);");
            preparedStatement.setString(1, id);
            preparedStatement.setTimestamp(2, timestamp);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteBan(String id) {
        try {
            Connection connection = hikariDataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM Bans WHERE discord_id = ?;");
            preparedStatement.setString(1, id);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
