package com.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class DBConnection {
    
    private static final Map<String, DatabaseConfig> MODULE_CONFIGS = new HashMap<>();
    
    // Default connection (backward compatibility) → Module A
    public static Connection getConnection() throws ClassNotFoundException, SQLException {
        return getConnection("A");
    }
    
    static {
        // Module A: complaints, facilities, bookings
        MODULE_CONFIGS.put("A", new DatabaseConfig(
            "com.mysql.cj.jdbc.Driver",                         // ← Updated driver
            "jdbc:mysql://localhost:3306/smartcampus_complaint?useSSL=false&serverTimezone=UTC",
            "root",
            "" /*
                "admin"
                */
        ));
        
        // Module B: users
        MODULE_CONFIGS.put("B", new DatabaseConfig(
            "com.mysql.cj.jdbc.Driver",                         // ← Updated driver
            "jdbc:mysql://localhost:3306/smartcampus_users?useSSL=false&serverTimezone=UTC",
            "root",
            "" /*
                "admin"
                */
        ));
    }
    
    private static class DatabaseConfig {
        private final String driver;
        private final String url;
        private final String user;
        private final String password;
        
        public DatabaseConfig(String driver, String url, String user, String password) {
            this.driver = driver;
            this.url = url;
            this.user = user;
            this.password = password;
        }
    }
    
    public static Connection getConnection(String moduleName) throws ClassNotFoundException, SQLException {
        DatabaseConfig config = MODULE_CONFIGS.get(moduleName.toUpperCase());
        if (config == null) {
            throw new IllegalArgumentException("Unknown module: " + moduleName);
        }
        Class.forName(config.driver);
        return DriverManager.getConnection(config.url, config.user, config.password);
    }
    
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    public static void registerModule(String moduleName, String driver, String url, 
                                     String user, String password) {
        MODULE_CONFIGS.put(moduleName.toUpperCase(), 
            new DatabaseConfig(driver, url, user, password));
    }
    
    public static boolean isModuleRegistered(String moduleName) {
        return MODULE_CONFIGS.containsKey(moduleName.toUpperCase());
    }
    
    public static String[] getRegisteredModules() {
        return MODULE_CONFIGS.keySet().toArray(new String[0]);
    }
}