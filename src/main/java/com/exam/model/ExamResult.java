package com.exam.model;

public class ExamResult {

    private String studentName;
    private int score;
    private int totalQuestions;
    private int wrongAnswers;
    private double percentage;
    private String grade;
    private String result;

    public ExamResult(String studentName,
                      int score,
                      int totalQuestions,
                      int wrongAnswers,
                      double percentage,
                      String grade,
                      String result) {

        this.studentName = studentName;
        this.score = score;
        this.totalQuestions = totalQuestions;
        this.wrongAnswers = wrongAnswers;
        this.percentage = percentage;
        this.grade = grade;
        this.result = result;
    }

    public String getStudentName() {
        return studentName;
    }

    public int getScore() {
        return score;
    }

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public int getWrongAnswers() {
        return wrongAnswers;
    }

    public double getPercentage() {
        return percentage;
    }

    public String getGrade() {
        return grade;
    }

    public String getResult() {
        return result;
    }
}