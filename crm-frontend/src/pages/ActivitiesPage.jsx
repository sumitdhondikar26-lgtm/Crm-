import { useState, useEffect } from 'react';
import { activityAPI } from '../services/api';
import { Plus, X, Phone, Mail, Video, StickyNote, ListTodo } from 'lucide-react';

const TYPE_CONFIG = {
    CALL: { icon: Phone, color: 'var(--info)', bg: 'var(--info-bg)' },
    EMAIL: { icon: Mail, color: 'var(--purple)', bg: 'var(--purple-bg)' },
    MEETING: { icon: Video, color: 'var(--success)', bg: 'var(--success-bg)' },
    NOTE: { icon: StickyNote, color: 'var(--warning)', bg: 'var(--warning-bg)' },
    TASK: { icon: ListTodo, color: 'var(--cyan)', bg: 'var(--cyan-bg)' },
};

function ActivityModal({ activity, onClose, onSave }) {
    const now = new Date().toISOString().slice(0, 16);
    const [form, setForm] = useState(activity || {
        type: 'CALL', subject: '', description: '', activityDate: now,
        durationMinutes: '', outcome: '', customerId: ''
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
                customerId: parseInt(form.customerId),
                durationMinutes: form.durationMinutes ? parseInt(form.durationMinutes) : null
            };
            if (activity?.id) {
                await activityAPI.update(activity.id, payload);
            } else {
                await activityAPI.create(payload);
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
                    <h2>{activity?.id ? 'Edit Activity' : 'Log Activity'}</h2>
                    <button className="btn btn-ghost btn-sm" onClick={onClose}><X size={18} /></button>
                </div>
                <form onSubmit={handleSubmit}>
                    <div className="modal-body">
                        <div className="grid-2">
                            <div className="form-group">
                                <label>Type *</label>
                                <select className="form-control" name="type" value={form.type} onChange={handleChange}>
                                    <option value="CALL">Call</option>
                                    <option value="EMAIL">Email</option>
                                    <option value="MEETING">Meeting</option>
                                    <option value="NOTE">Note</option>
                                    <option value="TASK">Task</option>
                                </select>
                            </div>
                            <div className="form-group">
                                <label>Customer ID *</label>
                                <input className="form-control" name="customerId" type="number" value={form.customerId} onChange={handleChange} required />
                            </div>
                        </div>
                        <div className="form-group">
                            <label>Subject *</label>
                            <input className="form-control" name="subject" value={form.subject} onChange={handleChange} required />
                        </div>
                        <div className="form-group">
                            <label>Date & Time *</label>
                            <input className="form-control" name="activityDate" type="datetime-local" value={form.activityDate} onChange={handleChange} required />
                        </div>
                        <div className="grid-2">
                            <div className="form-group">
                                <label>Duration (minutes)</label>
                                <input className="form-control" name="durationMinutes" type="number" value={form.durationMinutes || ''} onChange={handleChange} />
                            </div>
                            <div className="form-group">
                                <label>Outcome</label>
                                <input className="form-control" name="outcome" value={form.outcome || ''} onChange={handleChange} />
                            </div>
                        </div>
                        <div className="form-group">
                            <label>Description</label>
                            <textarea className="form-control" name="description" value={form.description || ''} onChange={handleChange} rows={3} />
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

export default function ActivitiesPage() {
    const [activities, setActivities] = useState([]);
    const [loading, setLoading] = useState(true);
    const [modal, setModal] = useState(null);

    const loadActivities = async () => {
        setLoading(true);
        try {
            const data = await activityAPI.getAll(0, 50);
            setActivities(data.content);
        } catch (err) {
            console.error(err);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => { loadActivities(); }, []);

    const formatDate = (d) => {
        if (!d) return '';
        const date = new Date(d);
        return date.toLocaleDateString('en-US', { month: 'short', day: 'numeric', year: 'numeric', hour: '2-digit', minute: '2-digit' });
    };

    return (
        <>
            <div className="page-header">
                <h1>Activities</h1>
                <div className="header-actions">
                    <button className="btn btn-primary" onClick={() => setModal('new')}>
                        <Plus size={16} /> Log Activity
                    </button>
                </div>
            </div>
            <div className="page-body">
                {loading ? (
                    <div className="loading-spinner"><div className="spinner" /></div>
                ) : activities.length === 0 ? (
                    <div className="empty-state">
                        <h3>No activities yet</h3>
                        <p>Log your first activity</p>
                    </div>
                ) : (
                    <div className="card">
                        {activities.map(act => {
                            const cfg = TYPE_CONFIG[act.type] || TYPE_CONFIG.NOTE;
                            const Icon = cfg.icon;
                            return (
                                <div className="activity-item" key={act.id} style={{ cursor: 'pointer' }} onClick={() => setModal(act)}>
                                    <div className="activity-icon-wrap" style={{ background: cfg.bg, color: cfg.color }}>
                                        <Icon size={20} />
                                    </div>
                                    <div className="activity-content" style={{ flex: 1 }}>
                                        <h4>{act.subject}</h4>
                                        {act.description && <p>{act.description}</p>}
                                        <div className="activity-meta">
                                            <span>{act.customerName}</span>
                                            {act.performedByName && <span> · {act.performedByName}</span>}
                                            <span> · {formatDate(act.activityDate)}</span>
                                            {act.durationMinutes && <span> · {act.durationMinutes} min</span>}
                                        </div>
                                        {act.outcome && <div style={{ marginTop: 6, fontSize: 13, color: 'var(--success)', fontWeight: 500 }}>Outcome: {act.outcome}</div>}
                                    </div>
                                </div>
                            );
                        })}
                    </div>
                )}
            </div>
            {modal && (
                <ActivityModal
                    activity={modal === 'new' ? null : modal}
                    onClose={() => setModal(null)}
                    onSave={() => { setModal(null); loadActivities(); }}
                />
            )}
        </>
    );
}
