// ===== Config =====
const API_BASE = 'http://localhost:8080/api/auth';

// ===== Token helpers =====
const TokenStore = {
    get: ()          => localStorage.getItem('auth_token'),
    set: (token)     => localStorage.setItem('auth_token', token),
    remove: ()       => localStorage.removeItem('auth_token'),
    exists: ()       => !!localStorage.getItem('auth_token'),
};

// ===== HTTP helpers =====
async function apiFetch(endpoint, options = {}) {
    const url = `${API_BASE}${endpoint}`;
    const defaultHeaders = { 'Content-Type': 'application/json' };

    const response = await fetch(url, {
        ...options,
        headers: { ...defaultHeaders, ...options.headers },
    });

    let data = null;
    const contentType = response.headers.get('content-type') || '';
    if (contentType.includes('application/json')) {
        data = await response.json();
    }

    return { ok: response.ok, status: response.status, data };
}

async function apiFetchAuth(endpoint, options = {}) {
    const token = TokenStore.get();
    return apiFetch(endpoint, {
        ...options,
        headers: {
            ...options.headers,
            Authorization: `Bearer ${token}`,
        },
    });
}

// ===== UI helpers =====
function showAlert(id, message, type = 'error') {
    const el = document.getElementById(id);
    if (!el) return;
    el.textContent = message;
    el.className = `alert alert-${type}`;
    el.style.display = 'block';
}

function hideAlert(id) {
    const el = document.getElementById(id);
    if (el) el.style.display = 'none';
}

function setLoading(btn, loading) {
    if (loading) {
        btn.disabled = true;
        btn.dataset.originalText = btn.textContent;
        btn.innerHTML = `<span class="spinner"></span>${btn.dataset.originalText}`;
    } else {
        btn.disabled = false;
        btn.textContent = btn.dataset.originalText || btn.textContent;
    }
}

// ===== Auth guard =====
function requireAuth() {
    if (!TokenStore.exists()) {
        window.location.href = 'login.html';
        return false;
    }
    return true;
}

function redirectIfLoggedIn(destination = 'dashboard.html') {
    if (TokenStore.exists()) {
        window.location.href = destination;
    }
}
