package Util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static DBConnection dbConnection;
    private static Connection connection;

    private static final String DB_PASSWORD_KEY = "db.password";
    private static final String DB_USER_KEY = "db.username";
    private static final String DB_URL_KEY = "db.url";

    private DBConnection(){
        try {
            connection = DriverManager.getConnection(
                    PropertiesSettingsUtil.get(DB_URL_KEY),
                    PropertiesSettingsUtil.get(DB_USER_KEY),
                    PropertiesSettingsUtil.get(DB_PASSWORD_KEY)
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static synchronized DBConnection getDbConnection(){
        if (dbConnection == null){
            dbConnection = new DBConnection();
        }

        return dbConnection;
    }

    public void closeConnection() {
        try {
            connection.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }
}
