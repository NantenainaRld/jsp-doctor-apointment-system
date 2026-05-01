<jsp:include page="/WEB-INF/jsp/medecin/fragments/header.jsp"/>

<main class="container-fluid h-100 d-flex flex-column">
    <%
    String pageMedecin = (String) session.getAttribute("page_medecin");
    if (pageMedecin == null) pageMedecin = "rdv";

    if (pageMedecin.equals("rdv")) {
    %>
    <div class="flex-grow-1 overflow-auto">
        <jsp:include page="/WEB-INF/jsp/medecin/rdv.jsp"/>
    </div>
    <%
    } else if (pageMedecin.equals("patient")) {
    %>
    <div class="flex-grow-1 overflow-auto">
        <jsp:include page="/WEB-INF/jsp/medecin/patient.jsp"/>
    </div>
    <%
    } else if (pageMedecin.equals("horaire")) {
    %>
    <div class="flex-grow-1 overflow-auto">
        <jsp:include page="/WEB-INF/jsp/medecin/horaire.jsp"/>
    </div>
    <%
    }
    %>
</main>

<jsp:include page="/WEB-INF/jsp/medecin/fragments/footer.jsp"/>