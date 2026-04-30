<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.doctorapointment.model.Rdv" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%
// Check if RDV exists in request
Rdv rdv = (Rdv) request.getAttribute("rdv");
if (rdv == null) {
response.sendRedirect(request.getContextPath());
return;
}

DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Modifier rendez-vous</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/bootstrap-5.3.3/css/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/update_rdv.css">
</head>
<body>

<div class="container py-5">
    <div class="row justify-content-center">
        <div class="col-md-6">
            <!-- Card -->
            <div class="card border-0 shadow-lg rounded-4 overflow-hidden">
                <div class="card-header bg-primary text-white py-3 border-0 d-flex justify-content-center">
                    <i class="fad fa-calendar-check fa-2x mb-2"></i>
                    <h4>Modification de rendez-vous</h4>
                </div>

                <div class="card-body p-4">

                    <div class="mb-4 text-center">
                        <h5 class="mb-0 fw-semibold">Rendez-vous n°<%= rdv.getIdRdv() %></h5>
                        <small class="opacity-75">du <%= rdv.getDateRdv().format(dateFormatter) %></small>
                    </div>
                    <hr>
                    <!-- Error message -->
                    <%
                    String errorMessage = (String) request.getAttribute("error_message");
                    if (errorMessage != null && !errorMessage.isEmpty()) {
                    %>
                    <div class="alert alert-danger alert-dismissible fade show" role="alert">
                        <i class="fad fa-exclamation-circle me-2"></i>
                        <%= errorMessage %>
                        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                    </div>
                    <%
                    }
                    %>

                    <!-- Update form -->
                    <form method="post" action="${pageContext.request.contextPath}/patient">
                        <input type="hidden" name="action" value="update_rdv">
                        <input type="hidden" name="id" value="<%= rdv.getIdRdv() %>">
                        <input type="hidden" name="dateRdv" value="<%= rdv.getDateRdv().format(dateFormatter) %>">

                        <!-- Start time -->
                        <div class="mb-3">
                            <label class="form-label fw-semibold">
                                <i class="fad fa-clock me-1 text-primary"></i> Heure de début
                            </label>
                            <input type="time" class="form-control form-control-md rounded-3"
                                   name="heureDebut" value="<%= rdv.getHeureDebut().format(timeFormatter) %>" required>
                        </div>

                        <!-- End time -->
                        <div class="mb-4">
                            <label class="form-label fw-semibold">
                                <i class="fad fa-clock me-1 text-primary"></i> Heure de fin
                            </label>
                            <input type="time" class="form-control form-control-md rounded-3"
                                   name="heureFin" value="<%= rdv.getHeureFin().format(timeFormatter) %>" required>
                        </div>

                        <!-- Action buttons -->
                        <div class="d-flex gap-3">
                            <button type="submit" class="btn btn-primary flex-fill py-2 rounded-3 fw-semibold">
                                <i class="fad fa-save me-2"></i> Enregistrer
                            </button>
                            <button type="button" class="btn btn-outline-secondary flex-fill py-2 rounded-3 fw-semibold"
                                    onclick="handleCancelNavigation()">
                                <i class="fad fa-times me-2"></i> Annuler
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

<script src="${pageContext.request.contextPath}/bootstrap-5.3.3/js/bootstrap.bundle.min.js"></script>
<script src="${pageContext.request.contextPath}/fontawesome-pro-7.1.0-web/js/fontawesome.js"></script>
<script src="${pageContext.request.contextPath}/fontawesome-pro-7.1.0-web/js/duotone.js"></script>
<script>
    function handleCancelNavigation() {
    <%
        String userRole = null;
        if (session.getAttribute("patient") != null) {
            userRole = "patient";
        } else if (session.getAttribute("admin") != null) {
            userRole = "admin";
        }
    %>
    var userRole = "<%= userRole %>";

    if (userRole === "patient") {
        window.location.href = "${pageContext.request.contextPath}/patient?action=med";
    } else if (userRole === "admin") {
        window.location.href = "${pageContext.request.contextPath}/admin?action=med";
    } else {
        window.history.back();
    }
}
</script>
</body>
</html>