
package com.exam.servlet;

import com.exam.dao.QuestionDAO;
import com.exam.dao.ExamSubmissionDAO;
import com.exam.model.ExamResult;
import com.exam.model.Question;
import com.exam.service.ExamService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@WebServlet("/exam")
public class ExamServlet extends HttpServlet {

    private final QuestionDAO questionDAO = new QuestionDAO();
    private final ExamSubmissionDAO submissionDAO =
            new ExamSubmissionDAO();
    private final ExamService examService = new ExamService();

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        // Display the saved result after a redirect
        if ("true".equals(request.getParameter("showResult"))) {
            Object savedResult = session.getAttribute("lastExamResult");

            if (savedResult instanceof ExamResult) {
                request.setAttribute("examResult", savedResult);
                request.getRequestDispatcher("/result.jsp")
                        .forward(request, response);
                return;
            }
        }

        try {
            List<Question> questions =
                    questionDAO.getAllQuestions();

            // Create a token for this exam form
            String token = UUID.randomUUID().toString();

            synchronized (session) {
                session.setAttribute("examSubmissionToken", token);
            }

            request.setAttribute("submissionToken", token);
            request.setAttribute("questions", questions);

            request.getRequestDispatcher("/exam.jsp")
                    .forward(request, response);

        } catch (SQLException e) {
            throw new ServletException(
                    "Unable to load exam questions.", e);
        }
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String studentName = request.getParameter("studentName");
        String submittedToken =
                request.getParameter("submissionToken");

        if (studentName == null || studentName.trim().isEmpty()) {
            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Student name is required.");
            return;
        }

        studentName = studentName.trim();

        if (studentName.length() > 100) {
            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Student name must not exceed 100 characters.");
            return;
        }

        HttpSession session = request.getSession(false);

        if (session == null || submittedToken == null) {
            response.sendError(
                    HttpServletResponse.SC_CONFLICT,
                    "Invalid or expired exam submission. Open a new exam.");
            return;
        }

        // Serialize submissions in this browser session
        synchronized (session) {

            String expectedToken =
                    (String) session.getAttribute("examSubmissionToken");

            if (expectedToken == null
                    || !expectedToken.equals(submittedToken)) {

                // A repeated submission should not create another result
                if (session.getAttribute("lastExamResult")
                        instanceof ExamResult) {

                    response.sendRedirect(
                            request.getContextPath()
                                    + "/exam?showResult=true");
                } else {
                    response.sendError(
                            HttpServletResponse.SC_CONFLICT,
                            "This exam was already submitted or has expired.");
                }
                return;
            }

            // Consume the token before processing the submission
            session.removeAttribute("examSubmissionToken");

            try {
                Map<Integer, String> correctAnswers =
                        questionDAO.getCorrectAnswers();

                if (correctAnswers.isEmpty()) {
                    throw new ServletException(
                            "No examination questions are available.");
                }

                Map<Integer, String> submittedAnswers =
                        new HashMap<>();

                for (Integer questionId : correctAnswers.keySet()) {
                    String answer = request.getParameter(
                            "q" + questionId);

                    if (answer != null) {
                        answer = answer.trim().toUpperCase();

                        if (answer.matches("[ABCD]")) {
                            submittedAnswers.put(questionId, answer);
                        }
                    }
                }

                ExamResult examResult =
                        examService.calculateResult(
                                studentName,
                                submittedAnswers,
                                correctAnswers);

                // Student and result are saved in one transaction
                submissionDAO.saveSubmission(
                        studentName,
                        examResult);

                // Keep the result for the redirected GET request
                session.setAttribute("lastExamResult", examResult);

                // Post/Redirect/Get: refreshing will not resubmit the POST
                response.sendRedirect(
                        request.getContextPath()
                                + "/exam?showResult=true");

            } catch (SQLException e) {
                // Permit a retry if the database operation failed
                session.setAttribute(
                        "examSubmissionToken", submittedToken);

                throw new ServletException(
                        "Unable to save the examination submission.", e);
            }
        }
    }
}
