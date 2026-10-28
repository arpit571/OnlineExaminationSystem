package com.exam.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.UUID;

@WebServlet("/admin-login")
public class AdminLoginServlet extends HttpServlet {

    private static final String LOGIN_PAGE = "/admin-login.jsp";

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(true);

        // Already authenticated? Go directly to question management.
        if (Boolean.TRUE.equals(session.getAttribute("adminLoggedIn"))) {
            response.sendRedirect(
                    request.getContextPath() + "/admin-questions");
            return;
        }

        String csrfToken = UUID.randomUUID().toString();
        session.setAttribute("adminLoginCsrfToken", csrfToken);
        request.setAttribute("csrfToken", csrfToken);

        request.getRequestDispatcher(LOGIN_PAGE)
               .forward(request, response);
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(true);

        String expectedToken =
                (String) session.getAttribute("adminLoginCsrfToken");
        String submittedToken = request.getParameter("csrfToken");

        synchronized (session) {
            if (expectedToken == null
                    || submittedToken == null
                    || !MessageDigest.isEqual(
                            expectedToken.getBytes(StandardCharsets.UTF_8),
                            submittedToken.getBytes(StandardCharsets.UTF_8))) {

                response.sendError(
                        HttpServletResponse.SC_FORBIDDEN,
                        "Invalid or expired login form. Reload the page.");
                return;
            }

            session.removeAttribute("adminLoginCsrfToken");
        }

        String configuredUsername = System.getenv("ADMIN_USERNAME");
        String configuredPassword = System.getenv("ADMIN_PASSWORD");

        if (configuredUsername == null || configuredUsername.isEmpty()
                || configuredPassword == null || configuredPassword.isEmpty()) {

            request.setAttribute(
                    "errorMessage",
                    "Admin login is not configured on the server.");
            forwardWithNewToken(request, response, session);
            return;
        }

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        boolean usernameMatches = username != null
                && MessageDigest.isEqual(
                    configuredUsername.getBytes(StandardCharsets.UTF_8),
                    username.getBytes(StandardCharsets.UTF_8));

        boolean passwordMatches = password != null
                && MessageDigest.isEqual(
                    configuredPassword.getBytes(StandardCharsets.UTF_8),
                    password.getBytes(StandardCharsets.UTF_8));

        if (usernameMatches && passwordMatches) {

            // Prevent session fixation after authentication.
            request.changeSessionId();

            session.setAttribute("adminLoggedIn", Boolean.TRUE);

            response.sendRedirect(
                    request.getContextPath() + "/admin-questions");
            return;
        }

        request.setAttribute(
                "errorMessage",
                "Invalid username or password.");

        forwardWithNewToken(request, response, session);
    }

    private void forwardWithNewToken(
            HttpServletRequest request,
            HttpServletResponse response,
            HttpSession session)
            throws ServletException, IOException {

        String csrfToken = UUID.randomUUID().toString();
        session.setAttribute("adminLoginCsrfToken", csrfToken);
        request.setAttribute("csrfToken", csrfToken);

        request.getRequestDispatcher(LOGIN_PAGE)
               .forward(request, response);
    }
}