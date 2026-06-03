import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import Login from './pages/Login';
import AdminDashboard from './pages/AdminDashboard';
import SecretariaDashboard from './pages/SecretariaDashboard';
import EntrenadorDashboard from './pages/EntrenadorDashboard';
import ClienteDashboard from './pages/ClienteDashboard';
import { PrivateRoute } from './components/PrivateRoute';

function App() {
  const userStr = localStorage.getItem('user');
  let rol = '';
  if (userStr) {
    try {
      const user = JSON.parse(userStr);
      rol = user.rol;
    } catch (e) {}
  }

  // Redirigir desde la raíz según el rol (opcional)
  const getDefaultRoute = () => {
    if (rol === 'ADMINISTRADOR') return <Navigate to="/admin" />;
    if (rol === 'SECRETARIA') return <Navigate to="/secretaria" />;
    if (rol === 'ENTRENADOR') return <Navigate to="/entrenador" />;
    if (rol) return <Navigate to="/cliente" />;
    return <Navigate to="/login" />;
  };

  return (
    <BrowserRouter>
      <Routes>
        <Route path="/login" element={<Login />} />
        <Route
          path="/admin"
          element={
            <PrivateRoute allowedRoles={['ADMINISTRADOR']}>
              <AdminDashboard />
            </PrivateRoute>
          }
        />
        <Route
          path="/secretaria"
          element={
            <PrivateRoute allowedRoles={['SECRETARIA']}>
              <SecretariaDashboard />
            </PrivateRoute>
          }
        />
        <Route
          path="/entrenador"
          element={
            <PrivateRoute allowedRoles={['ENTRENADOR']}>
              <EntrenadorDashboard />
            </PrivateRoute>
          }
        />
        <Route
          path="/cliente"
          element={
            <PrivateRoute allowedRoles={['CLIENTE']}>
              <ClienteDashboard />
            </PrivateRoute>
          }
        />
        <Route path="/" element={getDefaultRoute()} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;