
package com.exam.dao;

import com.exam.model.Question;
import com.exam.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuestionDAO {

    // Fetch all questions and options for the exam page
    public List<Question> getAllQuestions() throws SQLException {

        List<Question> questions = new ArrayList<>();

        String sql = "SELECT id, question_text, option_a, option_b, " +
                     "option_c, option_d FROM questions ORDER BY id";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {

                Question question = new Question(
                    rs.getInt("id"),
                    rs.getString("question_text"),
                    rs.getString("option_a"),
                    rs.getString("option_b"),
                    rs.getString("option_c"),
                    rs.getString("option_d")
                );

                questions.add(question);
            }
        }

        return questions;
    }

    // Fetch correct answers privately for server-side grading
    public Map<Integer, String> getCorrectAnswers() throws SQLException {

        Map<Integer, String> answers = new HashMap<>();

        String sql = "SELECT id, correct_answer FROM questions";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {

                int questionId = rs.getInt("id");

                String correctAnswer = rs.getString("correct_answer");

                if (correctAnswer != null) {
                    answers.put(
                        questionId,
                        correctAnswer.trim().toUpperCase()
                    );
                }
            }
        }

        return answers;
    }
}
