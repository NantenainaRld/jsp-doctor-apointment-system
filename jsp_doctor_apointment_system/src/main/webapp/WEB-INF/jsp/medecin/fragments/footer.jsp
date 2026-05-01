<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<footer class="bg-light text-center text-muted py-2 mt-auto flex-shrink-0">
    <div class="container">
        <small>
            <i class="fad fa-copyright me-1"></i> <%= java.time.Year.now().getValue() %> Doctor Appointment -
            Tous droits réservés
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

      // Cancel RDV modal handler
    let currentRdvId = null;

    function openCancelModal(rdvId) {
        currentRdvId = rdvId;
        const confirmBtn = document.getElementById('confirmCancelBtn');
        if (confirmBtn) {
            confirmBtn.href = '${pageContext.request.contextPath}/medecin?action=cancel_rdv&id=' + rdvId;
        }
        const modal = new bootstrap.Modal(document.getElementById('cancelRdvModal'));
        modal.show();
    }

       // Confirm RDV modal handler
    function openConfirmModal(rdvId) {
        const confirmBtn = document.getElementById('confirmRdvBtn');
        if (confirmBtn) {
            confirmBtn.href = '${pageContext.request.contextPath}/medecin?action=confirm_rdv&id=' + rdvId;
        }
        const modal = new bootstrap.Modal(document.getElementById('confirmRdvModal'));
        modal.show();
    }

    // Notify RDV modal handler
    function openNotifyModal(rdvId) {
        const confirmBtn = document.getElementById('confirmNotifyBtn');
        if (confirmBtn) {
            confirmBtn.href = '${pageContext.request.contextPath}/medecin?action=notifier_rdv&id=' + rdvId;
        }
        const modal = new bootstrap.Modal(document.getElementById('notifyRdvModal'));
        modal.show();
    }

   // Edit modal handler
    function openEditModal(id, date, debut, fin) {
        document.getElementById('editIdDispo').value = id;
        document.getElementById('editIdDispoLabel').innerText = id;
        document.getElementById('editDateLabel').innerText = date;
        document.getElementById('editDateDispo').value = date;
        document.getElementById('editHeureDebut').value = debut;
        document.getElementById('editHeureFin').value = fin;
        const modal = new bootstrap.Modal(document.getElementById('editDispoModal'));
        modal.show();
    }

    // Delete modal handler
    function openDeleteModal(id) {
        const confirmBtn = document.getElementById('confirmDeleteBtn');
        if (confirmBtn) {
            confirmBtn.href = '${pageContext.request.contextPath}/medecin?action=delete_dispo&id=' + id;
        }
        const modal = new bootstrap.Modal(document.getElementById('deleteDispoModal'));
        modal.show();
    }

    // Delete modal handler
    function openDeleteModal(id) {
        const confirmBtn = document.getElementById('confirmDeleteBtn');
        if (confirmBtn) {
            confirmBtn.href = '${pageContext.request.contextPath}/medecin?action=delete_dispo&id=' + id;
        }
        const modal = new bootstrap.Modal(document.getElementById('deleteDispoModal'));
        modal.show();
    }2
</script>

</script>
</body>
</html>