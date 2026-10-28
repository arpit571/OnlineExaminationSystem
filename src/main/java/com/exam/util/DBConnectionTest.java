package com.exam.util;

import java.sql.Connection;
import java.sql.SQLException;

public class DBConnectionTest {

    public static void main(String[] args) {

        try (Connection connection =
                     DBConnection.getConnection()) {

            System.out.println(
                    "MySQL connected successfully!"
            );

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}