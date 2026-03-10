import { NavLink, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import {
    LayoutDashboard, Users, Handshake, ListTodo,
    Activity, LogOut, ChevronRight
} from 'lucide-react';

export default function Sidebar() {
    const { user, logout } = useAuth();
    const navigate = useNavigate();

    const handleLogout = () => {
        logout();
        navigate('/');
    };

    const initials = user ? (user.fullName || '').split(' ').map(n => n[0]).join('').toUpperCase() : '?';

    return (
        <aside className="sidebar">
            <div className="sidebar-logo">
                <div className="logo-box">C</div>
                <span>CRM Pro</span>
            </div>

            <nav className="sidebar-nav">
                <div className="nav-section">
                    <div className="nav-section-title">Main</div>
                    <NavLink to="/dashboard" className={({ isActive }) => `nav-item ${isActive ? 'active' : ''}`}>
                        <LayoutDashboard className="nav-icon" />
                        Dashboard
                    </NavLink>
                    <NavLink to="/customers" className={({ isActive }) => `nav-item ${isActive ? 'active' : ''}`}>
                        <Users className="nav-icon" />
                        Customers
                    </NavLink>
                    <NavLink to="/deals" className={({ isActive }) => `nav-item ${isActive ? 'active' : ''}`}>
                        <Handshake className="nav-icon" />
                        Deals
                    </NavLink>
                </div>

                <div className="nav-section">
                    <div className="nav-section-title">Productivity</div>
                    <NavLink to="/tasks" className={({ isActive }) => `nav-item ${isActive ? 'active' : ''}`}>
                        <ListTodo className="nav-icon" />
                        Tasks
                    </NavLink>
                    <NavLink to="/activities" className={({ isActive }) => `nav-item ${isActive ? 'active' : ''}`}>
                        <Activity className="nav-icon" />
                        Activities
                    </NavLink>
                </div>
            </nav>

            <div className="sidebar-footer">
                <div className="user-info">
                    <div className="user-avatar">{initials}</div>
                    <div style={{ flex: 1, minWidth: 0 }}>
                        <div className="user-name text-truncate">{user?.fullName}</div>
                        <div className="user-role">{(user?.role || '').replace('ROLE_', '')}</div>
                    </div>
                    <LogOut size={18} style={{ cursor: 'pointer', color: 'var(--text-muted)' }} onClick={handleLogout} />
                </div>
            </div>
        </aside>
    );
}
