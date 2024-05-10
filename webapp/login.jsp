
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<html>
<head>
    <title>Login Page</title>
    <style>
    .loginstyle{
    border:2px solid black;
    height:300px;
    width:300px;
    spacing:20px;
    border-top-left-radius:5px;
    border-top-right-radius:5px;
    border-bottom-left-radius:5px;
    border-bottom-right-radius:5px;
    }
    
    </style>
</head>
<body>
<center>
<div class="loginstyle">
    <h2>Login</h2>
    <form action="/LoginServlet" id="loginform" method="post">
        <div>
            <label for="username">UserName:</label>
            <input type="text" id="username" name="username"><br><br>
        </div>
        <div>
            <label for="password">Password:</label>
            <input type="password" id="password" name="password"><br><br>
        </div>
        <div>
            <input type="submit" value="Login" >
        </div><br>
        <a href="register.jsp">Sign Up/Register</a>
           <p id="hello" style="color: red;"></p>
   
    </form>
    <% 
        // Display error message if login failed
        String error = request.getParameter("error");
        if (error != null) {
    %>
    <% } %>
   <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script>
        $(document).ready(function () {
            $('#loginform').submit(function (e) {
                e.preventDefault();
                var formData = $(this).serialize();
                $.ajax({
                    type: 'POST',
                    url: 'LoginServlet',
                    data: formData,
                    success: function (response) {
                        if (response.trim() === 'yes') { // Trim the response to remove any extra spaces
                            window.location.href = "index.jsp";
                        } else if (response.trim() === 'no'){
                            $('#hello').text('Invalid username or password').show(); // Display error message from the server
                        }
                    },
                    error: function () {
                        $('#error').text('Internal server error occurred');
                    }
                });
            });
        });
    </script>
</div>
</center>
</body>
</html>