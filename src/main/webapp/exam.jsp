
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.exam.model.Question" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Online Examination System</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>

<div class="container">
    <h1>Online Examination</h1>
    <p>Read each question carefully and select one answer.</p>

    <%
        List<Question> questions =
            (List<Question>) request.getAttribute("questions");
    %>

    <% if (questions == null || questions.isEmpty()) { %>

        <p>No questions are available. Please try again later.</p>

    <% } else { %>

        <form action="exam" method="post">

            <input
    type="hidden"
    name="submissionToken"
    value="<%= request.getAttribute("submissionToken") %>">

            <div class="student-details">
                <label for="studentName">Student Name:</label>
                <input
                    type="text"
                    id="studentName"
                    name="studentName"
                    maxlength="100"
                    required
                >
            </div>

            <hr>

            <% for (Question q : questions) { %>

                <div class="question">
                    <h3>
                        <%= q.getId() %>.
                        <%= q.getQuestionText() %>
                    </h3>

                    <div class="options">
                        <label>
                            <input
                                type="radio"
                                name="q<%= q.getId() %>"
                                value="A"
                                required
                            >
                            <%= q.getOptionA() %>
                        </label>

                        <label>
                            <input
                                type="radio"
                                name="q<%= q.getId() %>"
                                value="B"
                            >
                            <%= q.getOptionB() %>
                        </label>

                        <label>
                            <input
                                type="radio"
                                name="q<%= q.getId() %>"
                                value="C"
                            >
                            <%= q.getOptionC() %>
                        </label>

                        <label>
                            <input
                                type="radio"
                                name="q<%= q.getId() %>"
                                value="D"
                            >
                            <%= q.getOptionD() %>
                        </label>
                    </div>
                </div>

                <hr>

            <% } %>

            <button type="submit" class="btn">
                Submit Exam
            </button>

        </form>

    <% } %>

</div>

</body>
</html>
