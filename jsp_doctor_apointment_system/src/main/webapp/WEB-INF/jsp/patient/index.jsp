<jsp:include page="/WEB-INF/jsp/patient/fragments/header.jsp"/>

<main class="container-fluid h-100">

    <%
    String pagePatient = (String) session.getAttribute("page_patient");
    if (pagePatient == null) pagePatient = "";

    if (pagePatient.equals("rdv")) {
    %>
    <jsp:include page="/WEB-INF/jsp/patient/rdv.jsp"/>
    <%
    } else if (pagePatient.equals("med")) {
    %>
    <jsp:include page="/WEB-INF/jsp/patient/medecin.jsp"/>
    <%
    }
    %>
</main>

<jsp:include page="/WEB-INF/jsp/patient/fragments/footer.jsp"/>