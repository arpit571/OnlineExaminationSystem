
package com.exam.servlet;

import com.exam.dao.AdminQuestionDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.UUID;

@WebServlet("/admin-questions")
public class AdminQuestionServlet extends HttpServlet {

    private final AdminQuestionDAO questionDAO =
            new AdminQuestionDAO();

    // Display the admin question-management page
    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (!isAdmin(session)) {
            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN,
                    "Administrator login is required.");
            return;
        }

        try {
            String csrfToken = UUID.randomUUID().toString();
            session.setAttribute("adminQuestionCsrfToken", csrfToken);

            request.setAttribute("csrfToken", csrfToken);
            request.setAttribute(
                    "questions", questionDAO.getAllQuestions());

            request.getRequestDispatcher("/admin-questions.jsp")
                    .forward(request, response);

        } catch (SQLException e) {
            throw new ServletException(
                    "Unable to load admin questions.", e);
        }
    }

    // Add, update or delete a question
    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);

        if (!isAdmin(session)) {
            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN,
                    "Administrator login is required.");
            return;
        }

        synchronized (session) {
            String expectedToken =
                    (String) session.getAttribute(
                            "adminQuestionCsrfToken");

            String submittedToken =
                    request.getParameter("csrfToken");

            if (expectedToken == null
                    || submittedToken == null
                    || !expectedToken.equals(submittedToken)) {

                response.sendError(
                        HttpServletResponse.SC_FORBIDDEN,
                        "Invalid or expired form token. Reload the page.");
                return;
            }

            // Consume the token to prevent reuse of this form submission
            session.removeAttribute("adminQuestionCsrfToken");
        }

        String action = request.getParameter("action");

        try {
            if ("add".equals(action)) {

                questionDAO.addQuestion(
                        request.getParameter("questionText"),
                        request.getParameter("optionA"),
                        request.getParameter("optionB"),
                        request.getParameter("optionC"),
                        request.getParameter("optionD"),
                        request.getParameter("correctAnswer"));

                session.setAttribute(
                        "adminQuestionMessage",
                        "Question added successfully.");

            } else if ("update".equals(action)) {

                int id = parseId(request.getParameter("id"));

                boolean updated = questionDAO.updateQuestion(
                        id,
                        request.getParameter("questionText"),
                        request.getParameter("optionA"),
                        request.getParameter("optionB"),
                        request.getParameter("optionC"),
                        request.getParameter("optionD"),
                        request.getParameter("correctAnswer"));

                if (!updated) {
                    response.sendError(
                            HttpServletResponse.SC_NOT_FOUND,
                            "Question not found.");
                    return;
                }

                session.setAttribute(
                        "adminQuestionMessage",
                        "Question updated successfully.");

            } else if ("delete".equals(action)) {

                int id = parseId(request.getParameter("id"));

                boolean deleted = questionDAO.deleteQuestion(id);

                if (!deleted) {
                    response.sendError(
                            HttpServletResponse.SC_NOT_FOUND,
                            "Question not found.");
                    return;
                }

                session.setAttribute(
                        "adminQuestionMessage",
                        "Question deleted successfully.");

            } else {
                response.sendError(
                        HttpServletResponse.SC_BAD_REQUEST,
                        "Invalid question-management action.");
                return;
            }

            // Post/Redirect/Get pattern
            response.sendRedirect(
                    request.getContextPath() + "/admin-questions");

        } catch (IllegalArgumentException e) {
            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    e.getMessage());

        } catch (SQLException e) {
            throw new ServletException(
                    "Unable to modify the question.", e);
        }
    }

    private boolean isAdmin(HttpSession session) {
        return session != null
                && Boolean.TRUE.equals(
                        session.getAttribute("adminLoggedIn"));
    }

    private int parseId(String value) {
        try {
            int id = Integer.parseInt(value);

            if (id <= 0) {
                throw new IllegalArgumentException(
                        "Question ID must be positive.");
            }

            return id;

        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "Invalid question ID.");
        }
    }
}
