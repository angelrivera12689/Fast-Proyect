const pillars = [
  {
    icon: (
      <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={1.5} d="M19 21V5a2 2 0 00-2-2H7a2 2 0 00-2 2v16m14 0h2m-2 0h-5m-9 0H3m2 0h5M9 7h1m-1 4h1m4-4h1m-1 4h1m-5 10v-5a1 1 0 011-1h2a1 1 0 011 1v5m-4 0h4" />
      </svg>
    ),
    label: 'Quiénes Somos',
    title: 'Una empresa construida sobre confianza',
    text: 'Somos FAST, una distribuidora de medicamentos con más de 15 años conectando laboratorios farmacéuticos con farmacias, clínicas y hospitales en todo el país, garantizando la cadena de frío y el cumplimiento regulatorio en cada entrega.',
    accent: 'from-teal-400 to-cyan-400',
    glow: 'rgba(20,184,166,0.15)',
  },
  {
    icon: (
      <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={1.5} d="M9 12l2 2 4-4m5.618-4.016A11.955 11.955 0 0112 2.944a11.955 11.955 0 01-8.618 3.04A12.02 12.02 0 003 9c0 5.591 3.824 10.29 9 11.622 5.176-1.332 9-6.03 9-11.622 0-1.042-.133-2.052-.382-3.016z" />
      </svg>
    ),
    label: 'Misión',
    title: 'Garantizar el acceso oportuno al medicamento',
    text: 'Nuestra misión es asegurar que cada medicamento llegue en perfectas condiciones, en el momento justo y al precio correcto. Operamos bajo estrictos estándares INVIMA, BPD (Buenas Prácticas de Distribución) y trazabilidad completa de productos.',
    accent: 'from-amber-300 to-yellow-300',
    glow: 'rgba(251,191,36,0.12)',
  },
  {
    icon: (
      <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={1.5} d="M3.055 11H5a2 2 0 012 2v1a2 2 0 002 2 2 2 0 012 2v2.945M8 3.935V5.5A2.5 2.5 0 0010.5 8h.5a2 2 0 012 2 2 2 0 104 0 2 2 0 012-2h1.064M15 20.488V18a2 2 0 012-2h3.064" />
      </svg>
    ),
    label: 'Visión',
    title: 'Ser el distribuidor #1 de Colombia',
    text: 'Aspiramos a consolidarnos como la red de distribución farmacéutica más confiable y eficiente de Colombia, expandiendo nuestra cobertura a toda la cadena de salud y siendo el socio estratégico preferido de los principales laboratorios nacionales e internacionales.',
    accent: 'from-cyan-300 to-teal-300',
    glow: 'rgba(34,211,238,0.12)',
  },
];
 
export default function AboutUs() {
  return (
    <section className="py-28 bg-gradient-to-b from-[#050d1a] via-[#071525] to-[#061525] relative overflow-hidden">
      <div className="absolute inset-0 pointer-events-none">
        <div className="absolute top-1/2 left-0 w-[500px] h-[500px] bg-teal-500/5 rounded-full blur-[100px] -translate-y-1/2" />
        <div className="absolute top-1/2 right-0 w-[400px] h-[400px] bg-amber-400/5 rounded-full blur-[100px] -translate-y-1/2" />
        <div className="absolute left-1/2 top-0 bottom-0 w-px bg-gradient-to-b from-transparent via-teal-500/10 to-transparent -translate-x-1/2" />
      </div>
 
      <div className="container mx-auto px-8 relative z-10">
        <div className="text-center mb-20">
          <p className="text-teal-400/60 tracking-[0.3em] text-xs uppercase mb-3">Nuestra empresa</p>
          <h2 className="font-['Cormorant_Garamond',serif] text-4xl md:text-5xl font-bold text-white">
            El compromiso detrás de{' '}
            <span className="text-transparent bg-clip-text bg-gradient-to-r from-teal-300 to-amber-300 italic">
              FAST
            </span>
          </h2>
          <div className="mt-5 flex justify-center items-center gap-3">
            <div className="w-12 h-px bg-gradient-to-r from-transparent to-teal-400" />
            <div className="w-1.5 h-1.5 rounded-full bg-teal-400" />
            <div className="w-12 h-px bg-gradient-to-l from-transparent to-teal-400" />
          </div>
        </div>
 
        <div className="grid md:grid-cols-3 gap-8">
          {pillars.map((p, i) => (
            <div
              key={i}
              className="group relative bg-gradient-to-br from-[#0d2137]/70 to-[#071525]/90 border border-teal-500/15 rounded-3xl p-8 hover:border-teal-400/30 transition-all duration-500 overflow-hidden"
              onMouseEnter={e => e.currentTarget.style.boxShadow = `0 0 40px ${p.glow}`}
              onMouseLeave={e => e.currentTarget.style.boxShadow = 'none'}
            >
              <div className={`absolute top-0 left-8 right-8 h-px bg-gradient-to-r ${p.accent} opacity-40 group-hover:opacity-80 transition-opacity duration-500`} />
 
              <div className="inline-flex items-center justify-center w-12 h-12 rounded-2xl mb-6"
                style={{ background: 'rgba(13,33,55,0.8)', border: '1px solid rgba(255,255,255,0.08)' }}>
                <span className={`text-transparent bg-clip-text bg-gradient-to-br ${p.accent}`}>
                  {p.icon}
                </span>
              </div>
 
              <span className={`inline-block text-xs tracking-[0.2em] uppercase font-semibold text-transparent bg-clip-text bg-gradient-to-r ${p.accent} mb-3`}>
                {p.label}
              </span>
 
              <h3 className="font-['Cormorant_Garamond',serif] text-2xl font-bold text-white leading-snug mb-4">
                {p.title}
              </h3>
 
              <p className="text-teal-100/50 text-sm leading-relaxed font-light">
                {p.text}
              </p>
            </div>
          ))}
        </div>
 
        <div className="mt-20 text-center max-w-2xl mx-auto">
          <p className="font-['Cormorant_Garamond',serif] text-2xl md:text-3xl text-white/60 italic leading-relaxed">
            "Detrás de cada medicamento que llega a tiempo, hay un equipo que no falla."
          </p>
          <p className="mt-4 text-teal-400/40 text-xs tracking-[0.3em] uppercase">— Equipo FAST Distribuciones</p>
        </div>
      </div>
    </section>
  );
}