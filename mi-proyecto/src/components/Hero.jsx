export default function Hero({ onNavigate }) {
  const nav = onNavigate || (() => {});
  return (
    <section className="relative min-h-screen flex items-center overflow-hidden bg-gradient-to-br from-[#050d1a] via-[#0a1e35] to-[#061525]">
      {/* Glow blobs */}
      <div className="absolute inset-0 pointer-events-none overflow-hidden">
        <div className="absolute top-20 right-0 w-[600px] h-[600px] bg-teal-500/10 rounded-full blur-[120px] animate-pulse" />
        <div className="absolute bottom-0 left-1/3 w-[400px] h-[400px] bg-cyan-400/8 rounded-full blur-[100px]" />
        <div className="absolute top-1/2 right-1/4 w-[300px] h-[300px] bg-blue-600/10 rounded-full blur-[80px]" style={{ animation: 'float 8s ease-in-out infinite' }} />
      </div>
 
      {/* Grid overlay */}
      <div className="absolute inset-0 opacity-[0.03]"
        style={{ backgroundImage: 'linear-gradient(#0ff 1px, transparent 1px), linear-gradient(90deg, #0ff 1px, transparent 1px)', backgroundSize: '60px 60px' }} />
 
      <div className="relative z-10 container mx-auto px-8 grid md:grid-cols-2 gap-12 items-center pt-24">
        {/* Text */}
        <div className="space-y-6">
          <p className="text-teal-400 tracking-[0.3em] text-sm uppercase font-light">Distribución Nacional</p>
          <h1 className="font-['Cormorant_Garamond',serif] text-6xl md:text-7xl font-bold leading-tight text-white">
            Tu aliado en<br />
            distribución de<br />
            <span className="text-transparent bg-clip-text bg-gradient-to-r from-teal-300 to-cyan-400 italic">
              Medicamentos
            </span>
            <br />
            <span className="text-transparent bg-clip-text bg-gradient-to-r from-amber-300 to-yellow-200 italic">
              de Calidad
            </span>
          </h1>
          <p className="text-teal-100/50 text-sm leading-relaxed max-w-xs font-light">
            Conectamos laboratorios y farmacias con la cadena de distribución más confiable y eficiente del país.
          </p>
          <div className="flex items-center gap-8">
            <div>
              <p className="text-white font-bold text-xl font-['Cormorant_Garamond',serif]">500+</p>
              <p className="text-teal-400/60 text-xs tracking-widest uppercase">Productos</p>
            </div>
            <div className="w-px h-8 bg-teal-500/30" />
            <div>
              <p className="text-white font-bold text-xl font-['Cormorant_Garamond',serif]">200+</p>
              <p className="text-teal-400/60 text-xs tracking-widest uppercase">Clientes</p>
            </div>
            <div className="w-px h-8 bg-teal-500/30" />
            <div>
              <p className="text-white font-bold text-xl font-['Cormorant_Garamond',serif]">15+</p>
              <p className="text-teal-400/60 text-xs tracking-widest uppercase">Años</p>
            </div>
          </div>
          <button onClick={() => nav('catalog')} className="group relative mt-4 px-8 py-3 bg-gradient-to-r from-teal-500 to-cyan-500 text-white font-semibold rounded-full text-sm tracking-wider uppercase overflow-hidden transition-all duration-300 hover:shadow-[0_0_30px_rgba(20,184,166,0.5)]">
            <span className="relative z-10">Ver Catálogo</span>
            <div className="absolute inset-0 bg-gradient-to-r from-cyan-400 to-teal-400 opacity-0 group-hover:opacity-100 transition-opacity duration-300" />
          </button>
        </div>
 
        {/* Illustration — pill / medicine box */}
        <div className="relative flex justify-center items-center">
          <div className="absolute w-80 h-80 bg-teal-500/20 rounded-full blur-[60px]" />
          <div className="relative z-10 w-72 h-96 flex items-center justify-center">
            <div className="relative">
              <div className="absolute -inset-16 bg-gradient-to-t from-teal-500/0 via-teal-400/20 to-cyan-300/30 rounded-full blur-2xl animate-pulse" />
              <svg viewBox="0 0 220 300" className="w-64 h-auto drop-shadow-[0_0_30px_rgba(20,184,166,0.6)]" fill="none" xmlns="http://www.w3.org/2000/svg">
                <defs>
                  <linearGradient id="boxGrad" x1="0" y1="0" x2="1" y2="1">
                    <stop offset="0%" stopColor="#0d9488" stopOpacity="0.9" />
                    <stop offset="100%" stopColor="#164e63" stopOpacity="0.95" />
                  </linearGradient>
                  <linearGradient id="boxTop" x1="0" y1="0" x2="1" y2="1">
                    <stop offset="0%" stopColor="#2dd4bf" stopOpacity="0.9" />
                    <stop offset="100%" stopColor="#0891b2" stopOpacity="0.9" />
                  </linearGradient>
                  <linearGradient id="shimBox" x1="0" y1="0" x2="1" y2="0">
                    <stop offset="0%" stopColor="white" stopOpacity="0" />
                    <stop offset="40%" stopColor="white" stopOpacity="0.1" />
                    <stop offset="100%" stopColor="white" stopOpacity="0" />
                  </linearGradient>
                  <linearGradient id="pillGrad" x1="0" y1="0" x2="1" y2="0">
                    <stop offset="0%" stopColor="#5eead4" />
                    <stop offset="50%" stopColor="#ffffff" stopOpacity="0.9" />
                    <stop offset="100%" stopColor="#0891b2" />
                  </linearGradient>
                </defs>
 
                {/* Medicine box body */}
                <rect x="30" y="80" width="160" height="190" rx="10" fill="url(#boxGrad)" />
                <rect x="30" y="80" width="160" height="190" rx="10" fill="url(#shimBox)" />
 
                {/* Box top flap */}
                <rect x="30" y="60" width="160" height="30" rx="6" fill="url(#boxTop)" />
                <rect x="75" y="52" width="70" height="16" rx="4" fill="#2dd4bf" />
 
                {/* Cross symbol */}
                <rect x="95" y="110" width="30" height="8" rx="3" fill="rgba(255,255,255,0.7)" />
                <rect x="107" y="98" width="8" height="30" rx="3" fill="rgba(255,255,255,0.7)" />
 
                {/* Label area */}
                <rect x="44" y="155" width="132" height="90" rx="6" fill="rgba(255,255,255,0.04)" stroke="rgba(255,255,255,0.12)" strokeWidth="1" />
                <text x="110" y="182" textAnchor="middle" fill="rgba(255,255,255,0.85)" fontSize="13" fontFamily="serif" fontWeight="bold" letterSpacing="4">FAST</text>
                <text x="110" y="200" textAnchor="middle" fill="rgba(94,234,212,0.8)" fontSize="7" fontFamily="sans-serif" letterSpacing="2">DISTRIBUIDORA</text>
                <rect x="60" y="208" width="100" height="1" fill="rgba(255,255,255,0.1)" />
                <text x="110" y="224" textAnchor="middle" fill="rgba(255,255,255,0.4)" fontSize="6" fontFamily="sans-serif" letterSpacing="1">REG. SANITARIO</text>
                <text x="110" y="236" textAnchor="middle" fill="rgba(94,234,212,0.5)" fontSize="6" fontFamily="sans-serif">INVIMA 2026-COL</text>
 
                {/* Pills floating */}
                <ellipse cx="168" cy="68" rx="18" ry="9" rx1="18" fill="url(#pillGrad)" transform="rotate(-30 168 68)" />
                <ellipse cx="52" cy="72" rx="15" ry="7" fill="url(#pillGrad)" transform="rotate(20 52 72)" opacity="0.7" />
                <ellipse cx="175" cy="200" rx="12" ry="6" fill="url(#pillGrad)" transform="rotate(-15 175 200)" opacity="0.5" />
 
                {/* Base shadow */}
                <ellipse cx="110" cy="278" rx="70" ry="8" fill="#0d9488" opacity="0.2" />
              </svg>
            </div>
          </div>
          <div className="absolute bottom-8 right-0 bg-gradient-to-br from-teal-500 to-cyan-600 text-white text-xs font-bold px-4 py-2 rounded-full shadow-lg shadow-teal-500/30 animate-bounce">
            CERTIFICADO INVIMA
          </div>
        </div>
      </div>
 
      <style>{`
        @keyframes float {
          0%, 100% { transform: translateY(0px); }
          50% { transform: translateY(-20px); }
        }
      `}</style>
    </section>
  );
}