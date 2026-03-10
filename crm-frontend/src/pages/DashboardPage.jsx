import { useState, useEffect } from 'react';
import { dashboardAPI } from '../services/api';
import {
    Users, Handshake, DollarSign, ListTodo,
    AlertTriangle, Activity, TrendingUp, Trophy
} from 'lucide-react';

export default function DashboardPage() {
    const [data, setData] = useState(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        dashboardAPI.getMetrics()
            .then(setData)
            .catch(console.error)
            .finally(() => setLoading(false));
    }, []);

    if (loading) return <div className="loading-spinner"><div className="spinner" /></div>;
    if (!data) return <div className="empty-state"><h3>Failed to load dashboard</h3></div>;

    const formatCurrency = (v) => '$' + Number(v || 0).toLocaleString();

    const metrics = [
        { label: 'Total Customers', value: data.totalCustomers, icon: Users, color: 'blue' },
        { label: 'Active Customers', value: data.activeCustomers, icon: TrendingUp, color: 'green' },
        { label: 'Total Deals', value: data.totalDeals, icon: Handshake, color: 'purple' },
        { label: 'Pipeline Value', value: formatCurrency(data.totalDealValue), icon: DollarSign, color: 'cyan' },
        { label: 'Revenue Won', value: formatCurrency(data.wonDealValue), icon: Trophy, color: 'green' },
        { label: 'Open Tasks', value: data.openTasks, icon: ListTodo, color: 'orange' },
        { label: 'Overdue Tasks', value: data.overdueTasks, icon: AlertTriangle, color: 'red' },
        { label: 'Activities This Week', value: data.activitiesThisWeek, icon: Activity, color: 'blue' },
    ];

    return (
        <>
            <div className="page-header">
                <h1>Dashboard</h1>
            </div>
            <div className="page-body">
                <div className="metrics-grid">
                    {metrics.map((m, i) => (
                        <div className="metric-card" key={i}>
                            <div className={`metric-icon ${m.color}`}>
                                <m.icon size={24} />
                            </div>
                            <div>
                                <div className="metric-value">{m.value}</div>
                                <div className="metric-label">{m.label}</div>
                            </div>
                        </div>
                    ))}
                </div>

                <div className="grid-2">
                    <div className="card">
                        <div className="card-header"><h3>Customers by Status</h3></div>
                        {data.customersByStatus && Object.entries(data.customersByStatus).map(([k, v]) => (
                            <div key={k} style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', padding: '10px 0', borderBottom: '1px solid var(--border)' }}>
                                <span className={`badge-status badge-${k.toLowerCase()}`}>{k}</span>
                                <span style={{ fontWeight: 600 }}>{v}</span>
                            </div>
                        ))}
                    </div>

                    <div className="card">
                        <div className="card-header"><h3>Deals by Stage</h3></div>
                        {data.dealsByStage && Object.entries(data.dealsByStage).map(([k, v]) => (
                            <div key={k} style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', padding: '10px 0', borderBottom: '1px solid var(--border)' }}>
                                <span className={`badge-status badge-${k.toLowerCase()}`}>{k.replace('_', ' ')}</span>
                                <div style={{ textAlign: 'right' }}>
                                    <span style={{ fontWeight: 600 }}>{v}</span>
                                    {data.dealValueByStage?.[k] && (
                                        <span style={{ fontSize: 12, color: 'var(--text-muted)', marginLeft: 8 }}>
                                            {formatCurrency(data.dealValueByStage[k])}
                                        </span>
                                    )}
                                </div>
                            </div>
                        ))}
                    </div>

                    <div className="card">
                        <div className="card-header"><h3>Tasks by Status</h3></div>
                        {data.tasksByStatus && Object.entries(data.tasksByStatus).map(([k, v]) => (
                            <div key={k} style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', padding: '10px 0', borderBottom: '1px solid var(--border)' }}>
                                <span className={`badge-status badge-${k.toLowerCase()}`}>{k.replace('_', ' ')}</span>
                                <span style={{ fontWeight: 600 }}>{v}</span>
                            </div>
                        ))}
                    </div>

                    <div className="card">
                        <div className="card-header"><h3>Activities by Type</h3></div>
                        {data.activitiesByType && Object.entries(data.activitiesByType).map(([k, v]) => (
                            <div key={k} style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', padding: '10px 0', borderBottom: '1px solid var(--border)' }}>
                                <span style={{ fontWeight: 500, textTransform: 'capitalize' }}>{k.toLowerCase()}</span>
                                <span style={{ fontWeight: 600 }}>{v}</span>
                            </div>
                        ))}
                    </div>
                </div>
            </div>
        </>
    );
}
