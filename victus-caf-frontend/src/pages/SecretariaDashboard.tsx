import { useEffect, useState } from 'react';
import type { Secretaria } from '../services/secretariaService';
import {
  listarSecretarias,
  crearSecretaria,
  actualizarSecretaria,
  desactivarSecretaria,
  reactivarSecretaria,
  buscarSecretariaPorDocumento,
  registrarIngreso,
  type Asistencia,
} from '../services/secretariaService';
import Layout from '../components/Layout';
import DocumentTypeBar from '../components/DocumentTypeBar';
import { isServerUnreachable, SERVER_UNREACHABLE_MESSAGE } from '../utils/apiError';
import toast from 'react-hot-toast';
import { FiEdit2, FiTrash2, FiPlus, FiList, FiSearch, FiRefreshCw, FiLogIn } from 'react-icons/fi';
import axios from 'axios';

function getErrorMessage(err: unknown, fallback: string): string {
  if (axios.isAxiosError(err) && err.response?.data?.message) {
    return err.response.data.message as string;
  }
  return fallback;
}

export default function SecretariaDashboard() {
  const [secretarias, setSecretarias] = useState<Secretaria[]>([]);
  const [loading, setLoading] = useState(true);
  const [modo, setModo] = useState<'list' | 'create' | 'edit' | 'ingreso'>('list');
  const [currentSecretaria, setCurrentSecretaria] = useState<Secretaria | null>(null);
  const [busqueda, setBusqueda] = useState('');
  const [documentoIngreso, setDocumentoIngreso] = useState('');

  const [formData, setFormData] = useState({
    tipoDeDocumento: 'CC',
    numeroDeDocumento: 0,
    nombreCompleto: '',
    telefono: 0,
    correoElectronico: '',
    turno: '',
    contrasena: '',
  });

  const cargarSecretarias = async () => {
    setLoading(true);
    try {
      const data = await listarSecretarias();
      const filtradas = data.filter((u) => u.rol === 'SECRETARIA');
      setSecretarias(filtradas);
    } catch (err) {
      toast.error(
        isServerUnreachable(err) ? SERVER_UNREACHABLE_MESSAGE : 'Error al cargar secretarias'
      );
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    cargarSecretarias();
  }, []);

  const secretariasFiltradas = (() => {
    const q = busqueda.trim().toLowerCase();
    if (!q) return secretarias;
    return secretarias.filter(
      (s) =>
        s.nombreCompleto.toLowerCase().includes(q) ||
        String(s.numeroDeDocumento).includes(q) ||
        s.tipoDeDocumento.toLowerCase().includes(q)
    );
  })();

  const handleBuscarDocumento = async () => {
    const doc = parseInt(busqueda, 10);
    if (!doc || Number.isNaN(doc)) {
      toast.error('Ingresa un número de documento válido');
      return;
    }
    try {
      const encontrada = await buscarSecretariaPorDocumento(doc);
      setSecretarias((prev) =>
        prev.some((s) => s.idUsuarioSistema === encontrada.idUsuarioSistema)
          ? prev
          : [...prev, encontrada]
      );
      setBusqueda(String(doc));
      toast.success(`Secretaria encontrada: ${encontrada.nombreCompleto}`);
    } catch (err) {
      toast.error(getErrorMessage(err, 'Secretaria no encontrada'));
    }
  };

  const handleCreate = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!formData.contrasena) {
      toast.error('La contraseña es requerida');
      return;
    }
    try {
      await crearSecretaria({
        ...formData,
        contrasena: formData.contrasena,
      });
      toast.success('Secretaria creada exitosamente');
      setModo('list');
      cargarSecretarias();
      resetForm();
    } catch (err) {
      toast.error(getErrorMessage(err, 'Error al crear secretaria'));
    }
  };

  const handleUpdate = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!currentSecretaria) return;
    try {
      await actualizarSecretaria(currentSecretaria.idUsuarioSistema, {
        tipoDeDocumento: formData.tipoDeDocumento,
        numeroDeDocumento: formData.numeroDeDocumento,
        nombreCompleto: formData.nombreCompleto,
        telefono: formData.telefono,
        correoElectronico: formData.correoElectronico,
        turno: formData.turno,
        contrasena: formData.contrasena || undefined,
      });
      toast.success('Secretaria actualizada exitosamente');
      setModo('list');
      cargarSecretarias();
      setCurrentSecretaria(null);
      resetForm();
    } catch (err) {
      toast.error(getErrorMessage(err, 'Error al actualizar secretaria'));
    }
  };

  const handleDelete = async (id: number) => {
    if (!window.confirm('¿Estás seguro de inactivar esta secretaria?')) return;
    try {
      await desactivarSecretaria(id);
      toast.success('Secretaria inactivada');
      cargarSecretarias();
    } catch (err) {
      toast.error(getErrorMessage(err, 'Error al inactivar secretaria'));
    }
  };

  const handleReactivar = async (id: number) => {
    try {
      await reactivarSecretaria(id);
      toast.success('Secretaria reactivada');
      cargarSecretarias();
    } catch (err) {
      toast.error(getErrorMessage(err, 'Error al reactivar secretaria'));
    }
  };

  const handleRegistrarIngreso = async (e: React.FormEvent) => {
    e.preventDefault();
    const doc = parseInt(documentoIngreso, 10);
    if (!doc || Number.isNaN(doc)) {
      toast.error('Ingresa un número de documento válido');
      return;
    }
    try {
      const asistencia = await registrarIngreso(doc);
      toast.success(`Ingreso registrado para ${asistencia.usuario?.nombreCompleto || 'cliente'} a las ${asistencia.horaIngreso}`);
      setDocumentoIngreso('');
      setModo('list');
    } catch (err) {
      toast.error(getErrorMessage(err, 'Error al registrar ingreso'));
    }
  };

  const editSecretaria = (s: Secretaria) => {
    setCurrentSecretaria(s);
    setFormData({
      tipoDeDocumento: s.tipoDeDocumento,
      numeroDeDocumento: s.numeroDeDocumento,
      nombreCompleto: s.nombreCompleto,
      telefono: s.telefono,
      correoElectronico: s.correoElectronico,
      turno: s.turno || '',
      contrasena: '',
    });
    setModo('edit');
  };

  const resetForm = () => {
    setFormData({
      tipoDeDocumento: 'CC',
      numeroDeDocumento: 0,
      nombreCompleto: '',
      telefono: 0,
      correoElectronico: '',
      turno: '',
      contrasena: '',
    });
  };

  if (loading && secretarias.length === 0) {
    return (
      <Layout title="Cargando...">
        <div className="flex items-center justify-center h-full text-gray-500">Cargando...</div>
      </Layout>
    );
  }

  return (
    <Layout title="Panel de Secretaria">
      <div className="mb-6 flex flex-wrap gap-3">
        <button
          onClick={() => {
            setModo('list');
            resetForm();
          }}
          className={`flex items-center space-x-2 px-4 py-2 rounded shadow-sm transition ${modo === 'list' ? 'bg-blue-600 text-white' : 'bg-white text-gray-700 hover:bg-gray-50 border'}`}
        >
          <FiList /> <span>Listar Secretarias</span>
        </button>
        <button
          onClick={() => {
            setModo('create');
            resetForm();
          }}
          className={`flex items-center space-x-2 px-4 py-2 rounded shadow-sm transition ${modo === 'create' ? 'bg-blue-600 text-white' : 'bg-white text-gray-700 hover:bg-gray-50 border'}`}
        >
          <FiPlus /> <span>Crear Secretaria</span>
        </button>
        <button
          onClick={() => {
            setModo('ingreso');
            setDocumentoIngreso('');
          }}
          className={`flex items-center space-x-2 px-4 py-2 rounded shadow-sm transition ${modo === 'ingreso' ? 'bg-green-600 text-white' : 'bg-white text-gray-700 hover:bg-gray-50 border'}`}
        >
          <FiLogIn /> <span>Registrar Ingreso</span>
        </button>
      </div>

      {modo === 'list' && (
        <>
          <div className="mb-4 flex flex-wrap gap-2">
            <div className="relative flex-1 min-w-[200px]">
              <FiSearch className="absolute left-3 top-2.5 text-gray-400" />
              <input
                type="text"
                placeholder="Buscar por documento o nombre..."
                value={busqueda}
                onChange={(e) => setBusqueda(e.target.value)}
                className="w-full pl-10 pr-3 py-2 border border-gray-300 rounded-md shadow-sm focus:ring-blue-500 focus:border-blue-500"
              />
            </div>
            <button
              type="button"
              onClick={handleBuscarDocumento}
              className="px-4 py-2 bg-slate-800 text-white rounded-md hover:bg-slate-700 text-sm font-medium"
            >
              Buscar
            </button>
          </div>

          <div className="bg-white shadow-sm rounded-lg overflow-hidden border border-gray-200">
            <div className="overflow-x-auto">
              <table className="min-w-full divide-y divide-gray-200">
                <thead className="bg-gray-50">
                  <tr>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Documento</th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Nombre</th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Correo</th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Turno</th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Estado</th>
                    <th className="px-6 py-3 text-right text-xs font-medium text-gray-500 uppercase">Acciones</th>
                  </tr>
                </thead>
                <tbody className="bg-white divide-y divide-gray-200">
                  {secretariasFiltradas.length === 0 ? (
                    <tr>
                      <td colSpan={6} className="px-6 py-8 text-center text-gray-500">
                        No hay secretarias que coincidan con la búsqueda
                      </td>
                    </tr>
                  ) : (
                    secretariasFiltradas.map((s) => (
                      <tr key={s.idUsuarioSistema} className="hover:bg-gray-50">
                        <td className="px-6 py-4 text-sm text-gray-900">
                          {s.tipoDeDocumento} {s.numeroDeDocumento}
                        </td>
                        <td className="px-6 py-4 text-sm text-gray-900">{s.nombreCompleto}</td>
                        <td className="px-6 py-4 text-sm text-gray-500">{s.correoElectronico}</td>
                        <td className="px-6 py-4 text-sm text-gray-600">{s.turno || '-'}</td>
                        <td className="px-6 py-4 text-sm">
                          {s.estado ? (
                            <span className="px-2 text-xs font-semibold rounded-full bg-green-100 text-green-800">
                              Activa
                            </span>
                          ) : (
                            <span className="px-2 text-xs font-semibold rounded-full bg-red-100 text-red-800">
                              Inactiva
                            </span>
                          )}
                        </td>
                        <td className="px-6 py-4 text-right text-sm font-medium space-x-2">
                          <button
                            onClick={() => editSecretaria(s)}
                            className="text-blue-600 hover:text-blue-900 inline-flex"
                            title="Actualizar"
                          >
                            <FiEdit2 size={18} />
                          </button>
                          {s.estado ? (
                            <button
                              onClick={() => handleDelete(s.idUsuarioSistema)}
                              className="text-red-600 hover:text-red-900 inline-flex"
                              title="Inactivar"
                            >
                              <FiTrash2 size={18} />
                            </button>
                          ) : (
                            <button
                              onClick={() => handleReactivar(s.idUsuarioSistema)}
                              className="text-green-600 hover:text-green-900 inline-flex"
                              title="Reactivar"
                            >
                              <FiRefreshCw size={18} />
                            </button>
                          )}
                        </td>
                      </tr>
                    ))
                  )}
                </tbody>
              </table>
            </div>
          </div>
        </>
      )}

      {(modo === 'create' || modo === 'edit') && (
        <div className="bg-white p-8 rounded-lg shadow-sm border border-gray-200 max-w-3xl">
          <h2 className="text-xl font-bold mb-6 text-gray-800">
            {modo === 'create' ? 'Crear Nueva Secretaria' : 'Actualizar Secretaria'}
          </h2>
          <form onSubmit={modo === 'create' ? handleCreate : handleUpdate} className="grid grid-cols-1 md:grid-cols-2 gap-6">
            <div className="md:col-span-2">
              <label className="block text-sm font-medium text-gray-700 mb-2">Tipo de Documento</label>
              <DocumentTypeBar
                value={formData.tipoDeDocumento}
                onChange={(v) => setFormData({ ...formData, tipoDeDocumento: v })}
              />
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">Número de Documento</label>
              <input
                type="number"
                value={formData.numeroDeDocumento || ''}
                onChange={(e) =>
                  setFormData({ ...formData, numeroDeDocumento: parseInt(e.target.value, 10) || 0 })
                }
                required
                disabled={modo === 'edit'}
                className="w-full px-3 py-2 border border-gray-300 rounded-md disabled:bg-gray-100"
              />
            </div>
            <div className="md:col-span-2">
              <label className="block text-sm font-medium text-gray-700 mb-1">Nombre Completo</label>
              <input
                type="text"
                value={formData.nombreCompleto}
                onChange={(e) => setFormData({ ...formData, nombreCompleto: e.target.value })}
                required
                className="w-full px-3 py-2 border border-gray-300 rounded-md"
              />
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">Teléfono</label>
              <input
                type="number"
                value={formData.telefono || ''}
                onChange={(e) => setFormData({ ...formData, telefono: parseInt(e.target.value, 10) || 0 })}
                required
                className="w-full px-3 py-2 border border-gray-300 rounded-md"
              />
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">Correo Electrónico</label>
              <input
                type="email"
                value={formData.correoElectronico}
                onChange={(e) => setFormData({ ...formData, correoElectronico: e.target.value })}
                required
                className="w-full px-3 py-2 border border-gray-300 rounded-md"
              />
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">Turno</label>
              <select
                value={formData.turno}
                onChange={(e) => setFormData({ ...formData, turno: e.target.value })}
                className="w-full px-3 py-2 border border-gray-300 rounded-md bg-white"
              >
                <option value="">Seleccionar turno</option>
                <option value="MAÑANA">Mañana</option>
                <option value="TARDE">Tarde</option>
                <option value="NOCHE">Noche</option>
              </select>
            </div>
            <div className="md:col-span-2">
              <label className="block text-sm font-medium text-gray-700 mb-1">
                {modo === 'create' ? 'Contraseña' : 'Nueva contraseña (opcional)'}
              </label>
              <input
                type="password"
                value={formData.contrasena}
                onChange={(e) => setFormData({ ...formData, contrasena: e.target.value })}
                required={modo === 'create'}
                className="w-full px-3 py-2 border border-gray-300 rounded-md"
              />
            </div>

            <div className="md:col-span-2 flex justify-end space-x-3 mt-4">
              <button
                type="button"
                onClick={() => setModo('list')}
                className="px-4 py-2 border border-gray-300 rounded-md text-sm text-gray-700 bg-white hover:bg-gray-50"
              >
                Cancelar
              </button>
              <button type="submit" className="px-4 py-2 rounded-md text-sm text-white bg-blue-600 hover:bg-blue-700">
                {modo === 'create' ? 'Crear Secretaria' : 'Guardar Cambios'}
              </button>
            </div>
          </form>
        </div>
      )}

      {modo === 'ingreso' && (
        <div className="bg-white p-8 rounded-lg shadow-sm border border-gray-200 max-w-xl">
          <h2 className="text-xl font-bold mb-6 text-gray-800">Registrar Ingreso de Cliente</h2>
          <form onSubmit={handleRegistrarIngreso} className="space-y-6">
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">
                Número de Documento del Cliente
              </label>
              <input
                type="number"
                value={documentoIngreso}
                onChange={(e) => setDocumentoIngreso(e.target.value)}
                placeholder="Ej: 123456789"
                required
                className="w-full px-4 py-2 border border-gray-300 rounded-md focus:ring-green-500 focus:border-green-500 text-lg"
              />
            </div>

            <div className="flex justify-end space-x-3">
              <button
                type="button"
                onClick={() => setModo('list')}
                className="px-4 py-2 border border-gray-300 rounded-md text-sm text-gray-700 bg-white hover:bg-gray-50"
              >
                Cancelar
              </button>
              <button
                type="submit"
                className="px-4 py-2 rounded-md text-sm text-white bg-green-600 hover:bg-green-700 font-medium"
              >
                Registrar Ingreso
              </button>
            </div>
          </form>
        </div>
      )}
    </Layout>
  );
}