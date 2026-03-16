import { useCart } from '../context/CartContext';
import Navbar from '../components/Navbar';
import Footer from '../components/Footer';
 
const fmt = (n) => new Intl.NumberFormat('es-CO', { style: 'currency', currency: 'COP', maximumFractionDigits: 0 }).format(n);
 
export default function CartPage({ onNavigate }) {
  const { items, removeItem, updateQty, total, count, clearCart } = useCart();
 
  return (
    <div className="min-h-screen bg-[#050d1a]">
      <Navbar onNavigate={onNavigate} cartCount={count} />
 
      <div className="pt-28 pb-16 px-8">
        <div className="container mx-auto max-w-5xl">
 
          {/* Header */}
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
              <button onClick={() => onNavigate('catalog')}
                className="px-8 py-3 bg-gradient-to-r from-teal-500 to-cyan-500 text-white rounded-xl text-sm font-semibold hover:shadow-[0_0_20px_rgba(20,184,166,0.4)] transition-all">
                Ir al catálogo
              </button>
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