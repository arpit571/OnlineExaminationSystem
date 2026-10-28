
package com.exam.dao;

import com.exam.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AdminQuestionDAO {

    // View all questions, including the correct answer for admin use
    public List<Map<String, Object>> getAllQuestions()
            throws SQLException {

        List<Map<String, Object>> questions = new ArrayList<>();

        String sql =
                "SELECT id, question_text, option_a, option_b, " +
                "option_c, option_d, correct_answer " +
                "FROM questions ORDER BY id";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                Map<String, Object> question =
                        new LinkedHashMap<>();

                question.put("id", rs.getInt("id"));
                question.put("questionText",
                        rs.getString("question_text"));
                question.put("optionA", rs.getString("option_a"));
                question.put("optionB", rs.getString("option_b"));
                question.put("optionC", rs.getString("option_c"));
                question.put("optionD", rs.getString("option_d"));
                question.put("correctAnswer",
                        rs.getString("correct_answer"));

                questions.add(question);
            }
        }

        return questions;
    }

    // Add a new question
    public void addQuestion(
            String questionText,
            String optionA,
            String optionB,
            String optionC,
            String optionD,
            String correctAnswer) throws SQLException {

        validateQuestion(
                questionText, optionA, optionB,
                optionC, optionD, correctAnswer);

        String sql =
                "INSERT INTO questions " +
                "(question_text, option_a, option_b, " +
                "option_c, option_d, correct_answer) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, questionText.trim());
            statement.setString(2, optionA.trim());
            statement.setString(3, optionB.trim());
            statement.setString(4, optionC.trim());
            statement.setString(5, optionD.trim());
            statement.setString(6,
                    correctAnswer.trim().toUpperCase());

            statement.executeUpdate();
        }
    }

    // Update an existing question
    public boolean updateQuestion(
            int id,
            String questionText,
            String optionA,
            String optionB,
            String optionC,
            String optionD,
            String correctAnswer) throws SQLException {

        if (id <= 0) {
            throw new IllegalArgumentException(
                    "Invalid question ID.");
        }

        validateQuestion(
                questionText, optionA, optionB,
                optionC, optionD, correctAnswer);

        String sql =
                "UPDATE questions SET " +
                "question_text = ?, option_a = ?, " +
                "option_b = ?, option_c = ?, option_d = ?, " +
                "correct_answer = ? WHERE id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, questionText.trim());
            statement.setString(2, optionA.trim());
            statement.setString(3, optionB.trim());
            statement.setString(4, optionC.trim());
            statement.setString(5, optionD.trim());
            statement.setString(6,
                    correctAnswer.trim().toUpperCase());
            statement.setInt(7, id);

            return statement.executeUpdate() == 1;
        }
    }

    // Delete a question by ID
    public boolean deleteQuestion(int id) throws SQLException {

        if (id <= 0) {
            throw new IllegalArgumentException(
                    "Invalid question ID.");
        }

        String sql = "DELETE FROM questions WHERE id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            return statement.executeUpdate() == 1;
        }
    }

    // Validate data before inserting or updating
    private void validateQuestion(
            String questionText,
            String optionA,
            String optionB,
            String optionC,
            String optionD,
            String correctAnswer) {

        if (isBlank(questionText)
                || isBlank(optionA)
                || isBlank(optionB)
                || isBlank(optionC)
                || isBlank(optionD)
                || isBlank(correctAnswer)) {

            throw new IllegalArgumentException(
                    "All question fields are required.");
        }

        if (questionText.trim().length() > 500
                || optionA.trim().length() > 255
                || optionB.trim().length() > 255
                || optionC.trim().length() > 255
                || optionD.trim().length() > 255) {

            throw new IllegalArgumentException(
                    "Question or option text exceeds the allowed length.");
        }

        String answer = correctAnswer.trim().toUpperCase();

        if (!answer.matches("[ABCD]")) {
            throw new IllegalArgumentException(
                    "Correct answer must be A, B, C or D.");
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
