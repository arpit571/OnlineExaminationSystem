
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

public class ExamHistoryDAO {

    public List<Map<String, Object>> getAllExamHistory()
            throws SQLException {

        List<Map<String, Object>> history = new ArrayList<>();

        String sql =
                "SELECT r.id AS result_id, " +
                "s.name AS student_name, " +
                "r.score, r.total_questions, r.percentage, " +
                "r.grade, r.result_status, r.exam_date " +
                "FROM results r " +
                "INNER JOIN students s ON r.student_id = s.id " +
                "ORDER BY r.exam_date DESC, r.id DESC";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                Map<String, Object> record = new LinkedHashMap<>();

                record.put("resultId", rs.getInt("result_id"));
                record.put("studentName",
                        rs.getString("student_name"));
                record.put("score", rs.getInt("score"));
                record.put("totalQuestions",
                        rs.getInt("total_questions"));
                record.put("percentage",
                        rs.getDouble("percentage"));
                record.put("grade", rs.getString("grade"));
                record.put("status",
                        rs.getString("result_status"));
                record.put("examDate",
                        rs.getTimestamp("exam_date"));

                history.add(record);
            }
        }

        return history;
    }
}
