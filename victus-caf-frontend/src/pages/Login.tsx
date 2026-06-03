import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../services/api';
import { isServerUnreachable, SERVER_UNREACHABLE_MESSAGE } from '../utils/apiError';
import toast from 'react-hot-toast';

export default function Login() {
  const [documento, setDocumento] = useState('');
  const [password, setPassword] = useState('');
  const navigate = useNavigate();

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      const response = await api.post('/auth/login', {
        numeroDocumento: parseInt(documento),
        contrasena: password,
      });
      const { token, id, numeroDocumento: doc, nombreCompleto, rol } = response.data;
      localStorage.setItem('token', token);
      localStorage.setItem('user', JSON.stringify({ id, numeroDocumento: doc, nombreCompleto, rol }));
      
      toast.success('¡Bienvenido!');
      
      if (rol === 'ADMINISTRADOR') navigate('/admin');
      else if (rol === 'SECRETARIA') navigate('/secretaria');
      else if (rol === 'ENTRENADOR') navigate('/entrenador');
      else navigate('/cliente');
    } catch (err) {
      if (isServerUnreachable(err)) {
        toast.error(SERVER_UNREACHABLE_MESSAGE);
      } else {
        toast.error('Credenciales incorrectas');
      }
    }
  };

  return (
    <div className="min-h-screen bg-slate-50 flex flex-col justify-center py-12 sm:px-6 lg:px-8">
      <div className="sm:mx-auto sm:w-full sm:max-w-md">
        <h2 className="mt-6 text-center text-3xl font-extrabold text-gray-900">
          Victus CAF
        </h2>
        <p className="mt-2 text-center text-sm text-gray-600">
          Inicia sesión para acceder a tu panel
        </p>
      </div>

      <div className="mt-8 sm:mx-auto sm:w-full sm:max-w-md">
        <div className="bg-white py-8 px-4 shadow-xl sm:rounded-lg sm:px-10 border border-gray-100">
          <form className="space-y-6" onSubmit={handleSubmit}>
            <div>
              <label className="block text-sm font-medium text-gray-700">
                Número de Documento
              </label>
              <div className="mt-1">
                <input
                  type="number"
                  value={documento}
                  onChange={(e) => setDocumento(e.target.value)}
                  required
                  className="appearance-none block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm placeholder-gray-400 focus:outline-none focus:ring-blue-500 focus:border-blue-500 sm:text-sm"
                  placeholder="Ej. 123456789"
                />
              </div>
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700">
                Contraseña
              </label>
              <div className="mt-1">
                <input
                  type="password"
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  required
                  className="appearance-none block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm placeholder-gray-400 focus:outline-none focus:ring-blue-500 focus:border-blue-500 sm:text-sm"
                  placeholder="••••••••"
                />
              </div>
            </div>

            <div>
              <button
                type="submit"
                className="w-full flex justify-center py-2 px-4 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-blue-600 hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500 transition-colors"
              >
                Ingresar
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
}