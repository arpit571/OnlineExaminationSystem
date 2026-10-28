<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    if (Boolean.TRUE.equals(session.getAttribute("adminLoggedIn"))) {
        response.sendRedirect(request.getContextPath() + "/admin-questions");
        return;
    }

    String csrfToken = (String) request.getAttribute("csrfToken");
    String errorMessage = (String) request.getAttribute("errorMessage");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Login | Online Examination System</title>

    <style>
        * { box-sizing: border-box; }

        body {
            margin: 0;
            min-height: 100vh;
            display: grid;
            place-items: center;
            padding: 20px;
            background: #f1f5f9;
            font-family: Arial, sans-serif;
            color: #1e293b;
        }

        .login-card {
            width: 100%;
            max-width: 410px;
            padding: 32px;
            background: white;
            border-radius: 14px;
            box-shadow: 0 8px 28px rgba(15, 23, 42, 0.10);
        }

        h1 { margin-top: 0; font-size: 25px; }
        p { color: #64748b; line-height: 1.5; }

        label {
            display: block;
            margin: 18px 0 7px;
            font-weight: bold;
        }

        input {
            width: 100%;
            padding: 12px;
            border: 1px solid #cbd5e1;
            border-radius: 7px;
            font-size: 15px;
        }

        button {
            width: 100%;
            margin-top: 24px;
            padding: 13px;
            border: none;
            border-radius: 7px;
            background: #1d4ed8;
            color: white;
            font-weight: bold;
            font-size: 15px;
            cursor: pointer;
        }

        button:hover { background: #1e40af; }

        .error {
            padding: 12px;
            margin: 18px 0;
            background: #fee2e2;
            color: #991b1b;
            border-radius: 7px;
        }
    </style>
</head>

<body>
    <main class="login-card">
        <h1>Administrator Login</h1>
        <p>Sign in to manage examination questions.</p>

        <% if (errorMessage != null) { %>
            <div class="error"><%= errorMessage.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;") %></div>
        <% } %>

        <form method="post"
              action="<%= request.getContextPath() %>/admin-login">

            <input type="hidden" name="csrfToken"
                   value="<%= csrfToken == null ? "" :
                       csrfToken.replace("&", "&amp;")
                                .replace("\"", "&quot;")
                                .replace("<", "&lt;")
                                .replace(">", "&gt;") %>">

            <label for="username">Username</label>
            <input type="text" id="username" name="username"
                   autocomplete="username" required maxlength="100">

            <label for="password">Password</label>
            <input type="password" id="password" name="password"
                   autocomplete="current-password" required>

            <button type="submit">Sign In</button>
        </form>
    </main>
</body>
</html>