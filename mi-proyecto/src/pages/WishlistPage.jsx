import { useState, useEffect } from 'react';
import { getWishlist, removeFromWishlist } from '../services/wishlist';
import { isAuthenticated, getUser } from '../services/auth';
import { useCart } from '../context/useCart';
import Navbar from '../components/Navbar';
import Footer from '../components/Footer';

const fmt = (n) => new Intl.NumberFormat('es-CO', { style: 'currency', currency: 'COP', maximumFractionDigits: 0 }).format(n);

function MedIcon() {
  return (
    <svg viewBox="0 0 80 90" className="w-16 h-auto drop-shadow-[0_4px_12px_rgba(20,184,166,0.35)]" fill="none">
      <defs>
        <linearGradient id="cardBox" x1="0" y1="0" x2="1" y2="1">
          <stop offset="0%" stopColor="#0d9488" stopOpacity="0.9" />
          <stop offset="100%" stopColor="#164e63" stopOpacity="0.95" />
        </linearGradient>
        <linearGradient id="cardTop" x1="0" y1="0" x2="1" y2="1">
          <stop offset="0%" stopColor="#2dd4bf" /><stop offset="100%" stopColor="#0891b2" />
        </linearGradient>
      </defs>
      <rect x="10" y="24" width="60" height="60" rx="6" fill="url(#cardBox)" />
      <rect x="10" y="14" width="60" height="14" rx="4" fill="url(#cardTop)" />
      <rect x="28" y="10" width="24" height="8" rx="3" fill="#2dd4bf" />
      <rect x="33" y="37" width="14" height="4" rx="2" fill="rgba(255,255,255,0.6)" />
      <rect x="38" y="32" width="4" height="14" rx="2" fill="rgba(255,255,255,0.6)" />
      <rect x="16" y="54" width="48" height="24" rx="3" fill="rgba(255,255,255,0.04)" stroke="rgba(255,255,255,0.1)" strokeWidth="0.8" />
      <text x="40" y="65" textAnchor="middle" fill="rgba(255,255,255,0.65)" fontSize="5" fontFamily="serif" fontWeight="bold" letterSpacing="2">FAST</text>
    </svg>
  );
}

function StockBadge({ stock }) {
  if (stock === 0) return <span className="text-xs px-2 py-0.5 rounded-full bg-red-500/15 text-red-400 border border-red-500/20">Sin stock</span>;
  if (stock < 100) return <span className="text-xs px-2 py-0.5 rounded-full bg-amber-500/15 text-amber-400 border border-amber-500/20">Stock bajo</span>;
  return <span className="text-xs px-2 py-0.5 rounded-full bg-teal-500/15 text-teal-400 border border-teal-500/20">Disponible</span>;
}

