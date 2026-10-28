
package com.exam.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL =
            "DB_URL";

    private static final String USER = "DB_USERNAME";

    // Keep your existing MySQL password here
    private static final String PASSWORD = "DB_PASSWPRD";

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException(
                    "MySQL JDBC driver class was not found in the application.",
                    e
            );
        }

        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
