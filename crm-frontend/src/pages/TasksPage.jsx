import { useState, useEffect } from 'react';
import { taskAPI } from '../services/api';
import { Plus, X, CheckCircle2, Circle, Clock, XCircle } from 'lucide-react';

const STATUS_ICONS = {
    TODO: Circle,
    IN_PROGRESS: Clock,
    COMPLETED: CheckCircle2,
    CANCELLED: XCircle,
};

function TaskModal({ task, onClose, onSave }) {
    const [form, setForm] = useState(task || {
        title: '', description: '', status: 'TODO', priority: 'MEDIUM',
        dueDate: '', customerId: '', assignedToId: ''
    });
    const [saving, setSaving] = useState(false);
    const [error, setError] = useState('');

    const handleChange = (e) => setForm({ ...form, [e.target.name]: e.target.value });

    const handleSubmit = async (e) => {
        e.preventDefault();
        setSaving(true);
        setError('');
        try {
            const payload = {
                ...form,
                customerId: form.customerId ? parseInt(form.customerId) : null,
                assignedToId: form.assignedToId ? parseInt(form.assignedToId) : null
            };
            if (task?.id) {
                await taskAPI.update(task.id, payload);
            } else {
                await taskAPI.create(payload);
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
            <div className="modal" onClick={e => e.stopPropagation()}>
                <div className="modal-header">
                    <h2>{task?.id ? 'Edit Task' : 'New Task'}</h2>
                    <button className="btn btn-ghost btn-sm" onClick={onClose}><X size={18} /></button>
                </div>
                <form onSubmit={handleSubmit}>
                    <div className="modal-body">
                        <div className="form-group">
                            <label>Title *</label>
                            <input className="form-control" name="title" value={form.title} onChange={handleChange} required />
                        </div>
                        <div className="form-group">
                            <label>Description</label>
                            <textarea className="form-control" name="description" value={form.description || ''} onChange={handleChange} rows={3} />
                        </div>
                        <div className="grid-2">
                            <div className="form-group">
                                <label>Status</label>
                                <select className="form-control" name="status" value={form.status} onChange={handleChange}>
                                    <option value="TODO">To Do</option>
                                    <option value="IN_PROGRESS">In Progress</option>
                                    <option value="COMPLETED">Completed</option>
                                    <option value="CANCELLED">Cancelled</option>
                                </select>
                            </div>
                            <div className="form-group">
                                <label>Priority</label>
                                <select className="form-control" name="priority" value={form.priority} onChange={handleChange}>
                                    <option value="LOW">Low</option>
                                    <option value="MEDIUM">Medium</option>
                                    <option value="HIGH">High</option>
                                    <option value="URGENT">Urgent</option>
                                </select>
                            </div>
                        </div>
                        <div className="form-group">
                            <label>Due Date</label>
                            <input className="form-control" name="dueDate" type="date" value={form.dueDate || ''} onChange={handleChange} />
                        </div>
                        {error && <div className="error-text">{error}</div>}
                    </div>
                    <div className="modal-footer">
                        <button type="button" className="btn btn-secondary" onClick={onClose}>Cancel</button>
                        <button type="submit" className="btn btn-primary" disabled={saving}>{saving ? 'Saving...' : 'Save'}</button>
                    </div>
                </form>
            </div>
        </div>
    );
}

export default function TasksPage() {
    const [tasks, setTasks] = useState([]);
    const [loading, setLoading] = useState(true);
    const [filter, setFilter] = useState('ALL');
    const [modal, setModal] = useState(null);

    const loadTasks = async () => {
        setLoading(true);
        try {
            const data = filter === 'ALL'
                ? await taskAPI.getAll(0, 50)
                : await taskAPI.getByStatus(filter, 0);
            setTasks(data.content);
        } catch (err) {
            console.error(err);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => { loadTasks(); }, [filter]);

    const handleDelete = async (id) => {
        if (!confirm('Delete this task?')) return;
        try {
            await taskAPI.delete(id);
            loadTasks();
        } catch (err) {
            alert(err.message);
        }
    };

    return (
        <>
            <div className="page-header">
                <h1>Tasks</h1>
                <div className="header-actions">
                    <button className="btn btn-primary" onClick={() => setModal('new')}>
                        <Plus size={16} /> Add Task
                    </button>
                </div>
            </div>
            <div className="page-body">
                <div className="tabs">
                    {['ALL', 'TODO', 'IN_PROGRESS', 'COMPLETED', 'CANCELLED'].map(s => (
                        <button key={s} className={`tab ${filter === s ? 'active' : ''}`} onClick={() => setFilter(s)}>
                            {s === 'ALL' ? 'All' : s.replace('_', ' ')}
                        </button>
                    ))}
                </div>

                {loading ? (
                    <div className="loading-spinner"><div className="spinner" /></div>
                ) : tasks.length === 0 ? (
                    <div className="empty-state">
                        <h3>No tasks found</h3>
                        <p>Create a task to get started</p>
                    </div>
                ) : (
                    <div style={{ display: 'flex', flexDirection: 'column', gap: 10 }}>
                        {tasks.map(task => {
                            const Icon = STATUS_ICONS[task.status] || Circle;
                            const isOverdue = task.dueDate && task.status !== 'COMPLETED' && task.status !== 'CANCELLED' && new Date(task.dueDate) < new Date();
                            return (
                                <div className="card" key={task.id} style={{ padding: 16, cursor: 'pointer' }} onClick={() => setModal(task)}>
                                    <div style={{ display: 'flex', alignItems: 'flex-start', gap: 14 }}>
                                        <Icon size={20} style={{ marginTop: 2, color: task.status === 'COMPLETED' ? 'var(--success)' : task.status === 'IN_PROGRESS' ? 'var(--warning)' : 'var(--text-muted)', flexShrink: 0 }} />
                                        <div style={{ flex: 1 }}>
                                            <div style={{ display: 'flex', alignItems: 'center', gap: 10, marginBottom: 4 }}>
                                                <span style={{ fontWeight: 600, fontSize: 15, textDecoration: task.status === 'COMPLETED' ? 'line-through' : 'none', opacity: task.status === 'COMPLETED' ? 0.7 : 1 }}>{task.title}</span>
                                                <span className={`badge-status badge-${task.priority?.toLowerCase()}`}>{task.priority}</span>
                                            </div>
                                            {task.description && <p style={{ fontSize: 13, color: 'var(--text-secondary)', marginBottom: 6 }}>{task.description}</p>}
                                            <div style={{ display: 'flex', gap: 16, fontSize: 12, color: 'var(--text-muted)' }}>
                                                {task.customerName && <span>Customer: {task.customerName}</span>}
                                                {task.assignedToName && <span>Assigned: {task.assignedToName}</span>}
                                                {task.dueDate && <span style={{ color: isOverdue ? 'var(--danger)' : 'inherit' }}>Due: {task.dueDate}{isOverdue ? ' (Overdue!)' : ''}</span>}
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            );
                        })}
                    </div>
                )}
            </div>

            {modal && (
                <TaskModal
                    task={modal === 'new' ? null : modal}
                    onClose={() => setModal(null)}
                    onSave={() => { setModal(null); loadTasks(); }}
                />
            )}
        </>
    );
}
