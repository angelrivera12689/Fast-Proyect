import { useState } from 'react';

const INFO = [
  {
    icon: (
      <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={1.5} d="M17.657 16.657L13.414 20.9a1.998 1.998 0 01-2.827 0l-4.244-4.243a8 8 0 1111.314 0z" />
        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={1.5} d="M15 11a3 3 0 11-6 0 3 3 0 016 0z" />
      </svg>
    ),
    label: 'Dirección',
    value: 'Cra. 30 # 17-52, Bogotá D.C.',
    sub: 'Zona Industrial, Puente Aranda',
  },
  {
    icon: (
      <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={1.5} d="M3 5a2 2 0 012-2h3.28a1 1 0 01.948.684l1.498 4.493a1 1 0 01-.502 1.21l-2.257 1.13a11.042 11.042 0 005.516 5.516l1.13-2.257a1 1 0 011.21-.502l4.493 1.498a1 1 0 01.684.949V19a2 2 0 01-2 2h-1C9.716 21 3 14.284 3 6V5z" />
      </svg>
    ),
    label: 'Teléfono',
    value: '+57 (1) 234 5678',
    sub: 'Lun – Vie, 7:00 am – 5:00 pm',
  },
  {
    icon: (
      <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={1.5} d="M3 8l7.89 5.26a2 2 0 002.22 0L21 8M5 19h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v10a2 2 0 002 2z" />
      </svg>
    ),
    label: 'Correo',
    value: 'contacto@fast-dist.com',
    sub: 'Respondemos en menos de 24h',
  },
  {
    icon: (
      <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={1.5} d="M9 12l2 2 4-4m5.618-4.016A11.955 11.955 0 0112 2.944a11.955 11.955 0 01-8.618 3.04A12.02 12.02 0 003 9c0 5.591 3.824 10.29 9 11.622 5.176-1.332 9-6.03 9-11.622 0-1.042-.133-2.052-.382-3.016z" />
      </svg>
    ),
    label: 'Reg. INVIMA',
    value: 'BPD-COL-2024-0312',
    sub: 'Buenas Prácticas de Distribución',
  },
];

const TIPOS_QUEJA = [
  'Selecciona un tipo...',
  'Producto en mal estado',
  'Error en el pedido',
  'Demora en la entrega',
  'Facturación incorrecta',
  'Atención al cliente',
  'Otro',
];

