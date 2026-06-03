import { ReactNode } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { FiLogOut, FiUsers, FiUserPlus, FiHome } from 'react-icons/fi';

interface LayoutProps {
  children: ReactNode;
  title: string;
}

export default function Layout({ children, title }: LayoutProps) {
  const navigate = useNavigate();
  const userStr = localStorage.getItem('user');
  let user: any = null;
  if (userStr) {
    try { user = JSON.parse(userStr); } catch (e) {}
  }

  const logout = () => {
    localStorage.clear();
    window.location.href = '/login';
  };

  return (
    <div className="flex h-screen bg-gray-50">
      {/* Sidebar */}
      <aside className="w-64 bg-slate-900 text-white flex flex-col">
        <div className="p-4 bg-slate-950">
          <h2 className="text-2xl font-bold text-center">Victus CAF</h2>
        </div>
        <nav className="flex-1 p-4 space-y-2">
          {user?.rol === 'ADMINISTRADOR' && (
            <>
              <Link to="/admin" className="flex items-center space-x-2 p-2 hover:bg-slate-800 rounded transition">
                <FiUsers /> <span>Usuarios Sistema</span>
              </Link>
              <Link to="/gestion-clientes" className="flex items-center space-x-2 p-2 hover:bg-slate-800 rounded transition">
                <FiUserPlus /> <span>Clientes (Mensual/Diario/EPS)</span>
              </Link>
            </>
          )}
          {user?.rol === 'SECRETARIA' && (
            <>
              <Link to="/secretaria" className="flex items-center space-x-2 p-2 hover:bg-slate-800 rounded transition">
                <FiHome /> <span>Dashboard</span>
              </Link>
              <Link to="/gestion-clientes" className="flex items-center space-x-2 p-2 hover:bg-slate-800 rounded transition">
                <FiUserPlus /> <span>Clientes (Mensual/Diario/EPS)</span>
              </Link>
            </>
          )}
          {user?.rol === 'ENTRENADOR' && (
            <Link to="/entrenador" className="flex items-center space-x-2 p-2 hover:bg-slate-800 rounded transition">
              <FiHome /> <span>Dashboard</span>
            </Link>
          )}
          {user?.rol === 'CLIENTE' && (
            <Link to="/cliente" className="flex items-center space-x-2 p-2 hover:bg-slate-800 rounded transition">
              <FiHome /> <span>Dashboard</span>
            </Link>
          )}
        </nav>
        <div className="p-4 border-t border-slate-800">
          <p className="text-sm text-slate-400">Logueado como:</p>
          <p className="font-semibold truncate">{user?.nombreCompleto || 'Usuario'}</p>
        </div>
      </aside>

      {/* Main Content */}
      <main className="flex-1 flex flex-col overflow-hidden">
        <header className="bg-white h-16 flex items-center justify-between px-8 border-b">
          <h1 className="text-xl font-semibold text-gray-800">{title}</h1>
          <button 
            onClick={logout}
            className="flex items-center space-x-2 text-gray-600 hover:text-red-600 transition"
          >
            <FiLogOut /> <span>Cerrar Sesión</span>
          </button>
        </header>
        <div className="flex-1 overflow-auto p-8">
          {children}
        </div>
      </main>
    </div>
  );
}
