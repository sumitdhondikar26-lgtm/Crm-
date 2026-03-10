const API_BASE = (import.meta.env.VITE_API_BASE_URL || 'http://localhost:8081') + '/api';

function getToken() {
    return localStorage.getItem('crm_token');
}

async function request(endpoint, options = {}) {
    const token = getToken();
    const headers = {
        'Content-Type': 'application/json',
        ...(token && { Authorization: `Bearer ${token}` }),
        ...options.headers,
    };

    const response = await fetch(`${API_BASE}${endpoint}`, {
        ...options,
        headers,
    });

    if (response.status === 401) {
        localStorage.removeItem('crm_token');
        localStorage.removeItem('crm_user');
        window.location.href = '/';
        throw new Error('Unauthorized');
    }

    if (response.status === 204) return null;

    if (!response.ok) {
        const error = await response.json().catch(() => ({ message: 'Request failed' }));
        throw new Error(error.message || `HTTP ${response.status}`);
    }

    return response.json();
}

// Auth
export const authAPI = {
    login: (data) => request('/auth/login', { method: 'POST', body: JSON.stringify(data) }),
    register: (data) => request('/auth/register', { method: 'POST', body: JSON.stringify(data) }),
};

// Customers
export const customerAPI = {
    getAll: (page = 0, size = 10, sortBy = 'createdAt', sortDir = 'desc') =>
        request(`/customers?page=${page}&size=${size}&sortBy=${sortBy}&sortDir=${sortDir}`),
    getById: (id) => request(`/customers/${id}`),
    search: (q, page = 0, size = 10) => request(`/customers/search?q=${q}&page=${page}&size=${size}`),
    getByStatus: (status, page = 0, size = 10) => request(`/customers/status/${status}?page=${page}&size=${size}`),
    create: (data) => request('/customers', { method: 'POST', body: JSON.stringify(data) }),
    update: (id, data) => request(`/customers/${id}`, { method: 'PUT', body: JSON.stringify(data) }),
    delete: (id) => request(`/customers/${id}`, { method: 'DELETE' }),
};

// Contacts
export const contactAPI = {
    getByCustomer: (customerId, page = 0, size = 10) =>
        request(`/contacts/customer/${customerId}?page=${page}&size=${size}`),
    getById: (id) => request(`/contacts/${id}`),
    create: (data) => request('/contacts', { method: 'POST', body: JSON.stringify(data) }),
    update: (id, data) => request(`/contacts/${id}`, { method: 'PUT', body: JSON.stringify(data) }),
    delete: (id) => request(`/contacts/${id}`, { method: 'DELETE' }),
};

// Deals
export const dealAPI = {
    getAll: (page = 0, size = 10, sortBy = 'createdAt', sortDir = 'desc') =>
        request(`/deals?page=${page}&size=${size}&sortBy=${sortBy}&sortDir=${sortDir}`),
    getById: (id) => request(`/deals/${id}`),
    getByCustomer: (customerId, page = 0) => request(`/deals/customer/${customerId}?page=${page}`),
    getByStage: (stage, page = 0) => request(`/deals/stage/${stage}?page=${page}`),
    create: (data) => request('/deals', { method: 'POST', body: JSON.stringify(data) }),
    update: (id, data) => request(`/deals/${id}`, { method: 'PUT', body: JSON.stringify(data) }),
    delete: (id) => request(`/deals/${id}`, { method: 'DELETE' }),
};

// Tasks
export const taskAPI = {
    getAll: (page = 0, size = 10) => request(`/tasks?page=${page}&size=${size}`),
    getById: (id) => request(`/tasks/${id}`),
    getByStatus: (status, page = 0) => request(`/tasks/status/${status}?page=${page}`),
    create: (data) => request('/tasks', { method: 'POST', body: JSON.stringify(data) }),
    update: (id, data) => request(`/tasks/${id}`, { method: 'PUT', body: JSON.stringify(data) }),
    delete: (id) => request(`/tasks/${id}`, { method: 'DELETE' }),
};

// Activities
export const activityAPI = {
    getAll: (page = 0, size = 10) => request(`/activities?page=${page}&size=${size}`),
    getByCustomer: (customerId, page = 0) => request(`/activities/customer/${customerId}?page=${page}`),
    create: (data) => request('/activities', { method: 'POST', body: JSON.stringify(data) }),
    update: (id, data) => request(`/activities/${id}`, { method: 'PUT', body: JSON.stringify(data) }),
    delete: (id) => request(`/activities/${id}`, { method: 'DELETE' }),
};

// Dashboard
export const dashboardAPI = {
    getMetrics: () => request('/dashboard'),
};