export default function Contact() {
  const [form, setForm] = useState({ nombre: '', empresa: '', email: '', tipo: '', mensaje: '' });
  const [sent, setSent] = useState(false);
  const [loading, setLoading] = useState(false);

  const handleChange = (e) => setForm(f => ({ ...f, [e.target.name]: e.target.value }));

  const handleSubmit = (e) => {
    e.preventDefault();
    setLoading(true);
    // TODO: conectar con tu backend / servicio de email
    setTimeout(() => { setLoading(false); setSent(true); }, 1500);
  };

  const isValid = form.nombre && form.email && form.tipo && form.tipo !== TIPOS_QUEJA[0] && form.mensaje;

  return (
    <section className="py-28 bg-gradient-to-b from-[#061525] to-[#050d1a] relative overflow-hidden">
      {/* Ambient glows */}
      <div className="absolute inset-0 pointer-events-none">
        <div className="absolute bottom-0 left-0 w-[500px] h-[400px] bg-teal-500/5 rounded-full blur-[100px]" />
        <div className="absolute top-0 right-0 w-[400px] h-[300px] bg-amber-400/4 rounded-full blur-[100px]" />
      </div>

      <div className="container mx-auto px-8 relative z-10">
        {/* Header */}
        <div className="text-center mb-16">
          <p className="text-teal-400/60 tracking-[0.3em] text-xs uppercase mb-3">Estamos aquí para ayudarte</p>
          <h2 className="font-['Cormorant_Garamond',serif] text-4xl md:text-5xl font-bold text-white">
            Contacto y{' '}
            <span className="text-transparent bg-clip-text bg-gradient-to-r from-teal-300 to-amber-300">
              PQRS
            </span>
          </h2>
          <div className="mt-5 flex justify-center items-center gap-3">
            <div className="w-12 h-px bg-gradient-to-r from-transparent to-teal-400" />
            <div className="w-1.5 h-1.5 rounded-full bg-teal-400" />
            <div className="w-12 h-px bg-gradient-to-l from-transparent to-teal-400" />
          </div>
        </div>

        <div className="grid lg:grid-cols-2 gap-10">

          {/* Left col — mapa + info */}
          <div className="flex flex-col gap-6">
            {/* Mapa embed */}
            <div className="relative rounded-2xl overflow-hidden border border-teal-500/15 h-64">
              {/* Overlay de color para armonizar con el tema oscuro */}
              <div className="absolute inset-0 bg-[#050d1a]/30 pointer-events-none z-10 mix-blend-multiply" />
              <iframe
                title="Ubicación FAST Distribuciones"
                src="https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d3976.6!2d-74.1069!3d4.6097!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x0%3A0x0!2zNMKwMzYnMzUuMCJOIDc0wrAwNiczNC44Ilc!5e0!3m2!1ses!2sco!4v1234567890"
                width="100%"
                height="100%"
                style={{ border: 0, filter: 'invert(90%) hue-rotate(180deg)' }}
                allowFullScreen=""
                loading="lazy"
                referrerPolicy="no-referrer-when-downgrade"
              />
            </div>

            {/* Info cards */}
            <div className="grid sm:grid-cols-2 gap-4">
              {INFO.map((item, i) => (
                <div key={i}
                  className="bg-gradient-to-br from-[#0d2137]/80 to-[#071525]/80 border border-teal-500/15 rounded-2xl p-4 flex gap-3 hover:border-teal-400/30 transition-all duration-300">
                  <div className="w-9 h-9 rounded-xl bg-teal-500/10 border border-teal-500/20 flex items-center justify-center text-teal-400 flex-shrink-0">
                    {item.icon}
                  </div>
                  <div className="min-w-0">
                    <p className="text-teal-400/60 text-xs tracking-widest uppercase mb-0.5">{item.label}</p>
                    <p className="text-white text-sm font-medium truncate">{item.value}</p>
                    <p className="text-teal-100/35 text-xs mt-0.5">{item.sub}</p>
                  </div>
                </div>
              ))}
            </div>
          </div>

          {/* Right col — formulario PQRS */}
          <div className="bg-gradient-to-br from-[#0d2137]/80 to-[#071525]/80 border border-teal-500/15 rounded-2xl p-7 backdrop-blur-sm">
            {sent ? (
              <div className="h-full flex flex-col items-center justify-center text-center gap-4 py-10">
                <div className="w-16 h-16 rounded-full bg-teal-500/15 border border-teal-400/30 flex items-center justify-center">
                  <svg className="w-8 h-8 text-teal-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M5 13l4 4L19 7" />
                  </svg>
                </div>
                <p className="font-['Cormorant_Garamond',serif] text-2xl font-bold text-white">¡Recibido!</p>
                <p className="text-teal-100/50 text-sm max-w-xs">
                  Tu PQRS fue radicada exitosamente. Te responderemos al correo registrado en menos de 24 horas hábiles.
                </p>
                <button onClick={() => { setSent(false); setForm({ nombre: '', empresa: '', email: '', tipo: '', mensaje: '' }); }}
                  className="mt-2 px-6 py-2 border border-teal-500/30 text-teal-300 text-sm rounded-xl hover:bg-teal-500/10 transition-all">
                  Enviar otra
                </button>
              </div>
            ) : (
              <>
                <p className="text-teal-300 text-xs tracking-[0.2em] uppercase font-semibold mb-6">
                  Formulario de PQRS
                </p>

                <form onSubmit={handleSubmit} className="flex flex-col gap-4">
                  <div className="grid sm:grid-cols-2 gap-4">
                    <div>
                      <label className="text-teal-400/60 text-xs mb-1.5 block">Nombre completo *</label>
                      <input name="nombre" value={form.nombre} onChange={handleChange} required
                        placeholder="Juan Pérez"
                        className="w-full bg-[#071525] border border-teal-500/20 rounded-xl px-4 py-2.5 text-sm text-teal-100 placeholder-teal-500/25 outline-none focus:border-teal-400/50 transition-colors" />
                    </div>
                    <div>
                      <label className="text-teal-400/60 text-xs mb-1.5 block">Empresa</label>
                      <input name="empresa" value={form.empresa} onChange={handleChange}
                        placeholder="Farmacia El Bienestar"
                        className="w-full bg-[#071525] border border-teal-500/20 rounded-xl px-4 py-2.5 text-sm text-teal-100 placeholder-teal-500/25 outline-none focus:border-teal-400/50 transition-colors" />
                    </div>
                  </div>

                  <div>
                    <label className="text-teal-400/60 text-xs mb-1.5 block">Correo electrónico *</label>
                    <input name="email" type="email" value={form.email} onChange={handleChange} required
                      placeholder="correo@empresa.com"
                      className="w-full bg-[#071525] border border-teal-500/20 rounded-xl px-4 py-2.5 text-sm text-teal-100 placeholder-teal-500/25 outline-none focus:border-teal-400/50 transition-colors" />
                  </div>

                  <div>
                    <label className="text-teal-400/60 text-xs mb-1.5 block">Tipo de PQRS *</label>
                    <select name="tipo" value={form.tipo} onChange={handleChange} required
                      className="w-full bg-[#071525] border border-teal-500/20 rounded-xl px-4 py-2.5 text-sm text-teal-100 outline-none focus:border-teal-400/50 transition-colors appearance-none">
                      {TIPOS_QUEJA.map(t => (
                        <option key={t} value={t === TIPOS_QUEJA[0] ? '' : t}
                          disabled={t === TIPOS_QUEJA[0]}
                          className="bg-[#0d2137]">
                          {t}
                        </option>
                      ))}
                    </select>
                  </div>

                  <div>
                    <label className="text-teal-400/60 text-xs mb-1.5 block">Descripción *</label>
                    <textarea name="mensaje" value={form.mensaje} onChange={handleChange} required rows={4}
                      placeholder="Describe detalladamente tu petición, queja, reclamo o sugerencia..."
                      className="w-full bg-[#071525] border border-teal-500/20 rounded-xl px-4 py-2.5 text-sm text-teal-100 placeholder-teal-500/25 outline-none focus:border-teal-400/50 transition-colors resize-none" />
                  </div>

                  <button type="submit" disabled={!isValid || loading}
                    className={`w-full py-3 rounded-xl font-semibold text-sm tracking-wide flex items-center justify-center gap-2 transition-all duration-300
                      ${isValid && !loading
                        ? 'bg-gradient-to-r from-teal-500 to-cyan-500 text-white hover:shadow-[0_0_25px_rgba(20,184,166,0.4)]'
                        : 'bg-teal-500/10 text-teal-500/30 cursor-not-allowed border border-teal-500/10'}`}>
                    {loading ? (
                      <>
                        <svg className="w-4 h-4 animate-spin" fill="none" viewBox="0 0 24 24">
                          <circle className="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" strokeWidth="4" />
                          <path className="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4z" />
                        </svg>
                        Enviando...
                      </>
                    ) : 'Enviar PQRS'}
                  </button>

                  <p className="text-center text-teal-400/30 text-xs">
                    Tiempo de respuesta: máximo 3 días hábiles según normativa colombiana
                  </p>
                </form>
              </>
            )}
          </div>
        </div>
      </div>
    </section>
  );
}