<jsp:include page="/WEB-INF/jsp/admin/fragments/header.jsp"/>

<main class="container-fluid flex-grow-1 overflow-auto py-3">
    <%
    String currentPage = (String) session.getAttribute("page_admin");
    if (currentPage == null) currentPage = "medecin";

    if (currentPage.equals("medecin")) {
    %>
    <jsp:include page="/WEB-INF/jsp/admin/medecin.jsp"/>
    <%
    } else if (currentPage.equals("patient")) {
    %>
    <jsp:include page="/WEB-INF/jsp/admin/patient.jsp"/>
    <%
    } else if (currentPage.equals("rdv")) {
    %>
    <jsp:include page="/WEB-INF/jsp/admin/rdv.jsp"/>
    <%
    }
    %>
</main>

<jsp:include page="/WEB-INF/jsp/admin/fragments/footer.jsp"/>