import { useState } from 'react';
import { isAuthenticated, getUser, clearAuthTokens } from '../services/auth';
import { useCart } from '../context/useCart';

export default function Navbar({ onNavigate, cartCount = 0 }) {
  const nav = onNavigate || (() => {});
  const [showDropdown, setShowDropdown] = useState(false);
  const { setAuthCheck } = useCart();

  // Siempre verificar auth directamente (no useEffect para evitar problemas de renderizado)
  const user = isAuthenticated() ? getUser() : null;

  const handleLogout = () => {
    clearAuthTokens();
    // Notificar al carrito que cambió la autenticación
    setAuthCheck(prev => prev + 1);
    setShowDropdown(false);
    nav('home');
  };

  const handleNav = (target) => {
    if (target === 'nosotros' || target === 'contacto') {
      const element = document.getElementById(target);
      if (element) {
        element.scrollIntoView({ behavior: 'smooth' });
      }
    } else {
      nav(target);
    }
    setShowDropdown(false);
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

        {user ? (
          /* Usuario logueado - mostrar dropdown */
          <div className="relative">
            <button 
              onClick={() => setShowDropdown(!showDropdown)}
              className="flex items-center gap-2 border border-teal-500/30 text-teal-300 hover:bg-teal-500/10 px-4 py-1.5 rounded-full text-sm transition-all duration-300"
            >
              <div className="w-7 h-7 rounded-full bg-gradient-to-br from-teal-400 to-cyan-500 flex items-center justify-center">
                <span className="text-white text-xs font-bold">
                  {user.username ? user.username.charAt(0).toUpperCase() : 'U'}
                </span>
              </div>
              <span className="font-medium hidden md:inline">{user.username}</span>
              <svg className={`w-3 h-3 transition-transform ${showDropdown ? 'rotate-180' : ''}`} fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 9l-7 7-7-7" />
              </svg>
            </button>
            
            {showDropdown && (
              <div className="absolute right-0 mt-3 w-64 bg-[#0d2137]/95 backdrop-blur-md border border-teal-500/20 rounded-xl shadow-2xl overflow-hidden z-50 animate-fadeIn">
                {/* User Info Header */}
                <div className="px-4 py-3 bg-gradient-to-r from-teal-500/10 to-cyan-500/10 border-b border-teal-500/10">
                  <div className="flex items-center gap-3">
                    <div className="w-10 h-10 rounded-full bg-gradient-to-br from-teal-400 to-cyan-500 flex items-center justify-center">
                      <span className="text-white font-bold">
                        {user.username ? user.username.charAt(0).toUpperCase() : 'U'}
                      </span>
                    </div>
                    <div className="flex-1 min-w-0">
                      <p className="text-white font-medium text-sm truncate">{user.username || 'Usuario'}</p>
                      <p className="text-teal-400/60 text-xs truncate">{user.email}</p>
                    </div>
                  </div>
                </div>
                
                {/* Menu Options */}
                <div className="py-2">
                  <button 
                    onClick={() => handleNav('cart')}
                    className="w-full flex items-center gap-3 px-4 py-2.5 text-teal-100/80 hover:bg-teal-500/10 hover:text-teal-300 transition-all"
                  >
                    <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M3 3h2l.4 2M7 13h10l4-8H5.4M7 13L5.4 5M7 13l-2.293 2.293c-.63.63-.184 1.707.707 1.707H17m0 0a2 2 0 100 4 2 2 0 000-4zm-8 2a2 2 0 11-4 0 2 2 0 014 0z" />
                    </svg>
                    <span className="text-sm">Mi Carrito</span>
                    {cartCount > 0 && (
                      <span className="ml-auto bg-amber-400/20 text-amber-400 text-xs px-2 py-0.5 rounded-full">
                        {cartCount}
                      </span>
                    )}
                  </button>
                  
                  <button 
                    onClick={() => handleNav('catalog')}
                    className="w-full flex items-center gap-3 px-4 py-2.5 text-teal-100/80 hover:bg-teal-500/10 hover:text-teal-300 transition-all"
                  >
                    <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M4 6a2 2 0 012-2h2a2 2 0 012 2v2a2 2 0 01-2 2H6a2 2 0 01-2-2V6zM14 6a2 2 0 012-2h2a2 2 0 012 2v2a2 2 0 01-2 2h-2a2 2 0 01-2-2V6zM4 16a2 2 0 012-2h2a2 2 0 012 2v2a2 2 0 01-2 2H6a2 2 0 01-2-2v-2zM14 16a2 2 0 012-2h2a2 2 0 012 2v2a2 2 0 01-2 2h-2a2 2 0 01-2-2v-2z" />
                    </svg>
                    <span className="text-sm">Catálogo</span>
                  </button>
                  
                  <button 
                    onClick={() => handleNav('wishlist')}
                    className="w-full flex items-center gap-3 px-4 py-2.5 text-teal-100/80 hover:bg-teal-500/10 hover:text-teal-300 transition-all"
                  >
                    <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M4.318 6.318a4.5 4.5 0 000 6.364L12 20.364l7.682-7.682a4.5 4.5 0 00-6.364-6.364L12 7.636l-1.318-1.318a4.5 4.5 0 00-6.364 0z" />
                    </svg>
                    <span className="text-sm">Favoritos</span>
                  </button>
                </div>
                
                {/* Logout Button */}
                <div className="border-t border-teal-500/10 py-2">
                  <button 
                    onClick={handleLogout}
                    className="w-full flex items-center gap-3 px-4 py-2.5 text-red-400 hover:bg-red-500/10 transition-all"
                  >
                    <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M17 16l4-4m0 0l-4-4m4 4H7m6 4v1a3 3 0 01-3 3H6a3 3 0 01-3-3V7a3 3 0 013-3h4a3 3 0 013 3v1" />
                    </svg>
                    <span className="text-sm font-medium">Cerrar Sesión</span>
                  </button>
                </div>
              </div>
            )}
          </div>
        ) : (
          /* Usuario no logueado - mostrar botones de login/register */
          <>
            <button onClick={() => nav('login')} className="flex items-center gap-2 border border-teal-500/30 text-teal-300 hover:bg-teal-500/10 px-4 py-1.5 rounded-full text-sm transition-all duration-300">
              <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
              </svg>
              Iniciar sesión
            </button>

            <button onClick={() => nav('register')} className="flex items-center gap-2 bg-gradient-to-r from-teal-500 to-cyan-500 text-white px-4 py-1.5 rounded-full text-sm font-semibold transition-all duration-300 hover:shadow-[0_0_20px_rgba(20,184,166,0.4)]">
              Registrarse
            </button>
          </>
        )}

        {user?.role === 'ADMIN' && (
          <button onClick={() => nav('admin-login')} className="flex items-center gap-2 border border-amber-500/30 text-amber-400 hover:bg-amber-500/10 px-4 py-1.5 rounded-full text-sm transition-all duration-300">
            <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M10.325 4.317c.426-1.756 2.924-1.756 3.35 0a1.724 1.724 0 002.573 1.066c1.543-.94 3.31.826 2.37 2.37a1.724 1.724 0 001.065 2.572c1.756.426 1.756 2.924 0 3.35a1.724 1.724 0 00-1.066 2.573c.94 1.543-.826 3.31-2.37 2.37a1.724 1.724 0 00-2.572 1.065c-.426 1.756-2.924 1.756-3.35 0a1.724 1.724 0 00-2.573-1.066c-1.543.94-3.31-.826-2.37-2.37a1.724 1.724 0 00-1.065-2.572c-1.756-.426-1.756-2.924 0-3.35a1.724 1.724 0 001.066-2.573c-.94-1.543.826-3.31 2.37-2.37.996.608 2.296.07 2.572-1.065z" />
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M15 12a3 3 0 11-6 0 3 3 0 016 0z" />
            </svg>
            Admin
          </button>
        )}
      </div>
    </nav>
  );
}
