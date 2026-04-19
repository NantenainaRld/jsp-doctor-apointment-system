<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
<!DOCTYPE html>
<html>
<head>
    <title>Inscription Patient</title>
    <style>
        .error { color: red; }
        .success { color: green; }
    </style>
</head>
<body>
    
    <% if (request.getAttribute("error_message") != null) { %>
        <p class="error"><%= request.getAttribute("error_message") %></p>
    <% } %>
	<form action="${pageContext.request.contextPath}/patient" method="post">
		<input type="hidden" name="action" value="login"> 
		<label>Email:</label>
		<input type="email" name="email_pat" required><br> <label>Mot
			de passe:</label> <input type="password" name="mdp_pat" required><br>

		<button type="submit">S'inscrire</button>
	</form>
</body>
</html>