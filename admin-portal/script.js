const API_URL = 'http://localhost:5000';

let editingId = null;

// ── Load & render ────────────────────────────────────────────────────────────

async function loadNotifications() {
    const tbody = document.getElementById('tableBody');
    tbody.innerHTML = '<tr><td colspan="8" class="empty">Loading...</td></tr>';
    try {
        const res = await fetch(`${API_URL}/api/admin/notifications`);
        const data = await res.json();
        renderTable(data.notifications);
    } catch (err) {
        tbody.innerHTML = '<tr><td colspan="8" class="empty">Failed to load. Is the backend running?</td></tr>';
    }
}

function renderTable(notifications) {
    const tbody = document.getElementById('tableBody');
    if (!notifications || notifications.length === 0) {
        tbody.innerHTML = '<tr><td colspan="8" class="empty">No notifications yet.</td></tr>';
        return;
    }
    tbody.innerHTML = notifications.map(n => `
        <tr>
            <td><strong>${escHtml(n.title)}</strong></td>
            <td>${escHtml(n.targetScreen)}</td>
            <td>${escHtml(n.audience)}</td>
            <td>${n.showOnce ? 'Yes' : 'No'}</td>
            <td><span class="badge ${n.isActive ? 'badge-active' : 'badge-inactive'}">${n.isActive ? 'Active' : 'Inactive'}</span></td>
            <td>${n.viewsCounter ?? 0}</td>
            <td>${n.clicksCounter ?? 0}</td>
            <td>
                <div class="actions">
                    <button class="btn btn-edit" onclick="startEdit(${JSON.stringify(n).replace(/"/g, '&quot;')})">Edit</button>
                    ${n.isActive
                        ? `<button class="btn btn-warning" onclick="toggleActive('${n.id}', false)">Deactivate</button>`
                        : `<button class="btn btn-success" onclick="toggleActive('${n.id}', true)">Activate</button>`
                    }
                    <button class="btn btn-danger" onclick="deleteNotification('${n.id}')">Delete</button>
                </div>
            </td>
        </tr>
    `).join('');
}

// ── Create / Update ──────────────────────────────────────────────────────────

document.getElementById('notifForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    const data = getFormData();
    try {
        if (editingId) {
            await fetch(`${API_URL}/api/notifications/${editingId}`, {
                method: 'PUT',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(data)
            });
        } else {
            await fetch(`${API_URL}/api/notifications`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(data)
            });
        }
        clearForm();
        loadNotifications();
    } catch (err) {
        alert('Error saving notification.');
    }
});

// ── Edit ─────────────────────────────────────────────────────────────────────

function startEdit(notification) {
    editingId = notification.id;
    document.getElementById('formTitle').textContent = 'Edit Notification';
    document.getElementById('btnSubmit').textContent = 'Update';
    document.getElementById('title').value       = notification.title;
    document.getElementById('message').value     = notification.message;
    document.getElementById('targetScreen').value= notification.targetScreen;
    document.getElementById('audience').value    = notification.audience;
    document.getElementById('startDate').value   = notification.startDate;
    document.getElementById('endDate').value     = notification.endDate;
    document.getElementById('showOnce').checked  = notification.showOnce;
    document.getElementById('imageUrl').value    = notification.imageUrl || '';
    window.scrollTo({ top: 0, behavior: 'smooth' });
}

// ── Delete ───────────────────────────────────────────────────────────────────

async function deleteNotification(id) {
    if (!confirm('Delete this notification?')) return;
    await fetch(`${API_URL}/api/notifications/${id}`, { method: 'DELETE' });
    loadNotifications();
}

// ── Activate / Deactivate ────────────────────────────────────────────────────

async function toggleActive(id, activate) {
    const action = activate ? 'activate' : 'deactivate';
    await fetch(`${API_URL}/api/notifications/${id}/${action}`, { method: 'PATCH' });
    loadNotifications();
}

// ── Helpers ──────────────────────────────────────────────────────────────────

function getFormData() {
    const today = new Date().toISOString().split('T')[0];
    return {
        title:        document.getElementById('title').value.trim(),
        message:      document.getElementById('message').value.trim(),
        targetScreen: document.getElementById('targetScreen').value.trim(),
        audience:     document.getElementById('audience').value,
        startDate:    document.getElementById('startDate').value || today,
        endDate:      document.getElementById('endDate').value || '2099-12-31',
        showOnce:     document.getElementById('showOnce').checked,
        imageUrl:     document.getElementById('imageUrl').value.trim() || null
    };
}

function clearForm() {
    editingId = null;
    document.getElementById('formTitle').textContent  = 'Create Notification';
    document.getElementById('btnSubmit').textContent  = 'Create';
    document.getElementById('notifForm').reset();
}

function escHtml(str) {
    return String(str)
        .replace(/&/g, '&amp;')
        .replace(/</g, '&lt;')
        .replace(/>/g, '&gt;');
}

document.getElementById('btnCancel').addEventListener('click', clearForm);

// ── Init ─────────────────────────────────────────────────────────────────────

loadNotifications();
