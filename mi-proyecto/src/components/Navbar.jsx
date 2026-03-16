export default function Navbar({ onNavigate, cartCount = 0 }) {
  const nav = onNavigate || (() => {});

  const handleNav = (target) => {
    if (target === 'nosotros' || target === 'contacto') {
      const element = document.getElementById(target);
      if (element) {
        element.scrollIntoView({ behavior: 'smooth' });
      }
    } else {
      nav(target);
    }
  };
 
  return (
    <nav className="fixed top-0 left-0 right-0 z-50 flex items-center justify-between px-8 py-4 bg-[#0a1628]/80 backdrop-blur-md border-b border-teal-500/10">
      <button onClick={() => nav('home')}
        className="text-2xl font-bold text-white tracking-widest font-['Cormorant_Garamond',serif] hover:text-teal-300 transition-colors">
        FAST
      </button>
 
      <ul className="hidden md:flex gap-8 text-sm text-teal-100/70 font-light tracking-wide">
        {[['Inicio', 'home'], ['Productos', 'catalog'], ['Nosotros', 'nosotros'], ['Contacto', 'contacto']].map(([label, target]) => (
          <li key={label}>
            <button onClick={() => handleNav(target)} className="hover:text-teal-300 transition-colors duration-300">{label}</button>
          </li>
        ))}
      </ul>
 
      <div className="flex items-center gap-3">
        <div className="hidden md:flex items-center gap-2 bg-teal-900/30 border border-teal-500/20 rounded-full px-4 py-1.5">
          <svg className="w-4 h-4 text-teal-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
          </svg>
          <span className="text-teal-300/50 text-sm">Buscar...</span>
        </div>
 
        {/* Cart */}
        <button onClick={() => nav('cart')}
          className="relative flex items-center gap-2 border border-teal-500/30 text-teal-300 hover:bg-teal-500/10 px-3 py-1.5 rounded-full text-sm transition-all duration-300">
          <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M3 3h2l.4 2M7 13h10l4-8H5.4M7 13L5.4 5M7 13l-2.293 2.293c-.63.63-.184 1.707.707 1.707H17m0 0a2 2 0 100 4 2 2 0 000-4zm-8 2a2 2 0 11-4 0 2 2 0 014 0z" />
          </svg>
          {cartCount > 0 && (
            <span className="absolute -top-1.5 -right-1.5 w-5 h-5 rounded-full bg-amber-400 text-[#050d1a] text-xs font-bold flex items-center justify-center">
              {cartCount}
            </span>
          )}
        </button>
 
        <button onClick={() => nav('login')} className="flex items-center gap-2 border border-teal-500/30 text-teal-300 hover:bg-teal-500/10 px-4 py-1.5 rounded-full text-sm transition-all duration-300">
          <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
          </svg>
          Iniciar sesión
        </button>

        <button onClick={() => nav('admin-login')} className="flex items-center gap-2 border border-amber-500/30 text-amber-400 hover:bg-amber-500/10 px-4 py-1.5 rounded-full text-sm transition-all duration-300">
          <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M10.325 4.317c.426-1.756 2.924-1.756 3.35 0a1.724 1.724 0 002.573 1.066c1.543-.94 3.31.826 2.37 2.37a1.724 1.724 0 001.065 2.572c1.756.426 1.756 2.924 0 3.35a1.724 1.724 0 00-1.066 2.573c.94 1.543-.826 3.31-2.37 2.37a1.724 1.724 0 00-2.572 1.065c-.426 1.756-2.924 1.756-3.35 0a1.724 1.724 0 00-2.573-1.066c-1.543.94-3.31-.826-2.37-2.37a1.724 1.724 0 00-1.065-2.572c-1.756-.426-1.756-2.924 0-3.35a1.724 1.724 0 001.066-2.573c-.94-1.543.826-3.31 2.37-2.37.996.608 2.296.07 2.572-1.065z" />
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M15 12a3 3 0 11-6 0 3 3 0 016 0z" />
          </svg>
          Admin
        </button>
 
        <button onClick={() => nav('login')} className="flex items-center gap-2 bg-gradient-to-r from-teal-500 to-cyan-500 text-white px-4 py-1.5 rounded-full text-sm font-semibold transition-all duration-300 hover:shadow-[0_0_20px_rgba(20,184,166,0.4)]">
          Registrarse
        </button>
      </div>
    </nav>
  );
}