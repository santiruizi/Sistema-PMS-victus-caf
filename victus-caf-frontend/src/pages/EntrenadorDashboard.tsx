export default function EntrenadorDashboard() {
  const logout = () => {
    localStorage.clear();
    window.location.href = '/login';
  };
  return (
    <div>
      <h1>Panel de Entrenador</h1>
      <button onClick={logout}>Cerrar sesión</button>
      <p>Aquí podrás ver tus clientes, rutinas, etc.</p>
    </div>
  );
}