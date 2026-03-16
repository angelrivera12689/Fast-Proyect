import { useState } from 'react';
import { registerWithCompany } from '../services/auth';

export default function Register({ onNavigate }) {
  const [form, setForm] = useState({
    username: '',
    email: '',
    password: '',
    confirmPassword: '',
    phone: '',
    company: {
      nit: '',
      businessName: '',
      email: '',
      phone: '',
      address: '',
      logoUrl: ''
    }
  });
  const [showPass, setShowPass] = useState(false);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');

  const handleChange = (e) => {
    const { name, value } = e.target;
    setError('');
    if (name.startsWith('company.')) {
      const field = name.replace('company.', '');
      setForm(f => ({ ...f, company: { ...f.company, [field]: value } }));
    } else {
      setForm(f => ({ ...f, [name]: value }));
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');
    setSuccess('');

    // Validate passwords match
    if (form.password !== form.confirmPassword) {
      setError('Las contraseñas no coinciden');
      setLoading(false);
      return;
    }

    // Validate password length
    if (form.password.length < 6) {
      setError('La contraseña debe tener al menos 6 caracteres');
      setLoading(false);
      return;
    }

    try {
      await registerWithCompany({
        username: form.username,
        email: form.email,
        password: form.password,
        phone: form.phone,
        company: form.company
      });

      setSuccess('¡Registro exitoso! Ahora puedes iniciar sesión.');
      
      // Clear form
      setForm({
        username: '',
        email: '',
        password: '',
        confirmPassword: '',
        phone: '',
        company: {
          nit: '',
          businessName: '',
          email: '',
          phone: '',
          address: '',
          logoUrl: ''
        }
      });

      // Redirect to login after 2 seconds
      setTimeout(() => {
        onNavigate('login');
      }, 2000);

    } catch (error) {
      setError(error.message || 'Error al registrar. Intenta de nuevo.');
    } finally {
      setLoading(false);
    }
  };

  const isValid = form.username && form.username.length >= 3 && 
                  form.email && form.password && 
                  form.confirmPassword && form.company.nit && 
                  form.company.businessName && form.company.email;

  return (
    <div className="min-h-screen bg-gradient-to-br from-[#050d1a] via-[#0a1e35] to-[#061525] flex items-center justify-center px-4 relative overflow-hidden">

      {/* Background glows */}
      <div className="absolute inset-0 pointer-events-none">
        <div className="absolute top-0 right-0 w-[600px] h-[600px] bg-teal-500/8 rounded-full blur-[130px]" />
        <div className="absolute bottom-0 left-0 w-[500px] h-[500px] bg-cyan-400/6 rounded-full blur-[120px]" />
      </div>

      {/* Grid overlay */}
      <div className="absolute inset-0 opacity-[0.025]"
        style={{ backgroundImage: 'linear-gradient(#0ff 1px, transparent 1px), linear-gradient(90deg, #0ff 1px, transparent 1px)', backgroundSize: '60px 60px' }} />

      {/* Back button */}
      <button onClick={() => onNavigate('login')}
        className="absolute top-6 left-6 flex items-center gap-2 text-teal-400/50 hover:text-teal-300 text-sm transition-colors duration-300 z-10">
        <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M15 19l-7-7 7-7" />
        </svg>
        Volver al login
      </button>

      <div className="relative z-10 w-full max-w-2xl">

        {/* Card */}
        <div className="bg-gradient-to-br from-[#0d2137]/90 to-[#071525]/95 border border-teal-500/20 rounded-3xl p-8 backdrop-blur-md shadow-[0_0_80px_rgba(20,184,166,0.08)]">

          {/* Header */}
          <div className="text-center mb-6">
            <div className="inline-flex items-center justify-center w-14 h-14 rounded-2xl bg-gradient-to-br from-teal-500/20 to-cyan-500/10 border border-teal-500/25 mb-4">
              <svg className="w-7 h-7 text-teal-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={1.5} d="M18 9v3m0 0v3m0-3h3m-3 0h-3m-2-5a4 4 0 11-8 0 4 4 0 018 0zM3 20a6 6 0 0112 0v1H3v-1z" />
              </svg>
            </div>
            <h1 className="font-['Cormorant_Garamond',serif] text-3xl font-bold text-white tracking-wide">
              Registrate en FAST
            </h1>
            <p className="text-teal-400/50 text-xs tracking-[0.25em] uppercase mt-1">Distribuidora de Medicamentos</p>
          </div>

          {/* Success Message */}
          {success && (
            <div className="flex items-center gap-2 bg-green-500/10 border border-green-500/25 rounded-xl px-4 py-3 mb-4">
              <svg className="w-4 h-4 text-green-400 flex-shrink-0" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M5 13l4 4L19 7" />
              </svg>
              <p className="text-green-400 text-sm">{success}</p>
            </div>
          )}

          {/* Form */}
          <form onSubmit={handleSubmit} className="flex flex-col gap-4">

            {/* User Info Section */}
            <div className="border-b border-teal-500/20 pb-4">
              <h3 className="text-teal-400/70 text-sm font-medium mb-3">Información de usuario</h3>
              
              <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                {/* Username */}
                <div>
                  <label className="text-teal-400/60 text-xs tracking-wide mb-1.5 block">Usuario *</label>
                  <input
                    name="username" type="text" value={form.username} onChange={handleChange} required
                    placeholder="tu_usuario"
                    className="w-full bg-[#071525] border border-teal-500/20 rounded-xl px-4 py-3 text-sm text-teal-100 placeholder-teal-500/25 outline-none focus:border-teal-400/60 transition-all"
                  />
                </div>

                {/* Phone */}
                <div>
                  <label className="text-teal-400/60 text-xs tracking-wide mb-1.5 block">Teléfono</label>
                  <input
                    name="phone" type="tel" value={form.phone} onChange={handleChange}
                    placeholder="+57 300 123 4567"
                    className="w-full bg-[#071525] border border-teal-500/20 rounded-xl px-4 py-3 text-sm text-teal-100 placeholder-teal-500/25 outline-none focus:border-teal-400/60 transition-all"
                  />
                </div>

                {/* Email */}
                <div className="md:col-span-2">
                  <label className="text-teal-400/60 text-xs tracking-wide mb-1.5 block">Correo electrónico *</label>
                  <input
                    name="email" type="email" value={form.email} onChange={handleChange} required
                    placeholder="correo@empresa.com"
                    className="w-full bg-[#071525] border border-teal-500/20 rounded-xl px-4 py-3 text-sm text-teal-100 placeholder-teal-500/25 outline-none focus:border-teal-400/60 transition-all"
                  />
                </div>

                {/* Password */}
                <div>
                  <label className="text-teal-400/60 text-xs tracking-wide mb-1.5 block">Contraseña *</label>
                  <div className="relative">
                    <input
                      name="password" type={showPass ? 'text' : 'password'} value={form.password} onChange={handleChange} required
                      placeholder="••••••••"
                      className="w-full bg-[#071525] border border-teal-500/20 rounded-xl px-4 py-3 text-sm text-teal-100 placeholder-teal-500/25 outline-none focus:border-teal-400/60 transition-all"
                    />
                    <button type="button" onClick={() => setShowPass(p => !p)}
                      className="absolute right-3 top-1/2 -translate-y-1/2 text-teal-500/40 hover:text-teal-300 transition-colors">
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

                {/* Confirm Password */}
                <div>
                  <label className="text-teal-400/60 text-xs tracking-wide mb-1.5 block">Confirmar contraseña *</label>
                  <input
                    name="confirmPassword" type="password" value={form.confirmPassword} onChange={handleChange} required
                    placeholder="••••••••"
                    className="w-full bg-[#071525] border border-teal-500/20 rounded-xl px-4 py-3 text-sm text-teal-100 placeholder-teal-500/25 outline-none focus:border-teal-400/60 transition-all"
                  />
                </div>
              </div>
            </div>

            {/* Company Info Section */}
            <div className="pt-2">
              <h3 className="text-teal-400/70 text-sm font-medium mb-3">Información de la empresa</h3>
              
              <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                {/* NIT */}
                <div>
                  <label className="text-teal-400/60 text-xs tracking-wide mb-1.5 block">NIT *</label>
                  <input
                    name="company.nit" type="text" value={form.company.nit} onChange={handleChange} required
                    placeholder="123456789-0"
                    className="w-full bg-[#071525] border border-teal-500/20 rounded-xl px-4 py-3 text-sm text-teal-100 placeholder-teal-500/25 outline-none focus:border-teal-400/60 transition-all"
                  />
                </div>

                {/* Business Name */}
                <div>
                  <label className="text-teal-400/60 text-xs tracking-wide mb-1.5 block">Razón social *</label>
                  <input
                    name="company.businessName" type="text" value={form.company.businessName} onChange={handleChange} required
                    placeholder="Mi Empresa SAS"
                    className="w-full bg-[#071525] border border-teal-500/20 rounded-xl px-4 py-3 text-sm text-teal-100 placeholder-teal-500/25 outline-none focus:border-teal-400/60 transition-all"
                  />
                </div>

                {/* Company Email */}
                <div>
                  <label className="text-teal-400/60 text-xs tracking-wide mb-1.5 block">Email empresa *</label>
                  <input
                    name="company.email" type="email" value={form.company.email} onChange={handleChange} required
                    placeholder="empresa@empresa.com"
                    className="w-full bg-[#071525] border border-teal-500/20 rounded-xl px-4 py-3 text-sm text-teal-100 placeholder-teal-500/25 outline-none focus:border-teal-400/60 transition-all"
                  />
                </div>

                {/* Company Phone */}
                <div>
                  <label className="text-teal-400/60 text-xs tracking-wide mb-1.5 block">Teléfono empresa</label>
                  <input
                    name="company.phone" type="tel" value={form.company.phone} onChange={handleChange}
                    placeholder="+57 1 234 5678"
                    className="w-full bg-[#071525] border border-teal-500/20 rounded-xl px-4 py-3 text-sm text-teal-100 placeholder-teal-500/25 outline-none focus:border-teal-400/60 transition-all"
                  />
                </div>

                {/* Company Address */}
                <div className="md:col-span-2">
                  <label className="text-teal-400/60 text-xs tracking-wide mb-1.5 block">Dirección</label>
                  <input
                    name="company.address" type="text" value={form.company.address} onChange={handleChange}
                    placeholder="Carrera 123 # 45-67, Bogotá"
                    className="w-full bg-[#071525] border border-teal-500/20 rounded-xl px-4 py-3 text-sm text-teal-100 placeholder-teal-500/25 outline-none focus:border-teal-400/60 transition-all"
                  />
                </div>
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
              className={`w-full py-3.5 rounded-xl font-semibold text-sm tracking-wide flex items-center justify-center gap-2 transition-all duration-300 mt-2
                ${isValid && !loading
                  ? 'bg-gradient-to-r from-teal-500 to-cyan-500 text-white hover:shadow-[0_0_30px_rgba(20,184,166,0.45)] hover:scale-[1.01]'
                  : 'bg-teal-500/10 text-teal-500/30 cursor-not-allowed border border-teal-500/10'}`}>
              {loading ? (
                <>
                  <svg className="w-4 h-4 animate-spin" fill="none" viewBox="0 0 24 24">
                    <circle className="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" strokeWidth="4" />
                    <path className="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4z" />
                  </svg>
                  Registrando...
                </>
              ) : (
                <>
                  Crear cuenta
                  <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M18 9v3m0 0v3m0-3h3m-3 0h-3m-2-5a4 4 0 11-8 0 4 4 0 018 0zM3 20a6 6 0 0112 0v1H3v-1z" />
                  </svg>
                </>
              )}
            </button>
          </form>

          {/* Login link */}
          <div className="mt-4 text-center">
            <p className="text-teal-400/50 text-sm">
              ¿Ya tienes cuenta?{' '}
              <button onClick={() => onNavigate('login')} className="text-teal-300 hover:text-teal-200 underline">
                Iniciar sesión
              </button>
            </p>
          </div>

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
