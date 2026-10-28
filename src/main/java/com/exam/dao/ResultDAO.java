
package com.exam.dao;

import com.exam.model.ExamResult;
import com.exam.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ResultDAO {

    /**
     * Saves an exam result linked to a student.
     */
    public void saveResult(
            int studentId,
            ExamResult examResult) throws SQLException {

        String sql = "INSERT INTO results " +
                "(student_id, score, total_questions, percentage, grade, result_status) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, studentId);
            statement.setInt(2, examResult.getScore());
            statement.setInt(3, examResult.getTotalQuestions());
            statement.setDouble(4, examResult.getPercentage());
            statement.setString(5, examResult.getGrade());
            statement.setString(6, examResult.getResult());

            int rowsInserted = statement.executeUpdate();

            if (rowsInserted == 0) {
                throw new SQLException("Unable to save exam result.");
            }
        }
    }
}
