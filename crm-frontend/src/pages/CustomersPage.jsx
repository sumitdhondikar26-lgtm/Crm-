import { useState, useEffect } from 'react';
import { customerAPI } from '../services/api';
import { Plus, Search, Edit2, Trash2, X } from 'lucide-react';

const STATUSES = ['LEAD', 'PROSPECT', 'ACTIVE', 'INACTIVE', 'CHURNED'];

function CustomerModal({ customer, onClose, onSave }) {
    const [form, setForm] = useState(customer || {
        firstName: '', lastName: '', email: '', phone: '', company: '',
        jobTitle: '', industry: '', address: '', city: '', state: '', country: '', zipCode: '',
        status: 'LEAD', source: '', notes: ''
    });
    const [saving, setSaving] = useState(false);
    const [error, setError] = useState('');

    const handleChange = (e) => setForm({ ...form, [e.target.name]: e.target.value });

    const handleSubmit = async (e) => {
        e.preventDefault();
        setSaving(true);
        setError('');
        try {
            if (customer?.id) {
                await customerAPI.update(customer.id, form);
            } else {
                await customerAPI.create(form);
            }
            onSave();
        } catch (err) {
            setError(err.message);
        } finally {
            setSaving(false);
        }
    };

    return (
        <div className="modal-overlay" onClick={onClose}>
            <div className="modal" onClick={(e) => e.stopPropagation()}>
                <div className="modal-header">
                    <h2>{customer?.id ? 'Edit Customer' : 'New Customer'}</h2>
                    <button className="btn btn-ghost btn-sm" onClick={onClose}><X size={18} /></button>
                </div>
                <form onSubmit={handleSubmit}>
                    <div className="modal-body">
                        <div className="grid-2">
                            <div className="form-group">
                                <label>First Name *</label>
                                <input className="form-control" name="firstName" value={form.firstName} onChange={handleChange} required />
                            </div>
                            <div className="form-group">
                                <label>Last Name *</label>
                                <input className="form-control" name="lastName" value={form.lastName} onChange={handleChange} required />
                            </div>
                        </div>
                        <div className="form-group">
                            <label>Email *</label>
                            <input className="form-control" name="email" type="email" value={form.email} onChange={handleChange} required />
                        </div>
                        <div className="grid-2">
                            <div className="form-group">
                                <label>Phone</label>
                                <input className="form-control" name="phone" value={form.phone || ''} onChange={handleChange} />
                            </div>
                            <div className="form-group">
                                <label>Company</label>
                                <input className="form-control" name="company" value={form.company || ''} onChange={handleChange} />
                            </div>
                        </div>
                        <div className="grid-2">
                            <div className="form-group">
                                <label>Job Title</label>
                                <input className="form-control" name="jobTitle" value={form.jobTitle || ''} onChange={handleChange} />
                            </div>
                            <div className="form-group">
                                <label>Industry</label>
                                <input className="form-control" name="industry" value={form.industry || ''} onChange={handleChange} />
                            </div>
                        </div>
                        <div className="grid-2">
                            <div className="form-group">
                                <label>Status</label>
                                <select className="form-control" name="status" value={form.status} onChange={handleChange}>
                                    {STATUSES.map(s => <option key={s} value={s}>{s}</option>)}
                                </select>
                            </div>
                            <div className="form-group">
                                <label>Source</label>
                                <input className="form-control" name="source" value={form.source || ''} onChange={handleChange} placeholder="e.g. Website, Referral" />
                            </div>
                        </div>
                        <div className="form-group">
                            <label>Notes</label>
                            <textarea className="form-control" name="notes" value={form.notes || ''} onChange={handleChange} rows={3} />
                        </div>
                        {error && <div className="error-text">{error}</div>}
                    </div>
                    <div className="modal-footer">
                        <button type="button" className="btn btn-secondary" onClick={onClose}>Cancel</button>
                        <button type="submit" className="btn btn-primary" disabled={saving}>
                            {saving ? 'Saving...' : (customer?.id ? 'Update' : 'Create')}
                        </button>
                    </div>
                </form>
            </div>
        </div>
    );
}

