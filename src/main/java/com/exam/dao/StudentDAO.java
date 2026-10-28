
package com.exam.dao;

import com.exam.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class StudentDAO {

    /**
     * Saves a student and returns the generated student ID.
     */
    public int saveStudent(String name) throws SQLException {

        String sql = "INSERT INTO students (name) VALUES (?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, name.trim());

            int rowsInserted = statement.executeUpdate();

            if (rowsInserted == 0) {
                throw new SQLException("Unable to save student.");
            }

            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }

            throw new SQLException(
                    "Student was saved, but no student ID was returned.");
        }
    }
}
