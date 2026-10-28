
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.sql.Timestamp" %>
<%@ page import="java.text.SimpleDateFormat" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>Exam History</title>
    <link rel="stylesheet" href="css/style.css">

    <style>
        .history-container {
            max-width: 1150px;
            margin: 30px auto;
            padding: 20px;
            background: #fff;
            border-radius: 12px;
            box-shadow: 0 4px 15px rgba(0, 0, 0, 0.08);
        }

        .history-table-wrapper {
            overflow-x: auto;
            margin-top: 20px;
        }

        .history-table {
            width: 100%;
            border-collapse: collapse;
            white-space: nowrap;
        }

        .history-table th,
        .history-table td {
            padding: 12px 14px;
            border-bottom: 1px solid #e5e7eb;
            text-align: left;
        }

        .history-table th {
            background: #f3f6fa;
        }

        .status-pass {
            color: #15803d;
            font-weight: bold;
        }

        .status-fail {
            color: #dc2626;
            font-weight: bold;
        }

        .history-actions {
            margin-top: 24px;
        }

        .history-actions a {
            display: inline-block;
            padding: 10px 16px;
            margin-right: 8px;
            margin-bottom: 8px;
            border-radius: 6px;
            background: #2563eb;
            color: white;
            text-decoration: none;
        }

        .empty-message {
            padding: 25px 0;
            color: #555;
        }
    </style>
</head>

<body>
<div class="container">
    <div class="history-container">

        <h1>Exam History</h1>
        <p>View previous examination attempts.</p>

        <%
            List<Map<String, Object>> history =
                (List<Map<String, Object>>)
                    request.getAttribute("examHistory");

            SimpleDateFormat dateFormat =
                new SimpleDateFormat("dd MMM yyyy, hh:mm a");
        %>

        <% if (history == null || history.isEmpty()) { %>

            <p class="empty-message">
                No exam attempts found. Submit an exam to see its history here.
            </p>

        <% } else { %>

            <div class="history-table-wrapper">
                <table class="history-table">
                    <thead>
                    <tr>
                        <th>ID</th>
                        <th>Student Name</th>
                        <th>Score</th>
                        <th>Percentage</th>
                        <th>Grade</th>
                        <th>Result</th>
                        <th>Exam Date</th>
                    </tr>
                    </thead>

                    <tbody>
                    <% for (Map<String, Object> record : history) { %>

                        <tr>
                            <td><%= record.get("resultId") %></td>

                            <td><%= record.get("studentName") %></td>

                            <td>
                                <%= record.get("score") %> /
                                <%= record.get("totalQuestions") %>
                            </td>

                            <td>
                                <%= String.format(
                                    "%.2f",
                                    ((Number) record.get("percentage"))
                                        .doubleValue()) %>%
                            </td>

                            <td><%= record.get("grade") %></td>

                            <td class="<%= "PASS".equalsIgnoreCase(
                                    String.valueOf(record.get("status")))
                                    ? "status-pass" : "status-fail" %>">
                                <%= record.get("status") %>
                            </td>

                            <td>
                                <%
                                    Timestamp examDate =
                                        (Timestamp) record.get("examDate");

                                    if (examDate != null) {
                                        out.print(dateFormat.format(examDate));
                                    } else {
                                        out.print("N/A");
                                    }
                                %>
                            </td>
                        </tr>

                    <% } %>
                    </tbody>
                </table>
            </div>

        <% } %>

        <div class="history-actions">
            <a href="exam">Take an Exam</a>
            <a href="./">Home</a>
        </div>

    </div>
</div>
</body>
</html>
