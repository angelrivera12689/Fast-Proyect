export default function ProductCard({ name, price, onNavigate, delay = 0 }) {
  const nav = onNavigate || (() => {});
  
  // Formatear precio si es número
  const formattedPrice = typeof price === 'number' 
    ? price.toLocaleString('es-CO')
    : price;
  return (
    <div
      className="group relative bg-gradient-to-br from-[#0d2137] to-[#071525] border border-teal-500/15 rounded-2xl p-5 flex flex-col items-center gap-3 cursor-pointer hover:border-teal-400/40 hover:shadow-[0_0_30px_rgba(20,184,166,0.15)] transition-all duration-500"
      style={{ animationDelay: `${delay}ms` }}
    >
      <div className="absolute inset-0 rounded-2xl bg-gradient-to-br from-teal-500/0 to-cyan-500/0 group-hover:from-teal-500/5 group-hover:to-cyan-500/5 transition-all duration-500" />
 
      <div className="relative w-24 h-28 flex items-center justify-center">
        <div className="absolute inset-0 bg-teal-400/10 rounded-full blur-xl group-hover:bg-teal-400/20 transition-all duration-500" />
        <svg viewBox="0 0 100 110" className="w-20 h-auto relative z-10 drop-shadow-[0_4px_12px_rgba(20,184,166,0.4)] group-hover:scale-105 transition-transform duration-500" fill="none" xmlns="http://www.w3.org/2000/svg">
          <defs>
            <linearGradient id={`box-${name}`} x1="0" y1="0" x2="1" y2="1">
              <stop offset="0%" stopColor="#0d9488" stopOpacity="0.9" />
              <stop offset="100%" stopColor="#164e63" stopOpacity="0.95" />
            </linearGradient>
            <linearGradient id={`top-${name}`} x1="0" y1="0" x2="1" y2="1">
              <stop offset="0%" stopColor="#2dd4bf" />
              <stop offset="100%" stopColor="#0891b2" />
            </linearGradient>
            <linearGradient id={`pill-${name}`} x1="0" y1="0" x2="1" y2="0">
              <stop offset="0%" stopColor="#5eead4" />
              <stop offset="50%" stopColor="#ffffff" stopOpacity="0.85" />
              <stop offset="100%" stopColor="#0891b2" />
            </linearGradient>
          </defs>
          <rect x="15" y="30" width="70" height="72" rx="6" fill={`url(#box-${name})`} />
          <rect x="15" y="18" width="70" height="16" rx="4" fill={`url(#top-${name})`} />
          <rect x="33" y="12" width="34" height="10" rx="3" fill="#2dd4bf" />
          <rect x="42" y="44" width="16" height="5" rx="2" fill="rgba(255,255,255,0.65)" />
          <rect x="47" y="39" width="5" height="16" rx="2" fill="rgba(255,255,255,0.65)" />
          <rect x="22" y="64" width="56" height="30" rx="3" fill="rgba(255,255,255,0.04)" stroke="rgba(255,255,255,0.1)" strokeWidth="0.8" />
          <text x="50" y="78" textAnchor="middle" fill="rgba(255,255,255,0.7)" fontSize="6" fontFamily="serif" fontWeight="bold" letterSpacing="2">FAST</text>
          <text x="50" y="89" textAnchor="middle" fill="rgba(94,234,212,0.6)" fontSize="4.5" fontFamily="sans-serif" letterSpacing="1">DIST.</text>
          <ellipse cx="78" cy="26" rx="10" ry="5" fill={`url(#pill-${name})`} transform="rotate(-25 78 26)" opacity="0.8" />
        </svg>
      </div>
 
      <div className="text-center z-10">
        <p className="text-teal-100/80 text-sm font-light tracking-wide">{name}</p>
        <p className="text-teal-300 font-semibold mt-1 font-['Cormorant_Garamond',serif] text-lg">${formattedPrice}</p>
      </div>
 
      <button onClick={() => nav('catalog')} className="z-10 w-full mt-1 py-2 text-xs tracking-widest uppercase text-teal-400 border border-teal-500/30 rounded-full opacity-0 group-hover:opacity-100 hover:bg-teal-500/10 transition-all duration-300">
        Solicitar
      </button>
    </div>
  );
}