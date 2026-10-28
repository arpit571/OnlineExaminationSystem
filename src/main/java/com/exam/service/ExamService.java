
package com.exam.service;

import com.exam.model.ExamResult;

import java.util.Map;

public class ExamService {

    /**
     * Calculates the examination result using answers
     * retrieved from the database.
     */
    public ExamResult calculateResult(
            String studentName,
            Map<Integer, String> submittedAnswers,
            Map<Integer, String> correctAnswers) {

        if (studentName == null || studentName.trim().isEmpty()) {
            throw new IllegalArgumentException(
                "Student name is required."
            );
        }

        if (correctAnswers == null || correctAnswers.isEmpty()) {
            throw new IllegalArgumentException(
                "No examination questions are available."
            );
        }

        int totalQuestions = correctAnswers.size();
        int score = 0;

        if (submittedAnswers != null) {
            for (Map.Entry<Integer, String> entry
                    : correctAnswers.entrySet()) {

                Integer questionId = entry.getKey();
                String correctAnswer = entry.getValue();
                String submittedAnswer =
                    submittedAnswers.get(questionId);

                if (correctAnswer != null
                        && submittedAnswer != null
                        && correctAnswer.trim().equalsIgnoreCase(
                            submittedAnswer.trim())) {
                    score++;
                }
            }
        }

        int wrongAnswers = totalQuestions - score;

        double percentage =
            (score * 100.0) / totalQuestions;

        String grade;
        String result;

        if (percentage >= 90) {
            grade = "A+";
        } else if (percentage >= 80) {
            grade = "A";
        } else if (percentage >= 70) {
            grade = "B";
        } else if (percentage >= 60) {
            grade = "C";
        } else if (percentage >= 40) {
            grade = "D";
        } else {
            grade = "F";
        }

        result = percentage >= 40 ? "PASS" : "FAIL";

        return new ExamResult(
            studentName.trim(),
            score,
            totalQuestions,
            wrongAnswers,
            percentage,
            grade,
            result
        );
    }
}
