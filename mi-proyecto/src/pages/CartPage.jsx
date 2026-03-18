import { useState } from 'react';
import { useCart } from '../context/useCart';
import { getWishlist } from '../services/wishlist';
import { isAuthenticated } from '../services/auth';
import Navbar from '../components/Navbar';
import Footer from '../components/Footer';
 
const fmt = (n) => new Intl.NumberFormat('es-CO', { style: 'currency', currency: 'COP', maximumFractionDigits: 0 }).format(n);
 
export default function CartPage({ onNavigate }) {
  const { items, removeItem, updateQty, total, count, clearCart, addItem } = useCart();
  const [loadingFavorites, setLoadingFavorites] = useState(false);
  const [message, setMessage] = useState(null);

  const handleLoadFavorites = async () => {
    if (!isAuthenticated()) {
      setMessage('Debes iniciar sesión para cargar favoritos');
      setTimeout(() => setMessage(null), 3000);
      return;
    }

    try {
      setLoadingFavorites(true);
      const wishlist = await getWishlist();
      
      if (!wishlist || wishlist.length === 0) {
        setMessage('No tienes productos en favoritos');
        setTimeout(() => setMessage(null), 3000);
        return;
      }

      // Add each wishlist product to cart
      let addedCount = 0;
      for (const item of wishlist) {
        if (item.product && item.product.stock > 0) {
          addItem(item.product, 1);
          addedCount++;
        }
      }

      setMessage(`Se agregaron ${addedCount} productos de favoritos`);
      setTimeout(() => setMessage(null), 3000);
    } catch (err) {
      console.error('Error loading favorites:', err);
      setMessage('Error al cargar favoritos');
      setTimeout(() => setMessage(null), 3000);
    } finally {
      setLoadingFavorites(false);
    }
  };
 
  return (
    <div className="min-h-screen bg-[#050d1a]">
      <Navbar onNavigate={onNavigate} cartCount={count} />
 
      <div className="pt-28 pb-16 px-8">
        <div className="container mx-auto max-w-5xl">
 
          {/* Header */}
          {/* Message Toast */}
          {message && (
            <div className="fixed top-20 right-8 z-50 animate-fadeIn">
              <div className={`px-4 py-3 rounded-xl shadow-lg backdrop-blur-md border text-sm font-medium ${
                message.includes('Error') || message.includes('Debes') || message.includes('No tienes') ? 'bg-red-500/20 text-red-400 border-red-500/30' : 'bg-teal-500/20 text-teal-300 border-teal-500/30'}`}>
                {message}
              </div>
            </div>
          )}

          <div className="mb-8">
            <button onClick={() => onNavigate('catalog')}
              className="flex items-center gap-2 text-teal-400/60 hover:text-teal-300 text-sm mb-4 transition-colors">
              <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M15 19l-7-7 7-7" />
              </svg>
              Seguir comprando
            </button>
            <h1 className="font-['Cormorant_Garamond',serif] text-4xl font-bold text-white">
              Mi <span className="text-transparent bg-clip-text bg-gradient-to-r from-teal-300 to-amber-300">Pedido</span>
            </h1>
            <p className="text-teal-400/50 text-sm mt-1">{count} {count === 1 ? 'producto' : 'productos'} en el carrito</p>
          </div>
 
          {items.length === 0 ? (
            <div className="text-center py-32">
              <div className="text-6xl mb-4">📦</div>
              <p className="text-white text-xl font-light mb-2">Tu carrito está vacío</p>
              <p className="text-teal-400/40 text-sm mb-6">Agrega productos desde el catálogo</p>
              <div className="flex flex-col sm:flex-row gap-3 justify-center">
                <button onClick={() => onNavigate('catalog')}
                  className="px-8 py-3 bg-gradient-to-r from-teal-500 to-cyan-500 text-white rounded-xl text-sm font-semibold hover:shadow-[0_0_20px_rgba(20,184,166,0.4)] transition-all">
                  Ir al catálogo
                </button>
                <button onClick={handleLoadFavorites} disabled={loadingFavorites}
                  className="px-8 py-3 bg-pink-500/15 text-pink-300 border border-pink-500/30 rounded-xl text-sm font-semibold hover:bg-pink-500/25 transition-all flex items-center justify-center gap-2 disabled:opacity-50">
                  {loadingFavorites ? (
                    <span><span className="w-4 h-4 border-2 border-pink-400 border-t-transparent rounded-full animate-spin inline-block mr-2"></span>Cargando...</span>
                  ) : (
                    <span><svg className="w-5 h-5 inline-block mr-2" fill="currentColor" viewBox="0 0 24 24"><path d="M12 21.35l-1.45-1.32C5.4 15.36 2 12.28 2 8.5 2 5.42 4.42 3 7.5 3c1.74 0 3.41.81 4.5 2.09C13.09 3.81 14.76 3 16.5 3 19.58 3 22 5.42 22 8.5c0 3.78-3.4 6.86-8.55 11.54L12 21.35z" /></svg></span>
                  )}
                  Cargar favoritos
                </button>
                <button onClick={() => onNavigate('wishlist')}
                  className="px-8 py-3 bg-teal-500/10 text-teal-300 border border-teal-500/20 rounded-xl text-sm font-semibold hover:bg-teal-500/20 transition-all">
                  Ver favoritos
                </button>
              </div>
            </div>
          ) : (
            <div className="flex flex-col lg:flex-row gap-8">
              {/* Items */}
              <div className="flex-1 flex flex-col gap-4">
                {items.map(item => (
                  <div key={item.id}
                    className="bg-gradient-to-br from-[#0d2137] to-[#071525] border border-teal-500/15 rounded-2xl p-5 flex items-center gap-5">
                    {/* Mini icon */}
                    <div className="w-14 h-14 flex-shrink-0 flex items-center justify-center bg-teal-500/10 rounded-xl border border-teal-500/20">
                      <svg viewBox="0 0 40 45" className="w-9 h-auto" fill="none">
                        <defs>
                          <linearGradient id={`ci-${item.id}`} x1="0" y1="0" x2="1" y2="1">
                            <stop offset="0%" stopColor="#0d9488" /><stop offset="100%" stopColor="#164e63" />
                          </linearGradient>
                        </defs>
                        <rect x="5" y="12" width="30" height="30" rx="4" fill={`url(#ci-${item.id})`} />
                        <rect x="5" y="6" width="30" height="8" rx="3" fill="#2dd4bf" />
                        <rect x="17" y="19" width="6" height="2" rx="1" fill="rgba(255,255,255,0.6)" />
                        <rect x="19" y="17" width="2" height="6" rx="1" fill="rgba(255,255,255,0.6)" />
                      </svg>
                    </div>
 
                    <div className="flex-1 min-w-0">
                      <p className="text-white text-sm font-medium truncate">{item.name}</p>
                      <p className="text-teal-400/50 text-xs mt-0.5">{item.lab} · {item.category}</p>
                      <p className="text-teal-300 font-semibold mt-1 font-['Cormorant_Garamond',serif]">{fmt(item.price)}<span className="text-teal-400/40 text-xs font-normal"> / unid.</span></p>
                    </div>
 
                    {/* Qty */}
                    <div className="flex items-center gap-2">
                      <button onClick={() => updateQty(item.id, item.qty - item.minOrder)}
                        className="w-7 h-7 rounded-lg border border-teal-500/30 text-teal-300 hover:bg-teal-500/15 transition-all flex items-center justify-center text-lg leading-none">
                        −
                      </button>
                      <span className="text-white text-sm w-8 text-center font-medium">{item.qty}</span>
                      <button onClick={() => updateQty(item.id, item.qty + item.minOrder)}
                        className="w-7 h-7 rounded-lg border border-teal-500/30 text-teal-300 hover:bg-teal-500/15 transition-all flex items-center justify-center text-lg leading-none">
                        +
                      </button>
                    </div>
 
                    {/* Subtotal */}
                    <div className="text-right hidden sm:block min-w-[90px]">
                      <p className="text-white font-semibold text-sm">{fmt(item.price * item.qty)}</p>
                      <p className="text-teal-400/40 text-xs">{item.qty} unid.</p>
                    </div>
 
                    <button onClick={() => removeItem(item.id)}
                      className="text-teal-500/30 hover:text-red-400 transition-colors p-1">
                      <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M6 18L18 6M6 6l12 12" />
                      </svg>
                    </button>
                  </div>
                ))}
 
                <button onClick={clearCart}
                  className="self-start text-xs text-teal-500/40 hover:text-red-400 transition-colors mt-2">
                  Vaciar carrito
                </button>

                <button onClick={handleLoadFavorites} disabled={loadingFavorites}
                  className="self-start mt-2 ml-4 text-xs text-pink-400/60 hover:text-pink-300 transition-colors flex items-center gap-1 disabled:opacity-50">
                  {loadingFavorites ? (
                    <span><span className="w-3 h-3 border border-pink-400 border-t-transparent rounded-full animate-spin inline-block mr-1"></span>Cargando...</span>
                  ) : (
                    <span><svg className="w-3.5 h-3.5 inline-block mr-1" fill="currentColor" viewBox="0 0 24 24"><path d="M12 21.35l-1.45-1.32C5.4 15.36 2 12.28 2 8.5 2 5.42 4.42 3 7.5 3c1.74 0 3.41.81 4.5 2.09C13.09 3.81 14.76 3 16.5 3 19.58 3 22 5.42 22 8.5c0 3.78-3.4 6.86-8.55 11.54L12 21.35z" /></svg></span>
                  )}
                  Cargar favoritos
                </button>
                <button onClick={() => onNavigate('wishlist')}
                  className="self-start mt-2 ml-4 text-xs text-teal-400/60 hover:text-teal-300 transition-colors flex items-center gap-1">
                  <svg className="w-3.5 h-3.5" fill="currentColor" viewBox="0 0 24 24"><path d="M12 21.35l-1.45-1.32C5.4 15.36 2 12.28 2 8.5 2 5.42 4.42 3 7.5 3c1.74 0 3.41.81 4.5 2.09C13.09 3.81 14.76 3 16.5 3 19.58 3 22 5.42 22 8.5c0 3.78-3.4 6.86-8.55 11.54L12 21.35z" /></svg>
                  Ver favoritos
                </button>
              </div>
 
              {/* Summary */}
              <div className="lg:w-80 flex-shrink-0">
                <div className="sticky top-28 bg-gradient-to-br from-[#0d2137]/90 to-[#071525]/90 border border-teal-500/20 rounded-2xl p-6 backdrop-blur-sm">
                  <p className="text-teal-300 text-xs tracking-[0.2em] uppercase font-semibold mb-5">Resumen del pedido</p>
 
                  <div className="flex flex-col gap-3 mb-5">
                    {items.map(item => (
                      <div key={item.id} className="flex justify-between text-sm">
                        <span className="text-teal-100/50 truncate max-w-[160px]">{item.name.split(' ').slice(0, 3).join(' ')}…</span>
                        <span className="text-teal-100/70 ml-2">{fmt(item.price * item.qty)}</span>
                      </div>
                    ))}
                  </div>
 
                  <div className="w-full h-px bg-gradient-to-r from-transparent via-teal-500/20 to-transparent mb-4" />
 
                  <div className="flex justify-between items-center mb-6">
                    <span className="text-white font-semibold">Total</span>
                    <span className="font-['Cormorant_Garamond',serif] text-2xl font-bold text-teal-300">{fmt(total)}</span>
                  </div>
 
                  <button onClick={() => onNavigate('checkout')}
                    className="w-full py-3 bg-gradient-to-r from-teal-500 to-cyan-500 text-white font-semibold rounded-xl text-sm tracking-wide hover:shadow-[0_0_25px_rgba(20,184,166,0.45)] transition-all duration-300">
                    Proceder al pago →
                  </button>
                  <p className="text-center text-teal-400/30 text-xs mt-3">Solo para clientes B2B registrados</p>
                </div>
              </div>
            </div>
          )}
        </div>
      </div>
      <Footer onNavigate={onNavigate} />
    </div>
  );
}