export default function CustomersPage() {
    const [customers, setCustomers] = useState([]);
    const [page, setPage] = useState(0);
    const [totalPages, setTotalPages] = useState(0);
    const [total, setTotal] = useState(0);
    const [search, setSearch] = useState('');
    const [loading, setLoading] = useState(true);
    const [modal, setModal] = useState(null); // null | 'new' | customer obj

    const loadCustomers = async () => {
        setLoading(true);
        try {
            const data = search
                ? await customerAPI.search(search, page, 10)
                : await customerAPI.getAll(page, 10);
            setCustomers(data.content);
            setTotalPages(data.totalPages);
            setTotal(data.totalElements);
        } catch (err) {
            console.error(err);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => { loadCustomers(); }, [page]);

    const handleSearch = (e) => {
        e.preventDefault();
        setPage(0);
        loadCustomers();
    };

    const handleDelete = async (id) => {
        if (!confirm('Delete this customer?')) return;
        try {
            await customerAPI.delete(id);
            loadCustomers();
        } catch (err) {
            alert(err.message);
        }
    };

    return (
        <>
            <div className="page-header">
                <h1>Customers</h1>
                <div className="header-actions">
                    <button className="btn btn-primary" onClick={() => setModal('new')}>
                        <Plus size={16} /> Add Customer
                    </button>
                </div>
            </div>
            <div className="page-body">
                <div className="table-container">
                    <div className="table-toolbar">
                        <form onSubmit={handleSearch} className="search-box">
                            <Search size={16} />
                            <input
                                placeholder="Search customers..."
                                value={search}
                                onChange={(e) => setSearch(e.target.value)}
                            />
                        </form>
                        <span style={{ fontSize: 13, color: 'var(--text-muted)' }}>{total} customers</span>
                    </div>

                    {loading ? (
                        <div className="loading-spinner"><div className="spinner" /></div>
                    ) : customers.length === 0 ? (
                        <div className="empty-state">
                            <Users size={48} />
                            <h3>No customers found</h3>
                            <p>Create your first customer to get started</p>
                        </div>
                    ) : (
                        <>
                            <table>
                                <thead>
                                    <tr>
                                        <th>Name</th>
                                        <th>Email</th>
                                        <th>Company</th>
                                        <th>Status</th>
                                        <th>Assigned To</th>
                                        <th style={{ width: 100 }}>Actions</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    {customers.map((c) => (
                                        <tr key={c.id}>
                                            <td style={{ fontWeight: 600 }}>{c.firstName} {c.lastName}</td>
                                            <td style={{ color: 'var(--text-secondary)' }}>{c.email}</td>
                                            <td>{c.company || '—'}</td>
                                            <td><span className={`badge-status badge-${c.status?.toLowerCase()}`}>{c.status}</span></td>
                                            <td style={{ color: 'var(--text-secondary)' }}>{c.assignedToName || '—'}</td>
                                            <td>
                                                <div style={{ display: 'flex', gap: 4 }}>
                                                    <button className="btn btn-ghost btn-sm" onClick={() => setModal(c)}><Edit2 size={14} /></button>
                                                    <button className="btn btn-ghost btn-sm" onClick={() => handleDelete(c.id)} style={{ color: 'var(--danger)' }}><Trash2 size={14} /></button>
                                                </div>
                                            </td>
                                        </tr>
                                    ))}
                                </tbody>
                            </table>
                            <div className="table-pagination">
                                <span>Page {page + 1} of {totalPages}</span>
                                <div style={{ display: 'flex', gap: 8 }}>
                                    <button className="btn btn-secondary btn-sm" disabled={page === 0} onClick={() => setPage(p => p - 1)}>Previous</button>
                                    <button className="btn btn-secondary btn-sm" disabled={page >= totalPages - 1} onClick={() => setPage(p => p + 1)}>Next</button>
                                </div>
                            </div>
                        </>
                    )}
                </div>
            </div>

            {modal && (
                <CustomerModal
                    customer={modal === 'new' ? null : modal}
                    onClose={() => setModal(null)}
                    onSave={() => { setModal(null); loadCustomers(); }}
                />
            )}
        </>
    );
}
