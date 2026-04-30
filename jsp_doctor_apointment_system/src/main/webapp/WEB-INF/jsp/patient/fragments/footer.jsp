<%@ page contentType="text/html;charset=UTF-8" language="java"%>

<footer class="bg-light text-center text-muted py-1 mt-1  flex-shring-0">
    <div class="container">
        <small style="font-size: 12px">&copy; <%= java.time.LocalDate.now().getYear() %> Rendez-vous docteur - développé
            par Nantenaina & Lynda</small>
    </div>
</footer>
<script src="${pageContext.request.contextPath}/bootstrap-5.3.3/js/bootstrap.bundle.min.js"></script>
<script src="${pageContext.request.contextPath}/fontawesome-pro-7.1.0-web/js/fontawesome.js"></script>
<script src="${pageContext.request.contextPath}/fontawesome-pro-7.1.0-web/js/duotone.js"></script>
<script>
    var tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
    var tooltipList = tooltipTriggerList.map(function (tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl);
    });
    const contextPath = "${pageContext.request.contextPath}";
</script>
<script src="${pageContext.request.contextPath}/js/patient.js"></script>
</body>
</html>