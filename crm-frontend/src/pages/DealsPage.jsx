import { useState, useEffect } from 'react';
import { dealAPI } from '../services/api';
import { Plus, X } from 'lucide-react';

const STAGES = ['QUALIFICATION', 'NEEDS_ANALYSIS', 'PROPOSAL', 'NEGOTIATION', 'CLOSED_WON', 'CLOSED_LOST'];
const STAGE_LABELS = {
    QUALIFICATION: 'Qualification',
    NEEDS_ANALYSIS: 'Needs Analysis',
    PROPOSAL: 'Proposal',
    NEGOTIATION: 'Negotiation',
    CLOSED_WON: 'Closed Won',
    CLOSED_LOST: 'Closed Lost',
};

function DealModal({ deal, onClose, onSave }) {
    const [form, setForm] = useState(deal || {
        title: '', description: '', value: '', stage: 'QUALIFICATION',
        probability: 10, expectedCloseDate: '', source: '', notes: '', customerId: ''
    });
    const [saving, setSaving] = useState(false);
    const [error, setError] = useState('');

    const handleChange = (e) => setForm({ ...form, [e.target.name]: e.target.value });

    const handleSubmit = async (e) => {
        e.preventDefault();
        setSaving(true);
        setError('');
        try {
            const payload = { ...form, value: parseFloat(form.value), probability: parseInt(form.probability), customerId: parseInt(form.customerId) };
            if (deal?.id) {
                await dealAPI.update(deal.id, payload);
            } else {
                await dealAPI.create(payload);
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
                    <h2>{deal?.id ? 'Edit Deal' : 'New Deal'}</h2>
                    <button className="btn btn-ghost btn-sm" onClick={onClose}><X size={18} /></button>
                </div>
                <form onSubmit={handleSubmit}>
                    <div className="modal-body">
                        <div className="form-group">
                            <label>Title *</label>
                            <input className="form-control" name="title" value={form.title} onChange={handleChange} required />
                        </div>
                        <div className="grid-2">
                            <div className="form-group">
                                <label>Value ($) *</label>
                                <input className="form-control" name="value" type="number" step="0.01" value={form.value} onChange={handleChange} required />
                            </div>
                            <div className="form-group">
                                <label>Customer ID *</label>
                                <input className="form-control" name="customerId" type="number" value={form.customerId} onChange={handleChange} required />
                            </div>
                        </div>
                        <div className="grid-2">
                            <div className="form-group">
                                <label>Stage</label>
                                <select className="form-control" name="stage" value={form.stage} onChange={handleChange}>
                                    {STAGES.map(s => <option key={s} value={s}>{STAGE_LABELS[s]}</option>)}
                                </select>
                            </div>
                            <div className="form-group">
                                <label>Probability (%)</label>
                                <input className="form-control" name="probability" type="number" min="0" max="100" value={form.probability} onChange={handleChange} />
                            </div>
                        </div>
                        <div className="form-group">
                            <label>Expected Close Date</label>
                            <input className="form-control" name="expectedCloseDate" type="date" value={form.expectedCloseDate || ''} onChange={handleChange} />
                        </div>
                        <div className="form-group">
                            <label>Description</label>
                            <textarea className="form-control" name="description" value={form.description || ''} onChange={handleChange} rows={2} />
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

export default function DealsPage() {
    const [deals, setDeals] = useState([]);
    const [loading, setLoading] = useState(true);
    const [modal, setModal] = useState(null);
    const [view, setView] = useState('pipeline'); // 'pipeline' | 'list'

    const loadDeals = async () => {
        setLoading(true);
        try {
            const data = await dealAPI.getAll(0, 100);
            setDeals(data.content);
        } catch (err) {
            console.error(err);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => { loadDeals(); }, []);

    const formatCurrency = (v) => '$' + Number(v || 0).toLocaleString();
    const dealsByStage = {};
    STAGES.forEach(s => { dealsByStage[s] = deals.filter(d => d.stage === s); });

    return (
        <>
            <div className="page-header">
                <h1>Deals Pipeline</h1>
                <div className="header-actions">
                    <div className="tabs" style={{ margin: 0 }}>
                        <button className={`tab ${view === 'pipeline' ? 'active' : ''}`} onClick={() => setView('pipeline')}>Pipeline</button>
                        <button className={`tab ${view === 'list' ? 'active' : ''}`} onClick={() => setView('list')}>List</button>
                    </div>
                    <button className="btn btn-primary" onClick={() => setModal('new')}>
                        <Plus size={16} /> Add Deal
                    </button>
                </div>
            </div>
            <div className="page-body">
                {loading ? (
                    <div className="loading-spinner"><div className="spinner" /></div>
                ) : view === 'pipeline' ? (
                    <div className="pipeline-board">
                        {STAGES.map(stage => (
                            <div className="pipeline-column" key={stage}>
                                <div className="pipeline-header">
                                    <span>{STAGE_LABELS[stage]}</span>
                                    <span className="count">{dealsByStage[stage].length}</span>
                                </div>
                                <div className="pipeline-body">
                                    {dealsByStage[stage].length === 0 ? (
                                        <div style={{ padding: 20, textAlign: 'center', color: 'var(--text-muted)', fontSize: 13 }}>No deals</div>
                                    ) : dealsByStage[stage].map(deal => (
                                        <div className="pipeline-card" key={deal.id} onClick={() => setModal(deal)}>
                                            <h4>{deal.title}</h4>
                                            <div className="deal-customer">{deal.customerName}</div>
                                            <div className="deal-value">{formatCurrency(deal.value)}</div>
                                            <div className="deal-meta">
                                                <span>{deal.probability}% prob</span>
                                                {deal.expectedCloseDate && <span>{deal.expectedCloseDate}</span>}
                                            </div>
                                        </div>
                                    ))}
                                </div>
                            </div>
                        ))}
                    </div>
                ) : (
                    <div className="table-container">
                        <table>
                            <thead>
                                <tr>
                                    <th>Deal</th>
                                    <th>Customer</th>
                                    <th>Value</th>
                                    <th>Stage</th>
                                    <th>Probability</th>
                                    <th>Close Date</th>
                                </tr>
                            </thead>
                            <tbody>
                                {deals.map(d => (
                                    <tr key={d.id} className="clickable" onClick={() => setModal(d)}>
                                        <td style={{ fontWeight: 600 }}>{d.title}</td>
                                        <td style={{ color: 'var(--text-secondary)' }}>{d.customerName}</td>
                                        <td style={{ fontWeight: 600, color: 'var(--success)' }}>{formatCurrency(d.value)}</td>
                                        <td><span className={`badge-status badge-${d.stage?.toLowerCase()}`}>{STAGE_LABELS[d.stage] || d.stage}</span></td>
                                        <td>{d.probability}%</td>
                                        <td style={{ color: 'var(--text-secondary)' }}>{d.expectedCloseDate || '—'}</td>
                                    </tr>
                                ))}
                            </tbody>
                        </table>
                    </div>
                )}
            </div>

            {modal && (
                <DealModal
                    deal={modal === 'new' ? null : modal}
                    onClose={() => setModal(null)}
                    onSave={() => { setModal(null); loadDeals(); }}
                />
            )}
        </>
    );
}
