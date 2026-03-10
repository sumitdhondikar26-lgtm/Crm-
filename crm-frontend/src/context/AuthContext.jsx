import { createContext, useContext, useState, useEffect } from 'react';
import { authAPI } from '../services/api';

const AuthContext = createContext(null);

export function AuthProvider({ children }) {
    const [user, setUser] = useState(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const saved = localStorage.getItem('crm_user');
        if (saved) {
            try { setUser(JSON.parse(saved)); } catch { /* ignore */ }
        }
        setLoading(false);
    }, []);

    const login = async (username, password) => {
        const data = await authAPI.login({ username, password });
        localStorage.setItem('crm_token', data.token);
        localStorage.setItem('crm_user', JSON.stringify(data));
        setUser(data);
        return data;
    };

    const logout = () => {
        localStorage.removeItem('crm_token');
        localStorage.removeItem('crm_user');
        setUser(null);
    };

    return (
        <AuthContext.Provider value={{ user, login, logout, loading }}>
            {children}
        </AuthContext.Provider>
    );
}

export const useAuth = () => useContext(AuthContext);
