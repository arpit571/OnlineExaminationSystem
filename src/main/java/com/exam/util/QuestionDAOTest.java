package com.exam.util;

import com.exam.dao.QuestionDAO;
import com.exam.model.Question;

import java.sql.SQLException;
import java.util.List;

public class QuestionDAOTest {

    public static void main(String[] args) {

        QuestionDAO dao = new QuestionDAO();

        try {
            List<Question> questions =
                    dao.getAllQuestions();

            System.out.println(
                    "Total questions: " + questions.size()
            );

            for (Question q : questions) {
                System.out.println(
                    q.getId() + ". " +
                    q.getQuestionText()
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
