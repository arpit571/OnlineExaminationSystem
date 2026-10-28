
package com.exam.servlet;

import com.exam.dao.ExamHistoryDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/exam-history")
public class ExamHistoryServlet extends HttpServlet {

    private final ExamHistoryDAO historyDAO =
            new ExamHistoryDAO();

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        try {
            request.setAttribute(
                    "examHistory",
                    historyDAO.getAllExamHistory());

            request.getRequestDispatcher("/exam-history.jsp")
                    .forward(request, response);

        } catch (SQLException e) {
            throw new ServletException(
                    "Unable to load examination history.", e);
        }
    }
}
