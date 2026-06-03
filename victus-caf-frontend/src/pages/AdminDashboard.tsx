import { useEffect, useState } from 'react';
import { UsuarioSistema, listarUsuarios, crearUsuario, actualizarUsuario, desactivarUsuario } from '../services/usuarioSistemaService';

export default function AdminDashboard() {
  const [usuarios, setUsuarios] = useState<UsuarioSistema[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [modo, setModo] = useState<'list' | 'create' | 'edit'>('list');
  const [currentUser, setCurrentUser] = useState<UsuarioSistema | null>(null);
  const [formData, setFormData] = useState({
    tipoDeDocumento: '',
    numeroDeDocumento: 0,
    nombreCompleto: '',
    telefono: 0,
    correoElectronico: '',
    rol: 'ADMINISTRADOR' as const,
    contrasena: '',
  });

  const cargarUsuarios = async () => {
    setLoading(true);
    try {
      const res = await listarUsuarios();
      setUsuarios(res.data);
    } catch (err) {
      setError('Error al cargar usuarios');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    cargarUsuarios();
  }, []);

  const handleCreate = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      await crearUsuario(formData);
      setModo('list');
      cargarUsuarios();
      setFormData({
        tipoDeDocumento: '',
        numeroDeDocumento: 0,
        nombreCompleto: '',
        telefono: 0,
        correoElectronico: '',
        rol: 'ADMINISTRADOR',
        contrasena: '',
      });
    } catch (err) {
      setError('Error al crear usuario');
    }
  };

  const handleUpdate = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!currentUser) return;
    try {
      await actualizarUsuario(currentUser.idUsuarioSistema, formData);
      setModo('list');
      cargarUsuarios();
      setCurrentUser(null);
      setFormData({
        tipoDeDocumento: '',
        numeroDeDocumento: 0,
        nombreCompleto: '',
        telefono: 0,
        correoElectronico: '',
        rol: 'ADMINISTRADOR',
        contrasena: '',
      });
    } catch (err) {
      setError('Error al actualizar usuario');
    }
  };

  const handleDelete = async (id: number) => {
    if (window.confirm('¿Desactivar este usuario?')) {
      try {
        await desactivarUsuario(id);
        cargarUsuarios();
      } catch (err) {
        setError('Error al desactivar usuario');
      }
    }
  };

  const editUser = (user: UsuarioSistema) => {
    setCurrentUser(user);
    setFormData({
      tipoDeDocumento: user.tipoDeDocumento,
      numeroDeDocumento: user.numeroDeDocumento,
      nombreCompleto: user.nombreCompleto,
      telefono: user.telefono,
      correoElectronico: user.correoElectronico,
      rol: user.rol,
      contrasena: '',
    });
    setModo('edit');
  };

  const logout = () => {
    localStorage.clear();
    window.location.href = '/login';
  };

  if (loading) return <div>Cargando...</div>;
  if (error) return <div style={{ color: 'red' }}>{error}</div>;

  return (
    <div>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <h1>Panel de Administrador</h1>
        <button onClick={logout}>Cerrar sesión</button>
      </div>
      <hr />
      <div>
        <button onClick={() => { setModo('list'); setError(''); }}>Listar usuarios</button>
        <button onClick={() => { setModo('create'); setError(''); }}>Crear usuario</button>
      </div>

      {modo === 'list' && (
        <table border={1} cellPadding={8} style={{ width: '100%', borderCollapse: 'collapse', marginTop: '16px' }}>
          <thead>
            <tr>
              <th>ID</th>
              <th>Documento</th>
              <th>Nombre</th>
              <th>Correo</th>
              <th>Rol</th>
              <th>Estado</th>
              <th>Acciones</th>
            </tr>
          </thead>
          <tbody>
            {usuarios.map((user) => (
              <tr key={user.idUsuarioSistema}>
                <td>{user.idUsuarioSistema}</td>
                <td>{user.numeroDeDocumento}</td>
                <td>{user.nombreCompleto}</td>
                <td>{user.correoElectronico}</td>
                <td>{user.rol}</td>
                <td>{user.estado ? 'Activo' : 'Inactivo'}</td>
                <td>
                  <button onClick={() => editUser(user)}>Editar</button>
                  <button onClick={() => handleDelete(user.idUsuarioSistema)} disabled={!user. estado}>Desactivar</button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      )}

      {(modo === 'create' || modo === 'edit') && (
        <form onSubmit={modo === 'create' ? handleCreate : handleUpdate} style={{ marginTop: '16px', border: '1px solid #ccc', padding: '16px' }}>
          <h2>{modo === 'create' ? 'Crear nuevo usuario' : 'Editar usuario'}</h2>
          <div>
            <label>Tipo documento:</label>
            <input
              type="text"
              value={formData.tipoDeDocumento}
              onChange={(e) => setFormData({ ...formData, tipoDeDocumento: e.target.value })}
              required
            />
          </div>
          <div>
            <label>Número documento:</label>
            <input
              type="number"
              value={formData.numeroDeDocumento}
              onChange={(e) => setFormData({ ...formData, numeroDeDocumento: parseInt(e.target.value) })}
              required
            />
          </div>
          <div>
            <label>Nombre completo:</label>
            <input
              type="text"
              value={formData.nombreCompleto}
              onChange={(e) => setFormData({ ...formData, nombreCompleto: e.target.value })}
              required
            />
          </div>
          <div>
            <label>Teléfono:</label>
            <input
              type="number"
              value={formData.telefono}
              onChange={(e) => setFormData({ ...formData, telefono: parseInt(e.target.value) })}
              required
            />
          </div>
          <div>
            <label>Correo electrónico:</label>
            <input
              type="email"
              value={formData.correoElectronico}
              onChange={(e) => setFormData({ ...formData, correoElectronico: e.target.value })}
              required
            />
          </div>
          <div>
            <label>Rol:</label>
            <select
              value={formData.rol}
              onChange={(e) => setFormData({ ...formData, rol: e.target.value as typeof formData.rol })}
            >
              <option value="ADMINISTRADOR">Administrador</option>
              <option value="ENTRENADOR">Entrenador</option>
              <option value="SECRETARIA">Secretaria</option>
            </select>
          </div>
          {modo === 'create' && (
            <div>
              <label>Contraseña:</label>
              <input
                type="password"
                value={formData.contrasena}
                onChange={(e) => setFormData({ ...formData, contrasena: e.target.value })}
                required
              />
            </div>
          )}
          <div style={{ marginTop: '8px' }}>
            <button type="submit">{modo === 'create' ? 'Crear' : 'Guardar cambios'}</button>
            <button type="button" onClick={() => setModo('list')}>Cancelar</button>
          </div>
        </form>
      )}
    </div>
  );
}