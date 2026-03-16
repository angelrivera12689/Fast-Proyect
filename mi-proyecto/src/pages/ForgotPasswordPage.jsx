import { useState } from 'react';

export default function ForgotPasswordPage({ onNavigate }) {
  const [email, setEmail] = useState('');
  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState('');
  const [error, setError] = useState('');
  const [step, setStep] = useState(1); // 1: email, 2: code + new password

  const handleSubmitEmail = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');
    setMessage('');

    try {
      const response = await fetch('http://localhost:8080/api/auth/forgot-password', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ email })
      });

      const data = await response.json();

      if (response.ok) {
        setMessage(data.message || 'Código enviado correctamente');
        setStep(2);
      } else {
        setError(data.error || data.message || 'Error al enviar el código');
      }
    } catch {
      setError('Error de conexión. Intenta de nuevo.');
    } finally {
      setLoading(false);
    }
  };

  const handleResetPassword = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');
    setMessage('');

    const formData = new FormData(e.target);
    const code = formData.get('code');
    const newPassword = formData.get('newPassword');
    const confirmPassword = formData.get('confirmPassword');

    if (newPassword !== confirmPassword) {
      setError('Las contraseñas no coinciden');
      setLoading(false);
      return;
    }

    try {
      const response = await fetch('http://localhost:8080/api/auth/reset-password', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ email, code, newPassword })
      });

      const data = await response.json();

      if (response.ok) {
        setMessage(data.message || 'Contraseña restablecida correctamente');
        setTimeout(() => {
          onNavigate('login');
        }, 2000);
      } else {
        setError(data.error || data.message || 'Error al restablecer la contraseña');
      }
    } catch {
      setError('Error de conexión. Intenta de nuevo.');
    } finally {
      setLoading(false);
    }
  };

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

      <div className="relative z-10 w-full max-w-md">
        <div className="bg-gradient-to-br from-[#0d2137]/90 to-[#071525]/95 border border-teal-500/20 rounded-3xl p-8 backdrop-blur-md shadow-[0_0_80px_rgba(20,184,166,0.08)]">

          {/* Header */}
          <div className="text-center mb-8">
            <div className="inline-flex items-center justify-center w-14 h-14 rounded-2xl bg-gradient-to-br from-teal-500/20 to-cyan-500/10 border border-teal-500/25 mb-4">
              <svg className="w-7 h-7 text-teal-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={1.5} d="M15 7a2 2 0 012 2m4 0a6 6 0 01-7.743 5.743L11 17H9v2H7v2H4a1 1 0 01-1-1v-2.586a1 1 0 01.293-.707l5.964-5.964A6 6 0 1121 9z" />
              </svg>
            </div>
            <h1 className="font-['Cormorant_Garamond',serif] text-2xl font-bold text-white tracking-wide">
              {step === 1 ? 'Recuperar Contraseña' : 'Nueva Contraseña'}
            </h1>
            <p className="text-teal-400/50 text-xs mt-2">
              {step === 1 
                ? 'Ingresa tu correo electrónico para recibir un código de verificación' 
                : 'Ingresa el código que llegó a tu correo y tu nueva contraseña'}
            </p>
          </div>

          {/* Success message */}
          {message && step === 2 && (
            <div className="mb-6 flex items-center gap-2 bg-green-500/10 border border-green-500/25 rounded-xl px-4 py-3">
              <svg className="w-4 h-4 text-green-400 flex-shrink-0" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M5 13l4 4L19 7" />
              </svg>
              <p className="text-green-400 text-xs">{message}</p>
            </div>
          )}

          {/* Error */}
          {error && (
            <div className="mb-6 flex items-center gap-2 bg-red-500/10 border border-red-500/25 rounded-xl px-4 py-3">
              <svg className="w-4 h-4 text-red-400 flex-shrink-0" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 8v4m0 4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
              </svg>
              <p className="text-red-400 text-xs">{error}</p>
            </div>
          )}

          {/* Form Step 1: Email */}
          {step === 1 && (
            <form onSubmit={handleSubmitEmail} className="flex flex-col gap-4">
              <div>
                <label className="text-teal-400/60 text-xs tracking-wide mb-1.5 block">Correo electrónico</label>
                <div className="relative">
                  <div className="absolute left-3.5 top-1/2 -translate-y-1/2 text-teal-500/40">
                    <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M3 8l7.89 5.26a2 2 0 002.22 0L21 8M5 19h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v10a2 2 0 002 2z" />
                    </svg>
                  </div>
                  <input
                    type="email"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                    required
                    placeholder="correo@empresa.com"
                    className="w-full bg-[#071525] border border-teal-500/20 rounded-xl pl-10 pr-4 py-3 text-sm text-teal-100 placeholder-teal-500/25 outline-none focus:border-teal-400/60 focus:shadow-[0_0_0_3px_rgba(20,184,166,0.08)] transition-all"
                  />
                </div>
              </div>

              <button type="submit" disabled={!email || loading}
                className={`w-full py-3.5 rounded-xl font-semibold text-sm tracking-wide flex items-center justify-center gap-2 transition-all duration-300 mt-1
                  ${!email || loading
                    ? 'bg-teal-500/10 text-teal-500/30 cursor-not-allowed border border-teal-500/10'
                    : 'bg-gradient-to-r from-teal-500 to-cyan-500 text-white hover:shadow-[0_0_30px_rgba(20,184,166,0.45)] hover:scale-[1.01]'}`}>
                {loading ? (
                  <>
                    <svg className="w-4 h-4 animate-spin" fill="none" viewBox="0 0 24 24">
                      <circle className="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" strokeWidth="4" />
                      <path className="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4z" />
                    </svg>
                    Enviando...
                  </>
                ) : (
                  <>
                    Enviar código
                    <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M14 5l7 7m0 0l-7 7m7-7H3" />
                    </svg>
                  </>
                )}
              </button>
            </form>
          )}

          {/* Form Step 2: Code + New Password */}
          {step === 2 && (
            <form onSubmit={handleResetPassword} className="flex flex-col gap-4">
              <div>
                <label className="text-teal-400/60 text-xs tracking-wide mb-1.5 block">Código de verificación</label>
                <div className="relative">
                  <div className="absolute left-3.5 top-1/2 -translate-y-1/2 text-teal-500/40">
                    <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 15v2m-6 4h12a2 2 0 002-2v-6a2 2 0 00-2-2H6a2 2 0 00-2 2v6a2 2 0 002 2zm10-10V7a4 4 0 00-8 0v4h8z" />
                    </svg>
                  </div>
                  <input
                    name="code"
                    type="text"
                    required
                    placeholder="123456"
                    maxLength={6}
                    className="w-full bg-[#071525] border border-teal-500/20 rounded-xl pl-10 pr-4 py-3 text-sm text-teal-100 placeholder-teal-500/25 outline-none focus:border-teal-400/60 focus:shadow-[0_0_0_3px_rgba(20,184,166,0.08)] transition-all text-center tracking-[0.5em] font-mono"
                  />
                </div>
              </div>

              <div>
                <label className="text-teal-400/60 text-xs tracking-wide mb-1.5 block">Nueva contraseña</label>
                <input
                  name="newPassword"
                  type="password"
                  required
                  minLength={6}
                  placeholder="••••••••"
                  className="w-full bg-[#071525] border border-teal-500/20 rounded-xl px-4 py-3 text-sm text-teal-100 placeholder-teal-500/25 outline-none focus:border-teal-400/60 focus:shadow-[0_0_0_3px_rgba(20,184,166,0.08)] transition-all"
                />
              </div>

              <div>
                <label className="text-teal-400/60 text-xs tracking-wide mb-1.5 block">Confirmar contraseña</label>
                <input
                  name="confirmPassword"
                  type="password"
                  required
                  minLength={6}
                  placeholder="••••••••"
                  className="w-full bg-[#071525] border border-teal-500/20 rounded-xl px-4 py-3 text-sm text-teal-100 placeholder-teal-500/25 outline-none focus:border-teal-400/60 focus:shadow-[0_0_0_3px_rgba(20,184,166,0.08)] transition-all"
                />
              </div>

              <button type="submit" disabled={loading}
                className={`w-full py-3.5 rounded-xl font-semibold text-sm tracking-wide flex items-center justify-center gap-2 transition-all duration-300 mt-1
                  ${loading
                    ? 'bg-teal-500/10 text-teal-500/30 cursor-not-allowed border border-teal-500/10'
                    : 'bg-gradient-to-r from-teal-500 to-cyan-500 text-white hover:shadow-[0_0_30px_rgba(20,184,166,0.45)] hover:scale-[1.01]'}`}>
                {loading ? (
                  <>
                    <svg className="w-4 h-4 animate-spin" fill="none" viewBox="0 0 24 24">
                      <circle className="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" strokeWidth="4" />
                      <path className="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4z" />
                    </svg>
                    Restableciendo...
                  </>
                ) : (
                  <>
                    Restablecer contraseña
                    <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M14 5l7 7m0 0l-7 7m7-7H3" />
                    </svg>
                  </>
                )}
              </button>
            </form>
          )}

          {/* Success message after step 1 */}
          {message && step === 1 && (
            <div className="mt-6 flex items-center justify-center gap-2 bg-green-500/10 border border-green-500/25 rounded-xl px-4 py-3">
              <svg className="w-4 h-4 text-green-400 flex-shrink-0" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M5 13l4 4L19 7" />
              </svg>
              <p className="text-green-400 text-xs text-center">{message}</p>
            </div>
          )}
        </div>

        <p className="text-center text-teal-500/25 text-xs mt-6">
          © 2026 FAST Distribuciones
        </p>
      </div>
    </div>
  );
}
