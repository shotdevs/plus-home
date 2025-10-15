package com.plushome.mc.homeplugin.storage;

import com.plushome.mc.homeplugin.PlusHomePlugin;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {

    private final PlusHomePlugin plugin;
    private Connection connection;

    public DatabaseManager(PlusHomePlugin plugin) {
        this.plugin = plugin;
    }

    public void connect() {
        File dataFolder = plugin.getDataFolder();
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }

        File dbFile = new File(dataFolder, "homes.db");
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + dbFile.getAbsolutePath());
            plugin.getLogger().info("Successfully connected to the SQLite database.");
            createTables();
        } catch (SQLException | ClassNotFoundException e) {
            plugin.getLogger().severe("Could not connect to the SQLite database!");
            e.printStackTrace();
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

    public boolean isConnected() {
        return connection != null;
    }

    private void createTables() {
        if (!isConnected()) return;
        String sql = "CREATE TABLE IF NOT EXISTS homes (" +
                     "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                     "player_uuid VARCHAR(36) NOT NULL," +
                     "home_name VARCHAR(32) NOT NULL," +
                     "world VARCHAR(64) NOT NULL," +
                     "x DOUBLE NOT NULL," +
                     "y DOUBLE NOT NULL," +
                     "z DOUBLE NOT NULL," +
                     "yaw REAL NOT NULL," +
                     "pitch REAL NOT NULL," +
                     "UNIQUE(player_uuid, home_name)" +
                     ");";
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void saveHome(com.plushome.mc.homeplugin.model.Home home) {
        if (!isConnected()) return;
        String sql = "INSERT OR REPLACE INTO homes(player_uuid, home_name, world, x, y, z, yaw, pitch) VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
        try (java.sql.PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, home.getPlayerUUID().toString());
            pstmt.setString(2, home.getHomeName());
            pstmt.setString(3, home.getLocation().getWorld().getName());
            pstmt.setDouble(4, home.getLocation().getX());
            pstmt.setDouble(5, home.getLocation().getY());
            pstmt.setDouble(6, home.getLocation().getZ());
            pstmt.setFloat(7, home.getLocation().getYaw());
            pstmt.setFloat(8, home.getLocation().getPitch());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public java.util.Optional<com.plushome.mc.homeplugin.model.Home> getHome(java.util.UUID playerUUID, String homeName) {
        if (!isConnected()) return java.util.Optional.empty();
        String sql = "SELECT * FROM homes WHERE player_uuid = ? AND home_name = ?";
        try (java.sql.PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, playerUUID.toString());
            pstmt.setString(2, homeName);
            java.sql.ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                org.bukkit.World world = org.bukkit.Bukkit.getWorld(rs.getString("world"));
                if (world == null) return java.util.Optional.empty();
                org.bukkit.Location location = new org.bukkit.Location(world, rs.getDouble("x"), rs.getDouble("y"), rs.getDouble("z"), rs.getFloat("yaw"), rs.getFloat("pitch"));
                return java.util.Optional.of(new com.plushome.mc.homeplugin.model.Home(playerUUID, homeName, location));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return java.util.Optional.empty();
    }

    public java.util.List<com.plushome.mc.homeplugin.model.Home> getHomes(java.util.UUID playerUUID) {
        java.util.List<com.plushome.mc.homeplugin.model.Home> homes = new java.util.ArrayList<>();
        if (!isConnected()) return homes;
        String sql = "SELECT * FROM homes WHERE player_uuid = ?";
        try (java.sql.PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, playerUUID.toString());
            java.sql.ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                org.bukkit.World world = org.bukkit.Bukkit.getWorld(rs.getString("world"));
                if (world == null) continue;
                String homeName = rs.getString("home_name");
                org.bukkit.Location location = new org.bukkit.Location(world, rs.getDouble("x"), rs.getDouble("y"), rs.getDouble("z"), rs.getFloat("yaw"), rs.getFloat("pitch"));
                homes.add(new com.plushome.mc.homeplugin.model.Home(playerUUID, homeName, location));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return homes;
    }

    public void deleteHome(java.util.UUID playerUUID, String homeName) {
        if (!isConnected()) return;
        String sql = "DELETE FROM homes WHERE player_uuid = ? AND home_name = ?";
        try (java.sql.PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, playerUUID.toString());
            pstmt.setString(2, homeName);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
