<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false"%>
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
    <h2>Inscription Patient</h2>
    
    <% if (request.getAttribute("error_message") != null) { %>
        <p class="error"><%= request.getAttribute("error_message") %></p>
    <% } %>
    
    <form action="${pageContext.request.contextPath}/patient" method="post">
        <input type="hidden" name="action" value="register">
        
        <label>Nom:</label>
        <input type="text" name="nom_pat" required><br>
        
        <label>Prénom:</label>
        <input type="text" name="prenom_pat" ><br>
        
        <label>Date de naissance:</label>
        <input type="date" name="date_nais" required><br>
        
        <label>Email:</label>
        <input type="email" name="email_pat" required><br>
        
        <label>Mot de passe:</label>
        <input type="password" name="mdp_pat" required><br>
        
        <button type="submit">S'inscrire</button>
    </form>
    
    <p>Déjà inscrit ? <a href="${pageContext.request.contextPath}/patient?action=login">Se connecter</a></p>
</body>
</html>