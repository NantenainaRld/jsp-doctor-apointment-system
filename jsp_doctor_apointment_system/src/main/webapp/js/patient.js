// Cancel RDV modal handler
let rdvIdToCancel = null;

function cancelRdv(id) {
    rdvIdToCancel = id;
    const confirmBtn = document.getElementById('confirmCancelBtn');
    if (confirmBtn) {
        confirmBtn.href = contextPath + '/patient?action=cancel_rdv&id=' + id;
    }
    const modal = new bootstrap.Modal(document.getElementById('cancelModal'));
    modal.show();
}

// Update rdv
let rdvIdToUpdate = null;

function updateRdv(id) {
    rdvIdToUpdate = id;
    const confirmBtn = document.getElementById('confirmUpdateBtn');
    if (confirmBtn) {
        confirmBtn.href = contextPath + '/patient?action=update_rdv&id=' + id;
    }
    const modal = new bootstrap.Modal(document.getElementById('updateModal'));
    modal.show();
}