export default function WishlistPage({ onNavigate }) {
  const [wishlist, setWishlist] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [message, setMessage] = useState(null);
  const [addedId, setAddedId] = useState(null);
  const [removingId, setRemovingId] = useState(null);
  
  const { addItem } = useCart();
  
  const user = isAuthenticated() ? getUser() : null;
  const cartCount = 0; // Could be connected to cart context

  // Cargar wishlist al montar el componente
  useEffect(() => {
    const loadWishlist = async () => {
      if (!isAuthenticated()) {
        setError('Debes iniciar sesión para ver tus favoritos');
        setLoading(false);
        return;
      }

      try {
        setLoading(true);
        const data = await getWishlist();
        setWishlist(data);
        setError(null);
      } catch (err) {
        console.error('Error loading wishlist:', err);
        setError('No se pudieron cargar los favoritos');
      } finally {
        setLoading(false);
      }
    };

    loadWishlist();
  }, []);

  const handleRemove = async (productId) => {
    try {
      setRemovingId(productId);
      await removeFromWishlist(productId);
      setWishlist(prev => prev.filter(item => item.productId !== productId));
      setMessage('Producto eliminado de favoritos');
      setTimeout(() => setMessage(null), 3000);
    } catch (err) {
      console.error('Error removing from wishlist:', err);
      setMessage('Error al eliminar de favoritos');
      setTimeout(() => setMessage(null), 3000);
    } finally {
      setRemovingId(null);
    }
  };

  const handleAddToCart = (product) => {
    if (product.stock === 0) return;
    // Add to cart using CartContext
    addItem(product, 1);
    setAddedId(product.id);
    setMessage('Producto agregado al carrito');
    setTimeout(() => {
      setAddedId(null);
      setMessage(null);
    }, 1500);
  };

  return (
    <div className="min-h-screen bg-[#050d1a]">
      <Navbar onNavigate={onNavigate} cartCount={cartCount} />

      {/* Message Toast */}
      {message && (
        <div className="fixed top-20 right-8 z-50 animate-fadeIn">
          <div className={`px-4 py-3 rounded-xl shadow-lg backdrop-blur-md border text-sm font-medium
            ${message.includes('Error') ? 'bg-red-500/20 text-red-400 border-red-500/30' : 'bg-teal-500/20 text-teal-300 border-teal-500/30'}`}>
            {message}
          </div>
        </div>
      )}

      {/* Header */}
      <div className="pt-24 pb-10 px-8 bg-gradient-to-b from-[#071525] to-[#050d1a] border-b border-teal-500/10">
        <div className="container mx-auto">
          <p className="text-teal-400/60 tracking-[0.3em] text-xs uppercase mb-2">
            {user ? `Hola, ${user.username}` : 'Mis favoritos'}
          </p>
          <h1 className="font-['Cormorant_Garamond',serif] text-4xl md:text-5xl font-bold text-white">
            Mis{' '}
            <span className="text-transparent bg-clip-text bg-gradient-to-r from-pink-400 to-rose-400">Favoritos</span>
          </h1>
          <p className="text-teal-100/40 text-sm mt-2">{wishlist.length} productos en tu lista</p>
        </div>
      </div>

      <div className="container mx-auto px-8 py-10">
        {/* Loading */}
        {loading ? (
          <div className="text-center py-24">
            <div className="inline-block w-8 h-8 border-2 border-teal-500 border-t-transparent rounded-full animate-spin"></div>
            <p className="text-teal-400/40 mt-4">Cargando favoritos...</p>
          </div>
        ) : error ? (
          <div className="text-center py-24">
            <div className="text-6xl mb-4">🔐</div>
            <p className="text-teal-100/60 text-lg mb-4">{error}</p>
            <button 
              onClick={() => onNavigate('login')}
              className="px-6 py-3 bg-teal-500 text-white rounded-xl font-semibold hover:bg-teal-600 transition-colors"
            >
              Iniciar Sesión
            </button>
          </div>
        ) : wishlist.length === 0 ? (
          <div className="text-center py-24">
            <div className="text-6xl mb-4">💝</div>
            <p className="text-teal-100/60 text-lg mb-2">No tienes favoritos aún</p>
            <p className="text-teal-400/40 text-sm mb-6">Agrega productos al catálogo para verlos aquí</p>
            <button 
              onClick={() => onNavigate('catalog')}
              className="px-6 py-3 bg-teal-500/15 text-teal-300 border border-teal-500/30 rounded-xl font-semibold hover:bg-teal-500/25 transition-colors"
            >
              Ver Catálogo
            </button>
          </div>
        ) : (
          <div className="grid grid-cols-1 sm:grid-cols-2 xl:grid-cols-3 gap-5">
            {wishlist.map(item => (
              <div key={item.id}
                className="group bg-gradient-to-br from-[#0d2137] to-[#071525] border border-teal-500/15 rounded-2xl p-5 flex flex-col hover:border-teal-400/35 hover:shadow-[0_0_25px_rgba(20,184,166,0.12)] transition-all duration-400 relative">

                {/* Remove Button */}
                <button
                  onClick={() => handleRemove(item.productId)}
                  disabled={removingId === item.productId}
                  className="absolute top-3 right-3 z-10 p-2 rounded-full bg-pink-500/10 text-pink-400/70 hover:bg-pink-500/20 hover:text-pink-400 transition-all"
                  title="Quitar de favoritos"
                >
                  {removingId === item.productId ? (
                    <div className="w-5 h-5 border-2 border-pink-400 border-t-transparent rounded-full animate-spin" />
                  ) : (
                    <svg className="w-5 h-5" fill="currentColor" stroke="currentColor" viewBox="0 0 24 24">
                      <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M4.318 6.318a4.5 4.5 0 000 6.364L12 20.364l7.682-7.682a4.5 4.5 0 00-6.364-6.364L12 7.636l-1.318-1.318a4.5 4.5 0 00-6.364 0z" />
                    </svg>
                  )}
                </button>

                {/* Icon */}
                <div className="flex justify-center mb-4 mt-2">
                  <div className="relative">
                    <div className="absolute inset-0 bg-teal-400/10 rounded-full blur-xl group-hover:bg-teal-400/20 transition-all duration-500" />
                    <MedIcon />
                  </div>
                </div>

                {/* Info */}
                <div className="flex-1">
                  <div className="flex items-start justify-between gap-2 mb-2">
                    <p className="text-white text-sm font-medium leading-snug">{item.product?.name || 'Producto'}</p>
                    <StockBadge stock={item.product?.stock || 0} />
                  </div>
                  <div className="flex items-center gap-2 mb-1">
                    <span className="text-xs text-teal-400/60 bg-teal-500/10 px-2 py-0.5 rounded-full">{item.product?.category || 'Sin categoría'}</span>
                    <span className="text-xs text-teal-400/50">{item.product?.laboratory || ''}</span>
                  </div>
                  {item.addedAt && (
                    <p className="text-xs text-teal-100/35 mt-1">
                      Agregado: {new Date(item.addedAt).toLocaleDateString('es-CO')}
                    </p>
                  )}
                </div>

                <div className="mt-4 flex items-center justify-between">
                  <p className="font-['Cormorant_Garamond',serif] text-xl font-bold text-teal-300">
                    {fmt(item.product?.price || 0)}
                  </p>
                  <button
                    onClick={() => handleAddToCart(item.product)}
                    disabled={!item.product?.stock || addedId === item.productId}
                    className={`px-4 py-2 rounded-xl text-xs font-semibold tracking-wide transition-all duration-300
                      {!item.product?.stock
                        ? 'bg-teal-500/5 text-teal-500/30 cursor-not-allowed border border-teal-500/10'
                        : addedId === item.productId
                          ? 'bg-teal-500 text-white shadow-[0_0_16px_rgba(20,184,166,0.5)]'
                          : 'bg-teal-500/15 text-teal-300 border border-teal-500/30 hover:bg-teal-500/30'}`}>
                    {addedId === item.productId ? '✓ Agregado' : !item.product?.stock ? 'Sin stock' : 'Agregar'}
                  </button>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>

      <Footer onNavigate={onNavigate} />
    </div>
  );
}
