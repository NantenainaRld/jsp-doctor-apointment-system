<%@ page contentType="text/html;charset=UTF-8" language="java" %>

</div>

<footer class="bg-light text-center text-muted py-2 mt-auto flex-shrink-0">
    <div class="container">
        <small>
            <i class="fad fa-copyright me-1"></i> <%= java.time.Year.now().getValue() %> Doctor Appointment -
            Administration - Tous droits réservés
        </small>
    </div>
</footer>

<script src="${pageContext.request.contextPath}/bootstrap-5.3.3/js/bootstrap.bundle.min.js"></script>
<script src="${pageContext.request.contextPath}/fontawesome-pro-7.1.0-web/js/fontawesome.js"></script>
<script src="${pageContext.request.contextPath}/fontawesome-pro-7.1.0-web/js/duotone.js"></script>
<script>
    // Initialize tooltips
    document.addEventListener('DOMContentLoaded', function() {
        var tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
        tooltipTriggerList.forEach(function (tooltipTriggerEl) {
            new bootstrap.Tooltip(tooltipTriggerEl);
        });
    });
     // Delete modal handler
        function openDeleteModal(id) {
            const confirmBtn = document.getElementById('confirmDeleteBtn');
            if (confirmBtn) {
                confirmBtn.href = '${pageContext.request.contextPath}/admin?action=delete_medecin&id=' + id;
            }
            const modal = new bootstrap.Modal(document.getElementById('deleteMedecinModal'));
            modal.show();
        }

     // Delete modal handler
        function openDeleteModal(id) {
            var confirmBtn = document.getElementById('confirmDeleteBtn');
            if (confirmBtn) {
                confirmBtn.href = '${pageContext.request.contextPath}/admin?action=delete_patient&id=' + id;
            }
            var modal = new bootstrap.Modal(document.getElementById('deletePatientModal'));
            modal.show();
        }

      // Edit RDV modal handler
    function openEditRdvModal(id, date, heureDebut, heureFin) {
        document.getElementById('editRdvId').value = id;
        document.getElementById('editRdvIdLabel').innerText = id;
        document.getElementById('editDateRdvDisplay').innerText = date;
        document.getElementById('editHeureDebutRdv').value = heureDebut;
        document.getElementById('editHeureFinRdv').value = heureFin;
        var modal = new bootstrap.Modal(document.getElementById('editRdvModal'));
        modal.show();
    }

    // Cancel RDV modal handler
    function openCancelModal(id) {
        document.getElementById('confirmCancelBtn').href = '${pageContext.request.contextPath}/admin?action=cancel_rdv_admin&id=' + id;
        var modal = new bootstrap.Modal(document.getElementById('cancelRdvModal'));
        modal.show();
    }

    // Confirm RDV modal handler
    function openConfirmModal(id) {
        document.getElementById('confirmRdvBtn').href = '${pageContext.request.contextPath}/admin?action=confirm_rdv_admin&id=' + id;
        var modal = new bootstrap.Modal(document.getElementById('confirmRdvModal'));
        modal.show();
    }

    // Notify RDV modal handler
    function openNotifyModal(id) {
        document.getElementById('confirmNotifyBtn').href = '${pageContext.request.contextPath}/admin?action=notify_rdv_admin&id=' + id;
        var modal = new bootstrap.Modal(document.getElementById('notifyRdvModal'));
        modal.show();
    }

      // Delete RDV modal handler
    function openDeleteRdvModal(id) {
        document.getElementById('confirmDeleteRdvBtn').href = '${pageContext.request.contextPath}/admin?action=delete_rdv_admin&id=' + id;
        var modal = new bootstrap.Modal(document.getElementById('deleteRdvModal'));
        modal.show();
    }
</script>
</body>
</html>