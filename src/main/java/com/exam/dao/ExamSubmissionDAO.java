
package com.exam.dao;

import com.exam.model.ExamResult;
import com.exam.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ExamSubmissionDAO {

    /**
     * Saves the student and exam result in one transaction.
     * Both records are committed together or rolled back.
     */
    public void saveSubmission(
            String studentName,
            ExamResult examResult) throws SQLException {

        String studentSql =
                "INSERT INTO students (name) VALUES (?)";

        String resultSql =
                "INSERT INTO results " +
                "(student_id, score, total_questions, percentage, grade, result_status) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = DBConnection.getConnection()) {

            connection.setAutoCommit(false);

            try {
                int studentId;

                // 1. Insert the student
                try (PreparedStatement studentStatement =
                             connection.prepareStatement(
                                     studentSql,
                                     Statement.RETURN_GENERATED_KEYS)) {

                    studentStatement.setString(1, studentName.trim());

                    int rows = studentStatement.executeUpdate();

                    if (rows != 1) {
                        throw new SQLException(
                                "Unable to save student record.");
                    }

                    try (ResultSet keys =
                                 studentStatement.getGeneratedKeys()) {

                        if (!keys.next()) {
                            throw new SQLException(
                                    "Student ID was not generated.");
                        }

                        studentId = keys.getInt(1);
                    }
                }

                // 2. Insert the exam result
                try (PreparedStatement resultStatement =
                             connection.prepareStatement(resultSql)) {

                    resultStatement.setInt(1, studentId);
                    resultStatement.setInt(2, examResult.getScore());
                    resultStatement.setInt(
                            3, examResult.getTotalQuestions());
                    resultStatement.setDouble(
                            4, examResult.getPercentage());
                    resultStatement.setString(
                            5, examResult.getGrade());
                    resultStatement.setString(
                            6, examResult.getResult());

                    int rows = resultStatement.executeUpdate();

                    if (rows != 1) {
                        throw new SQLException(
                                "Unable to save exam result.");
                    }
                }

                // 3. Commit both records together
                connection.commit();

            } catch (SQLException e) {

                // Roll back both inserts if either one fails
                try {
                    connection.rollback();
                } catch (SQLException rollbackError) {
                    e.addSuppressed(rollbackError);
                }

                throw e;
            }
        }
    }
}
