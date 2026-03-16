import { useState } from 'react';

const ADMIN_CREDENTIALS = {
  username: 'admin',
  password: 'admin123'
};

export default function AdminLogin({ onNavigate }) {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');

  const handleSubmit = (e) => {
    e.preventDefault();
    setError('');

    if (username === ADMIN_CREDENTIALS.username && password === ADMIN_CREDENTIALS.password) {
      onNavigate('admin');
    } else {
      setError('Credenciales incorrectas. Intenta de nuevo.');
    }
  };

  return (
    <div className="min-h-screen bg-[#050d1a] flex items-center justify-center px-4">
      <div className="w-full max-w-md">
        {/* Logo/Title */}
        <div className="text-center mb-8">
          <h1 className="text-4xl font-bold text-white font-['Cormorant_Garamond',serif] tracking-widest">
            FAST
          </h1>
          <p className="text-teal-400/50 text-sm mt-2">Panel de Administración</p>
        </div>

        {/* Login Form */}
        <div className="bg-gradient-to-br from-[#0d2137]/80 to-[#071525]/80 border border-teal-500/15 rounded-2xl p-8">
          <h2 className="text-2xl font-bold text-white mb-6 text-center">Iniciar Sesión</h2>
          
          <form onSubmit={handleSubmit} className="space-y-5">
            <div>
              <label className="block text-teal-400/60 text-sm mb-2">Usuario</label>
              <input
                type="text"
                value={username}
                onChange={(e) => setUsername(e.target.value)}
                className="w-full bg-[#050d1a]/50 border border-teal-500/20 rounded-lg px-4 py-3 text-white placeholder-teal-100/30 focus:outline-none focus:border-teal-400/50 transition-colors"
                placeholder="Ingresa tu usuario"
                required
              />
            </div>

            <div>
              <label className="block text-teal-400/60 text-sm mb-2">Contraseña</label>
              <input
                type="password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                className="w-full bg-[#050d1a]/50 border border-teal-500/20 rounded-lg px-4 py-3 text-white placeholder-teal-100/30 focus:outline-none focus:border-teal-400/50 transition-colors"
                placeholder="Ingresa tu contraseña"
                required
              />
            </div>

            {error && (
              <div className="bg-red-500/10 border border-red-500/25 text-red-400 text-sm px-4 py-2 rounded-lg">
                {error}
              </div>
            )}

            <button
              type="submit"
              className="w-full bg-gradient-to-r from-teal-500 to-cyan-500 text-white font-semibold py-3 rounded-lg transition-all duration-300 hover:shadow-[0_0_20px_rgba(20,184,166,0.4)]"
            >
              Ingresar
            </button>
          </form>

          <div className="mt-6 text-center">
            <button
              onClick={() => onNavigate('home')}
              className="text-teal-400/50 text-sm hover:text-teal-300 transition-colors"
            >
              ← Volver al inicio
            </button>
          </div>
        </div>

        {/* Demo credentials hint */}
        <div className="mt-6 text-center">
          <p className="text-teal-100/30 text-xs">
            Demo: admin / admin123
          </p>
        </div>
      </div>
    </div>
  );
}
