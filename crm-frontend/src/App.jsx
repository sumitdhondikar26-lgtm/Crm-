import { BrowserRouter, Routes, Route, Navigate, Outlet } from 'react-router-dom';
import { AuthProvider, useAuth } from './context/AuthContext';
import Sidebar from './components/Sidebar';
import LoginPage from './pages/LoginPage';
import DashboardPage from './pages/DashboardPage';
import CustomersPage from './pages/CustomersPage';
import DealsPage from './pages/DealsPage';
import TasksPage from './pages/TasksPage';
import ActivitiesPage from './pages/ActivitiesPage';

function ProtectedLayout() {
  const { user, loading } = useAuth();
  if (loading) return <div className="loading-spinner"><div className="spinner" /></div>;
  if (!user) return <Navigate to="/" replace />;
  return (
    <div className="app-layout">
      <Sidebar />
      <main className="main-content">
        <Outlet />
      </main>
    </div>
  );
}

function LoginRoute() {
  const { user, loading } = useAuth();
  if (loading) return <div className="loading-spinner"><div className="spinner" /></div>;
  if (user) return <Navigate to="/dashboard" replace />;
  return <LoginPage />;
}

export default function App() {
  return (
    <BrowserRouter>
      <AuthProvider>
        <Routes>
          <Route path="/" element={<LoginRoute />} />
          <Route element={<ProtectedLayout />}>
            <Route path="/dashboard" element={<DashboardPage />} />
            <Route path="/customers" element={<CustomersPage />} />
            <Route path="/deals" element={<DealsPage />} />
            <Route path="/tasks" element={<TasksPage />} />
            <Route path="/activities" element={<ActivitiesPage />} />
          </Route>
          <Route path="*" element={<Navigate to="/" replace />} />
        </Routes>
      </AuthProvider>
    </BrowserRouter>
  );
}
