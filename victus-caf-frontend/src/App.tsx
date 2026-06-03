import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import Login from './pages/Login';
import ListaClientesMensuales from './pages/ListaClientesMensuales';

const Dashboard = () => {
    const user = JSON.parse(localStorage.getItem('user') || '{}');
    return (
        <div>
            <h1>Bienvenido, {user.nombreCompleto || 'Usuario'}</h1>
            <p>Rol: {user.rol}</p>
            <nav>
                <ul>
                    <li><a href="/clientes">Clientes Mensuales</a></li>
                </ul>
            </nav>
            <button onClick={() => {
                localStorage.clear();
                window.location.href = '/login';
            }}>Cerrar sesión</button>
        </div>
    );
};

function App() {
    const token = localStorage.getItem('token');
    return (
        <BrowserRouter>
            <Routes>
                <Route path="/login" element={<Login />} />
                <Route path="/" element={token ? <Dashboard /> : <Navigate to="/login" />} />
                <Route path="/clientes" element={token ? <ListaClientesMensuales /> : <Navigate to="/login" />} />
            </Routes>
        </BrowserRouter>
    );
}

export default App;