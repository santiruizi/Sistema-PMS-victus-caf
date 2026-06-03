export default function ClienteDashboard() {
  const logout = () => {
    localStorage.clear();
    window.location.href = '/login';
  };
  return (
    <div>
      <h1>Portal del Cliente</h1>
      <button onClick={logout}>Cerrar sesión</button>
      <p>Aquí podrás ver tu membresía, progreso, etc.</p>
    </div>
  );
}