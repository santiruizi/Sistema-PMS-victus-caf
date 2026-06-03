import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../services/api';

export default function Login() {
  const [documento, setDocumento] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
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
      // Redirigir según el rol
      if (rol === 'ADMINISTRADOR') navigate('/admin');
      else if (rol === 'SECRETARIA') navigate('/secretaria');
      else if (rol === 'ENTRENADOR') navigate('/entrenador');
      else navigate('/cliente');
    } catch (err) {
      setError('Credenciales incorrectas');
    }
  };

  return (
    <div style={{ maxWidth: '400px', margin: '100px auto', padding: '20px', border: '1px solid #ccc' }}>
      <h2>Iniciar Sesión - Victus CAF</h2>
      {error && <p style={{ color: 'red' }}>{error}</p>}
      <form onSubmit={handleSubmit}>
        <div>
          <label>Número de documento:</label>
          <input
            type="number"
            value={documento}
            onChange={(e) => setDocumento(e.target.value)}
            required
            style={{ width: '100%', padding: '8px', margin: '8px 0' }}
          />
        </div>
        <div>
          <label>Contraseña:</label>
          <input
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
            style={{ width: '100%', padding: '8px', margin: '8px 0' }}
          />
        </div>
        <button type="submit" style={{ width: '100%', padding: '10px', background: 'blue', color: 'white' }}>
          Ingresar
        </button>
      </form>
    </div>
  );
}