
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Exam Result</title>
    <link rel="stylesheet" href="css/style.css">

    <style>
        .result-card {
            max-width: 650px;
            margin: 30px auto;
            padding: 25px;
            background: #ffffff;
            border-radius: 12px;
            box-shadow: 0 4px 15px rgba(0, 0, 0, 0.10);
        }

        .result-header {
            text-align: center;
            margin-bottom: 25px;
        }

        .result-grid {
            display: grid;
            grid-template-columns: repeat(2, minmax(0, 1fr));
            gap: 15px;
        }

        .result-item {
            padding: 18px;
            background: #f3f6fa;
            border-radius: 8px;
        }

        .result-item p {
            margin: 0 0 8px;
            color: #555;
        }

        .result-item h3 {
            margin: 0;
            color: #172554;
            overflow-wrap: anywhere;
        }

        .status {
            text-align: center;
            margin: 25px 0;
            font-size: 22px;
            font-weight: bold;
        }

        .pass {
            color: #15803d;
        }

        .fail {
            color: #dc2626;
        }

        .result-actions {
            text-align: center;
            margin-top: 25px;
        }

        .result-actions a {
            display: inline-block;
            padding: 12px 22px;
            margin: 5px;
            border-radius: 6px;
            background: #2563eb;
            color: white;
            text-decoration: none;
        }

        @media (max-width: 480px) {
            .result-grid {
                grid-template-columns: 1fr;
            }
        }
    </style>
</head>
<body>

<div class="container">
    <div class="result-card">

        <div class="result-header">
            <h1>Exam Result</h1>
            <p>Online Examination System</p>
        </div>

        <% if (request.getAttribute("examResult") == null) { %>

            <h3>Result not available</h3>
            <p>Please submit the examination to view your result.</p>

        <% } else { %>

            <h2>
                Student:
                <%= ((com.exam.model.ExamResult)
                    request.getAttribute("examResult"))
                    .getStudentName() %>
            </h2>

            <div class="result-grid">

                <div class="result-item">
                    <p>Correct Answers / Score</p>
                    <h3>
                        <%= ((com.exam.model.ExamResult)
                            request.getAttribute("examResult"))
                            .getScore() %>
                        /
                        <%= ((com.exam.model.ExamResult)
                            request.getAttribute("examResult"))
                            .getTotalQuestions() %>
                    </h3>
                </div>

                <div class="result-item">
                    <p>Wrong Answers</p>
                    <h3>
                        <%= ((com.exam.model.ExamResult)
                            request.getAttribute("examResult"))
                            .getWrongAnswers() %>
                    </h3>
                </div>

                <div class="result-item">
                    <p>Percentage</p>
                    <h3>
                        <%= String.format("%.2f",
                            ((com.exam.model.ExamResult)
                            request.getAttribute("examResult"))
                            .getPercentage()) %>%
                    </h3>
                </div>

                <div class="result-item">
                    <p>Grade</p>
                    <h3>
                        <%= ((com.exam.model.ExamResult)
                            request.getAttribute("examResult"))
                            .getGrade() %>
                    </h3>
                </div>

            </div>

            <div class="status
                <%= "PASS".equalsIgnoreCase(
                    ((com.exam.model.ExamResult)
                    request.getAttribute("examResult"))
                    .getResult()) ? "pass" : "fail" %>">

                <%= ((com.exam.model.ExamResult)
                    request.getAttribute("examResult"))
                    .getResult() %>

            </div>

            <div class="result-actions">
                <a href="exam">Take Another Exam</a>
                <a href="./">Home</a>
            </div>

        <% } %>

    </div>
</div>

</body>
</html>
