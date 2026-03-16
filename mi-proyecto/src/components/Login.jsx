import { useState } from 'react';
import { login } from '../services/auth';

export default function Login({ onNavigate }) {
  const [form, setForm] = useState({ usernameOrEmail: '', password: '', twoFactorCode: '' });
  const [showPass, setShowPass] = useState(false);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const handleChange = (e) => {
    setError('');
    setForm(f => ({ ...f, [e.target.name]: e.target.value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');
    
    try {
      const result = await login(form);
      
      // Verificar si se requiere 2FA
      if (result.twoFactorRequired) {
        // TODO: Implementar lógica para 2FA
        console.log('Se requiere autenticación de dos factores');
      } else {
        // Login exitoso
        onNavigate('catalog');
      }
    } catch (error) {
      setError(error.message || 'Correo/usuario o contraseña incorrectos');
    } finally {
      setLoading(false);
    }
  };

  const isValid = form.usernameOrEmail && form.password.length >= 6;

  return (
    <div className="min-h-screen bg-gradient-to-br from-[#050d1a] via-[#0a1e35] to-[#061525] flex items-center justify-center px-4 relative overflow-hidden">

      {/* Background glows */}
      <div className="absolute inset-0 pointer-events-none">
        <div className="absolute top-0 right-0 w-[600px] h-[600px] bg-teal-500/8 rounded-full blur-[130px]" />
        <div className="absolute bottom-0 left-0 w-[500px] h-[500px] bg-cyan-400/6 rounded-full blur-[120px]" />
        <div className="absolute top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2 w-[300px] h-[300px] bg-teal-600/5 rounded-full blur-[80px]" />
      </div>

      {/* Grid overlay */}
      <div className="absolute inset-0 opacity-[0.025]"
        style={{ backgroundImage: 'linear-gradient(#0ff 1px, transparent 1px), linear-gradient(90deg, #0ff 1px, transparent 1px)', backgroundSize: '60px 60px' }} />

      {/* Back button */}
      <button onClick={() => onNavigate('home')}
        className="absolute top-6 left-6 flex items-center gap-2 text-teal-400/50 hover:text-teal-300 text-sm transition-colors duration-300 z-10">
        <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M15 19l-7-7 7-7" />
        </svg>
        Volver al inicio
      </button>

      <div className="relative z-10 w-full max-w-md">

        {/* Card */}
        <div className="bg-gradient-to-br from-[#0d2137]/90 to-[#071525]/95 border border-teal-500/20 rounded-3xl p-8 backdrop-blur-md shadow-[0_0_80px_rgba(20,184,166,0.08)]">

          {/* Logo + header */}
          <div className="text-center mb-8">
            <div className="inline-flex items-center justify-center w-14 h-14 rounded-2xl bg-gradient-to-br from-teal-500/20 to-cyan-500/10 border border-teal-500/25 mb-4">
              <svg className="w-7 h-7 text-teal-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={1.5} d="M19 21V5a2 2 0 00-2-2H7a2 2 0 00-2 2v16m14 0h2m-2 0h-5m-9 0H3m2 0h5M9 7h1m-1 4h1m4-4h1m-1 4h1m-5 10v-5a1 1 0 011-1h2a1 1 0 011 1v5m-4 0h4" />
              </svg>
            </div>
            <h1 className="font-['Cormorant_Garamond',serif] text-3xl font-bold text-white tracking-wide">
              FAST
            </h1>
            <p className="text-teal-400/50 text-xs tracking-[0.25em] uppercase mt-1">Distribuidora de Medicamentos</p>
            <div className="mt-4 w-full h-px bg-gradient-to-r from-transparent via-teal-500/25 to-transparent" />
            <p className="text-teal-100/50 text-sm mt-4 font-light">
              Acceso exclusivo para clientes B2B registrados
            </p>
          </div>

          {/* Form */}
          <form onSubmit={handleSubmit} className="flex flex-col gap-4">

            {/* Email / Username */}
            <div>
              <label className="text-teal-400/60 text-xs tracking-wide mb-1.5 block">Usuario o correo</label>
              <div className="relative">
                <div className="absolute left-3.5 top-1/2 -translate-y-1/2 text-teal-500/40">
                  <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M3 8l7.89 5.26a2 2 0 002.22 0L21 8M5 19h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v10a2 2 0 002 2z" />
                  </svg>
                </div>
                <input
                  name="usernameOrEmail" type="text" value={form.usernameOrEmail} onChange={handleChange} required
                  placeholder="usuario o correo@empresa.com"
                  className="w-full bg-[#071525] border border-teal-500/20 rounded-xl pl-10 pr-4 py-3 text-sm text-teal-100 placeholder-teal-500/25 outline-none focus:border-teal-400/60 focus:shadow-[0_0_0_3px_rgba(20,184,166,0.08)] transition-all"
                />
              </div>
            </div>

            {/* Password */}
            <div>
              <div className="flex justify-between items-center mb-1.5">
                <label className="text-teal-400/60 text-xs tracking-wide">Contraseña</label>
                <button type="button" className="text-teal-400/50 hover:text-teal-300 text-xs transition-colors">
                  ¿Olvidaste tu contraseña?
                </button>
              </div>
              <div className="relative">
                <div className="absolute left-3.5 top-1/2 -translate-y-1/2 text-teal-500/40">
                  <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 15v2m-6 4h12a2 2 0 002-2v-6a2 2 0 00-2-2H6a2 2 0 00-2 2v6a2 2 0 002 2zm10-10V7a4 4 0 00-8 0v4h8z" />
                  </svg>
                </div>
                <input
                  name="password" type={showPass ? 'text' : 'password'} value={form.password} onChange={handleChange} required
                  placeholder="••••••••"
                  className="w-full bg-[#071525] border border-teal-500/20 rounded-xl pl-10 pr-11 py-3 text-sm text-teal-100 placeholder-teal-500/25 outline-none focus:border-teal-400/60 focus:shadow-[0_0_0_3px_rgba(20,184,166,0.08)] transition-all"
                />
                <button type="button" onClick={() => setShowPass(p => !p)}
                  className="absolute right-3.5 top-1/2 -translate-y-1/2 text-teal-500/40 hover:text-teal-300 transition-colors">
                  {showPass ? (
                    <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M13.875 18.825A10.05 10.05 0 0112 19c-4.478 0-8.268-2.943-9.543-7a9.97 9.97 0 011.563-3.029m5.858.908a3 3 0 114.243 4.243M9.878 9.878l4.242 4.242M9.88 9.88l-3.29-3.29m7.532 7.532l3.29 3.29M3 3l3.59 3.59m0 0A9.953 9.953 0 0112 5c4.478 0 8.268 2.943 9.543 7a10.025 10.025 0 01-4.132 5.411m0 0L21 21" />
                    </svg>
                  ) : (
                    <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M15 12a3 3 0 11-6 0 3 3 0 016 0z" />
                      <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z" />
                    </svg>
                  )}
                </button>
              </div>
            </div>

            {/* 2FA Code (optional) */}
            <div>
              <label className="text-teal-400/60 text-xs tracking-wide mb-1.5 block">
                Código 2FA <span className="text-teal-500/30">(opcional)</span>
              </label>
              <div className="relative">
                <div className="absolute left-3.5 top-1/2 -translate-y-1/2 text-teal-500/40">
                  <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 15v2m-6 4h12a2 2 0 002-2v-6a2 2 0 00-2-2H6a2 2 0 00-2 2v6a2 2 0 002 2zm10-10V7a4 4 0 00-8 0v4h8z" />
                  </svg>
                </div>
                <input
                  name="twoFactorCode" type="text" value={form.twoFactorCode} onChange={handleChange}
                  placeholder="Código de 6 dígitos"
                  maxLength={6}
                  className="w-full bg-[#071525] border border-teal-500/20 rounded-xl pl-10 pr-4 py-3 text-sm text-teal-100 placeholder-teal-500/25 outline-none focus:border-teal-400/60 focus:shadow-[0_0_0_3px_rgba(20,184,166,0.08)] transition-all"
                />
              </div>
            </div>

            {/* Error */}
            {error && (
              <div className="flex items-center gap-2 bg-red-500/10 border border-red-500/25 rounded-xl px-4 py-3">
                <svg className="w-4 h-4 text-red-400 flex-shrink-0" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 8v4m0 4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
                </svg>
                <p className="text-red-400 text-xs">{error}</p>
              </div>
            )}

            {/* Submit */}
            <button type="submit" disabled={!isValid || loading}
              className={`w-full py-3.5 rounded-xl font-semibold text-sm tracking-wide flex items-center justify-center gap-2 transition-all duration-300 mt-1
                ${isValid && !loading
                  ? 'bg-gradient-to-r from-teal-500 to-cyan-500 text-white hover:shadow-[0_0_30px_rgba(20,184,166,0.45)] hover:scale-[1.01]'
                  : 'bg-teal-500/10 text-teal-500/30 cursor-not-allowed border border-teal-500/10'}`}>
              {loading ? (
                <>
                  <svg className="w-4 h-4 animate-spin" fill="none" viewBox="0 0 24 24">
                    <circle className="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" strokeWidth="4" />
                    <path className="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4z" />
                  </svg>
                  Verificando...
                </>
              ) : (
                <>
                  Ingresar al portal
                  <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M14 5l7 7m0 0l-7 7m7-7H3" />
                  </svg>
                </>
              )}
            </button>
          </form>

          {/* Divider */}
          <div className="mt-6 flex items-center gap-3">
            <div className="flex-1 h-px bg-teal-500/10" />
            <span className="text-teal-500/30 text-xs">¿No tienes cuenta?</span>
            <div className="flex-1 h-px bg-teal-500/10" />
          </div>

          {/* Register */}
          <button
            onClick={() => onNavigate('register')}
            className="mt-4 w-full py-3 border border-teal-500/25 text-teal-300/70 hover:text-teal-300 hover:border-teal-400/40 hover:bg-teal-500/5 rounded-xl text-sm font-light tracking-wide transition-all duration-300">
            Solicitar acceso como distribuidor
          </button>

          {/* Trust */}
          <div className="mt-6 flex items-center justify-center gap-5 text-teal-500/25 text-xs">
            <span className="flex items-center gap-1">
              <svg className="w-3 h-3" fill="currentColor" viewBox="0 0 20 20">
                <path fillRule="evenodd" d="M5 9V7a5 5 0 0110 0v2a2 2 0 012 2v5a2 2 0 01-2 2H5a2 2 0 01-2-2v-5a2 2 0 012-2zm8-2v2H7V7a3 3 0 016 0z" clipRule="evenodd" />
              </svg>
              Conexión segura
            </span>
            <span>·</span>
            <span>INVIMA certificado</span>
            <span>·</span>
            <span>BPD</span>
          </div>
        </div>

        {/* Bottom note */}
        <p className="text-center text-teal-500/25 text-xs mt-6">
          © 2026 FAST Distribuciones · Acceso restringido a clientes autorizados
        </p>
      </div>
    </div>
  );
}