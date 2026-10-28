
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List,java.util.Map" %>

<%!
    private String esc(Object value) {
        if (value == null) return "";
        String s = String.valueOf(value);
        return s.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
%>

<%
    Object questionData = request.getAttribute("questions");
    List<Map<String, Object>> questions =
        questionData == null
        ? java.util.Collections.emptyList()
        : (List<Map<String, Object>>) questionData;

    String csrfToken = (String) request.getAttribute("csrfToken");
    String message = (String) session.getAttribute("adminQuestionMessage");

    if (message != null) {
        session.removeAttribute("adminQuestionMessage");
    }
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin - Question Management</title>

    <style>
        * { box-sizing: border-box; }

        body {
            margin: 0;
            font-family: Arial, sans-serif;
            background: #f3f6fb;
            color: #1f2937;
        }

        header {
            background: #172554;
            color: white;
            padding: 22px 6%;
            display: flex;
            justify-content: space-between;
            align-items: center;
            gap: 16px;
            flex-wrap: wrap;
        }

        header h1 { margin: 0; font-size: 24px; }

        .container {
            width: 92%;
            max-width: 1100px;
            margin: 28px auto;
        }

        .panel, .question-card {
            background: white;
            padding: 24px;
            margin-bottom: 22px;
            border-radius: 12px;
            box-shadow: 0 3px 12px rgba(0,0,0,.06);
        }

        h2 { margin-top: 0; }

        .form-grid {
            display: grid;
            grid-template-columns: repeat(2, minmax(0, 1fr));
            gap: 16px;
        }

        .field { margin-bottom: 15px; }

        label {
            display: block;
            margin-bottom: 7px;
            font-weight: bold;
        }

        input, textarea, select {
            width: 100%;
            padding: 11px;
            border: 1px solid #cbd5e1;
            border-radius: 7px;
            font: inherit;
        }

        textarea {
            min-height: 85px;
            resize: vertical;
        }

        button, .link-button {
            display: inline-block;
            padding: 10px 16px;
            border: none;
            border-radius: 7px;
            cursor: pointer;
            font-weight: bold;
            font-size: 14px;
            text-decoration: none;
        }

        .primary { background: #2563eb; color: white; }
        .secondary { background: #475569; color: white; }
        .danger { background: #dc2626; color: white; }

        .message {
            padding: 13px;
            margin-bottom: 20px;
            border-radius: 8px;
            background: #dcfce7;
            color: #166534;
        }

        .question-heading {
            display: flex;
            justify-content: space-between;
            align-items: center;
            gap: 10px;
            flex-wrap: wrap;
        }

        .question-id {
            color: #475569;
            font-size: 13px;
        }

        .actions {
            display: flex;
            gap: 10px;
            margin-top: 15px;
            flex-wrap: wrap;
        }

        .empty {
            padding: 20px;
            background: white;
            border-radius: 8px;
        }

        @media (max-width: 650px) {
            .form-grid { grid-template-columns: 1fr; }
            .panel, .question-card { padding: 16px; }
        }
    </style>
</head>

<body>
<header>
    <h1>Question Management</h1>
    <a class="link-button secondary"
       href="<%= request.getContextPath() %>/exam-history">
       View Exam History
    </a>
    <form method="post"
      action="<%= request.getContextPath() %>/admin-logout">
    <input type="hidden" name="csrfToken"
           value="<%= esc(csrfToken) %>">
    <button class="secondary" type="submit">Logout</button>
</form>
</header>

<main class="container">

    <% if (message != null) { %>
        <div class="message"><%= esc(message) %></div>
    <% } %>

    <section class="panel">
        <h2>Add New Question</h2>
        <p>Add a multiple-choice question to the student examination.</p>

        <form method="post"
              action="<%= request.getContextPath() %>/admin-questions">

            <input type="hidden" name="csrfToken"
                   value="<%= esc(csrfToken) %>">
            <input type="hidden" name="action" value="add">

            <div class="field">
                <label for="questionText">Question</label>
                <textarea id="questionText" name="questionText"
                          maxlength="500" required></textarea>
            </div>

            <div class="form-grid">
                <div class="field">
                    <label for="optionA">Option A</label>
                    <input id="optionA" name="optionA"
                           maxlength="255" required>
                </div>

                <div class="field">
                    <label for="optionB">Option B</label>
                    <input id="optionB" name="optionB"
                           maxlength="255" required>
                </div>

                <div class="field">
                    <label for="optionC">Option C</label>
                    <input id="optionC" name="optionC"
                           maxlength="255" required>
                </div>

                <div class="field">
                    <label for="optionD">Option D</label>
                    <input id="optionD" name="optionD"
                           maxlength="255" required>
                </div>
            </div>

            <div class="field">
                <label for="correctAnswer">Correct Answer</label>
                <select id="correctAnswer" name="correctAnswer" required>
                    <option value="">Select the correct option</option>
                    <option value="A">Option A</option>
                    <option value="B">Option B</option>
                    <option value="C">Option C</option>
                    <option value="D">Option D</option>
                </select>
            </div>

            <button class="primary" type="submit">Add Question</button>
        </form>
    </section>

    <section>
        <h2>Existing Questions (<%= questions.size() %>)</h2>

        <% if (questions.isEmpty()) { %>
            <div class="empty">
                No questions found in the database.
            </div>
        <% } %>

        <% for (Map<String, Object> q : questions) { %>
            <article class="question-card">

                <div class="question-heading">
                    <h3>Question #<%= esc(q.get("id")) %></h3>
                    <span class="question-id">
                        Correct answer: <strong>
                            <%= esc(q.get("correctAnswer")) %>
                        </strong>
                    </span>
                </div>

                <form method="post"
                      action="<%= request.getContextPath() %>/admin-questions">

                    <input type="hidden" name="csrfToken"
                           value="<%= esc(csrfToken) %>">
                    <input type="hidden" name="action" value="update">
                    <input type="hidden" name="id"
                           value="<%= esc(q.get("id")) %>">

                    <div class="field">
                        <label>Question Text</label>
                        <textarea name="questionText"
                                  maxlength="500" required><%= esc(q.get("questionText")) %></textarea>
                    </div>

                    <div class="form-grid">
                        <div class="field">
                            <label>Option A</label>
                            <input name="optionA" maxlength="255" required
                                   value="<%= esc(q.get("optionA")) %>">
                        </div>

                        <div class="field">
                            <label>Option B</label>
                            <input name="optionB" maxlength="255" required
                                   value="<%= esc(q.get("optionB")) %>">
                        </div>

                        <div class="field">
                            <label>Option C</label>
                            <input name="optionC" maxlength="255" required
                                   value="<%= esc(q.get("optionC")) %>">
                        </div>

                        <div class="field">
                            <label>Option D</label>
                            <input name="optionD" maxlength="255" required
                                   value="<%= esc(q.get("optionD")) %>">
                        </div>
                    </div>

                    <div class="field">
                        <label>Correct Answer</label>
                        <select name="correctAnswer" required>
                            <option value="A"
                                <%= "A".equals(q.get("correctAnswer")) ? "selected" : "" %>>
                                Option A
                            </option>
                            <option value="B"
                                <%= "B".equals(q.get("correctAnswer")) ? "selected" : "" %>>
                                Option B
                            </option>
                            <option value="C"
                                <%= "C".equals(q.get("correctAnswer")) ? "selected" : "" %>>
                                Option C
                            </option>
                            <option value="D"
                                <%= "D".equals(q.get("correctAnswer")) ? "selected" : "" %>>
                                Option D
                            </option>
                        </select>
                    </div>

                    <div class="actions">
                        <button class="primary" type="submit">
                            Save Changes
                        </button>
                    </div>
                </form>

                <form method="post"
                      action="<%= request.getContextPath() %>/admin-questions"
                      onsubmit="return confirm('Delete this question? Students may see a different exam after deletion.');">

                    <input type="hidden" name="csrfToken"
                           value="<%= esc(csrfToken) %>">
                    <input type="hidden" name="action" value="delete">
                    <input type="hidden" name="id"
                           value="<%= esc(q.get("id")) %>">

                    <div class="actions">
                        <button class="danger" type="submit">
                            Delete Question
                        </button>
                    </div>
                </form>

            </article>
        <% } %>
    </section>

</main>
</body>
</html>
