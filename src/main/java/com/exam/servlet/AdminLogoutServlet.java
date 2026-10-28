package com.exam.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/admin-logout")
public class AdminLogoutServlet extends HttpServlet {

    @Override
protected void doPost(
        HttpServletRequest request,
        HttpServletResponse response)
        throws ServletException, IOException {

    HttpSession session = request.getSession(false);

    if (session == null
            || !Boolean.TRUE.equals(
                    session.getAttribute("adminLoggedIn"))) {
        response.sendError(
                HttpServletResponse.SC_UNAUTHORIZED,
                "Please sign in first.");
        return;
    }

    synchronized (session) {
        String expected =
                (String) session.getAttribute("adminQuestionCsrfToken");
        String submitted = request.getParameter("csrfToken");

        if (expected == null || submitted == null
                || !java.security.MessageDigest.isEqual(
                    expected.getBytes(java.nio.charset.StandardCharsets.UTF_8),
                    submitted.getBytes(java.nio.charset.StandardCharsets.UTF_8))) {
            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN,
                    "Invalid or expired form token. Reload the page.");
            return;
        }
    }

    session.invalidate();
    response.setHeader("Cache-Control", "no-store");
    response.sendRedirect(
            request.getContextPath() + "/admin-login");
}

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        response.setHeader("Allow", "POST");
        response.sendError(
                HttpServletResponse.SC_METHOD_NOT_ALLOWED,
                "Use the logout form to sign out.");
    }
}