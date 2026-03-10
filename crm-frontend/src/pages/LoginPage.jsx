import { useState } from 'react';
import { useAuth } from '../context/AuthContext';
import { Zap } from 'lucide-react';

export default function LoginPage() {
    const { login } = useAuth();
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(false);

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');
        setLoading(true);
        try {
            await login(username, password);
        } catch {
            setError('Invalid username or password');
        } finally {
            setLoading(false);
        }
    };

    const fillDemo = (user, pass) => {
        setUsername(user);
        setPassword(pass);
    };

    return (
        <div className="login-page">
            <div className="login-card">
                <div className="logo-icon"><Zap size={24} /></div>
                <h1>CRM Pro</h1>
                <p className="subtitle">Sign in to your account</p>

                <form onSubmit={handleSubmit}>
                    <div className="form-group">
                        <label>Username</label>
                        <input
                            className="form-control"
                            type="text"
                            placeholder="Enter username"
                            value={username}
                            onChange={(e) => setUsername(e.target.value)}
                            required
                        />
                    </div>
                    <div className="form-group">
                        <label>Password</label>
                        <input
                            className="form-control"
                            type="password"
                            placeholder="Enter password"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            required
                        />
                    </div>

                    {error && <div className="error-text" style={{ marginBottom: 16 }}>{error}</div>}

                    <button className="btn btn-primary btn-block" disabled={loading} style={{ marginTop: 8, padding: '14px' }}>
                        {loading ? 'Signing in...' : 'Sign In'}
                    </button>
                </form>

                <div style={{ marginTop: 28, padding: '16px', background: 'var(--bg-input)', borderRadius: 'var(--radius-sm)', border: '1px solid var(--border)' }}>
                    <div style={{ fontSize: 12, fontWeight: 600, color: 'var(--text-muted)', marginBottom: 10, textTransform: 'uppercase', letterSpacing: '0.5px' }}>
                        Quick Login
                    </div>
                    <div style={{ display: 'flex', flexDirection: 'column', gap: 6 }}>
                        {[
                            { user: 'admin', pass: 'admin123', label: 'Admin' },
                            { user: 'jmanager', pass: 'manager123', label: 'Manager' },
                            { user: 'bsalesrep', pass: 'sales123', label: 'Sales Rep' },
                        ].map(d => (
                            <button
                                key={d.user}
                                className="btn btn-ghost btn-sm"
                                type="button"
                                onClick={() => fillDemo(d.user, d.pass)}
                                style={{ justifyContent: 'flex-start', fontSize: 13 }}
                            >
                                <span style={{ color: 'var(--accent)', fontWeight: 600 }}>{d.label}</span>
                                <span style={{ color: 'var(--text-muted)' }}>— {d.user} / {d.pass}</span>
                            </button>
                        ))}
                    </div>
                </div>
            </div>
        </div>
    );
}
