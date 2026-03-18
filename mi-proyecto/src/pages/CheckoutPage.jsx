import { useState, useEffect } from 'react';
import { useCart } from '../context/useCart';
import { getUser, getUserProfile } from '../services/auth';
// import { checkout } from '../services/orders'; // TODO: usar cuando se implemente checkout real
import Navbar from '../components/Navbar';
import Footer from '../components/Footer';

const fmt = (n) => new Intl.NumberFormat('es-CO', { style: 'currency', currency: 'COP', maximumFractionDigits: 0 }).format(n);

export default function CheckoutPage({ onNavigate }) {
  const { items, total, count, clearCart } = useCart();

  const [form, setForm] = useState({
    razonSocial: '',
    nit: '',
    regSanitario: '',
    email: '',
    telefono: '',
    ciudad: '',
    direccion: '',
    notas: '',
  });
  const [loading, setLoading] = useState(false);
  const [profileLoading, setProfileLoading] = useState(true);

  // Cargar datos del perfil del usuario al iniciar
  useEffect(() => {
    const loadProfile = async () => {
      try {
        const profile = await getUserProfile();
        
        // Pre-llenar formulario con datos del perfil
        if (profile) {
          setForm(prev => ({
            ...prev,
            // Datos de la empresa
            razonSocial: profile.company?.businessName || prev.razonSocial,
            nit: profile.company?.nit || prev.nit,
            email: profile.email || profile.company?.email || prev.email,
            telefono: profile.phone || profile.company?.phone || prev.telefono,
            direccion: profile.company?.address || prev.direccion,
          }));
        }
      } catch (error) {
        console.error('Error cargando perfil:', error);
        // Si falla, usamos los datos guardados localmente
        const user = getUser();
        if (user) {
          setForm(prev => ({
            ...prev,
            email: user.email || prev.email,
          }));
        }
      } finally {
        setProfileLoading(false);
      }
    };
    
    loadProfile();
  }, []);

  const handleChange = (e) => setForm(f => ({ ...f, [e.target.name]: e.target.value }));

  const handlePay = async (e) => {
    e.preventDefault();
    setLoading(true);

    try {
      const user = getUser();
      if (!user) {
        throw new Error('Debes iniciar sesión para realizar el pedido');
      }

      // Preparar datos para el checkout
      // eslint-disable-next-line no-unused-vars
      const shippingAddress = `${form.direccion}, ${form.ciudad}`;
      // eslint-disable-next-line no-unused-vars
      const notes = form.notas;

      // Por ahora, simulamos el checkout ya que el flujo de Mercado Pago
      // requeriría integración adicional con la API de Mercado Pago
      // TODO: Integrar con Mercado Pago para crear preferencia de pago
      
      // Ejemplo de cómo sería con el backend:
      // const order = await checkout(cartId, shippingAddress, notes);
      // const preference = await createPaymentPreference(order.id);
      // window.location.href = preference.init_point;

      // Simulación por ahora - mostrar éxito
      await new Promise(resolve => setTimeout(resolve, 2000));
      alert('Pedido realizado con éxito! (Simulación)');
      
      // Limpiar carrito después del pedido
      await clearCart();
      
      // Navegar a home
      onNavigate('home');
    } catch (error) {
      console.error('Error en checkout:', error);
      alert(error.message || 'Error al procesar el pedido');
    } finally {
      setLoading(false);
    }
  };

  const isValid = form.razonSocial && form.nit && form.email && form.telefono && form.ciudad && form.direccion;

  return (
    <div className="min-h-screen bg-[#050d1a]">
      <Navbar onNavigate={onNavigate} cartCount={count} />

      <div className="pt-28 pb-16 px-8">
        <div className="container mx-auto max-w-5xl">

          {/* Header */}
          <div className="mb-8">
            <button onClick={() => onNavigate('cart')}
              className="flex items-center gap-2 text-teal-400/60 hover:text-teal-300 text-sm mb-4 transition-colors">
              <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M15 19l-7-7 7-7" />
              </svg>
              Volver al carrito
            </button>
            <h1 className="font-['Cormorant_Garamond',serif] text-4xl font-bold text-white">
              Datos del <span className="text-transparent bg-clip-text bg-gradient-to-r from-teal-300 to-amber-300">Pedido</span>
            </h1>
            {/* Steps */}
            <div className="flex items-center gap-3 mt-4">
              {[['1', 'Catálogo', false], ['2', 'Carrito', false], ['3', 'Datos', true], ['4', 'Pago', false]].map(([n, label, active]) => (
                <div key={n} className="flex items-center gap-2">
                  <div className={`w-6 h-6 rounded-full flex items-center justify-center text-xs font-bold transition-all
                    ${active ? 'bg-gradient-to-br from-teal-400 to-cyan-500 text-white shadow-[0_0_12px_rgba(20,184,166,0.5)]'
                      : 'border border-teal-500/30 text-teal-500/40'}`}>
                    {n}
                  </div>
                  <span className={`text-xs hidden sm:block ${active ? 'text-teal-300' : 'text-teal-500/30'}`}>{label}</span>
                  {n !== '4' && <div className="w-6 h-px bg-teal-500/20" />}
                </div>
              ))}
            </div>
          </div>

          <form onSubmit={handlePay} className="flex flex-col lg:flex-row gap-8">
            {/* Form */}
            <div className="flex-1 flex flex-col gap-6">
              {/* Datos empresa */}
              <div className="bg-gradient-to-br from-[#0d2137]/80 to-[#071525]/80 border border-teal-500/15 rounded-2xl p-6">
                <p className="text-teal-300 text-xs tracking-[0.2em] uppercase font-semibold mb-5">Datos de la empresa</p>
                {profileLoading ? (
                  <div className="flex items-center justify-center py-8">
                    <div className="flex items-center gap-3 text-teal-400/60">
                      <svg className="w-5 h-5 animate-spin" fill="none" viewBox="0 0 24 24">
                        <circle className="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" strokeWidth="4" />
                        <path className="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4z" />
                      </svg>
                      <span className="text-sm">Cargando datos...</span>
                    </div>
                  </div>
                ) : (
                  <>
                    <div className="grid sm:grid-cols-2 gap-4">
                      <div className="sm:col-span-2">
                        <label className="text-teal-400/60 text-xs mb-1.5 block">Razón Social *</label>
                        <input name="razonSocial" value={form.razonSocial} onChange={handleChange} required
                          placeholder="Farmacia El Bienestar S.A.S."
                          className="w-full bg-[#071525] border border-teal-500/20 rounded-xl px-4 py-2.5 text-sm text-teal-100 placeholder-teal-500/25 outline-none focus:border-teal-400/50 transition-colors" />
                      </div>
                      <div>
                        <label className="text-teal-400/60 text-xs mb-1.5 block">NIT *</label>
                        <input name="nit" value={form.nit} onChange={handleChange} required
                          placeholder="900.123.456-7"
                          className="w-full bg-[#071525] border border-teal-500/20 rounded-xl px-4 py-2.5 text-sm text-teal-100 placeholder-teal-500/25 outline-none focus:border-teal-400/50 transition-colors" />
                      </div>
                      <div>
                        <label className="text-teal-400/60 text-xs mb-1.5 block">Registro Sanitario</label>
                        <input name="regSanitario" value={form.regSanitario} onChange={handleChange}
                          placeholder="INVIMA 2024-DES-001"
                          className="w-full bg-[#071525] border border-teal-500/20 rounded-xl px-4 py-2.5 text-sm text-teal-100 placeholder-teal-500/25 outline-none focus:border-teal-400/50 transition-colors" />
                      </div>
                      <div>
                        <label className="text-teal-400/60 text-xs mb-1.5 block">Correo electrónico *</label>
                        <input name="email" type="email" value={form.email} onChange={handleChange} required
                          placeholder="compras@farmacia.com"
                          className="w-full bg-[#071525] border border-teal-500/20 rounded-xl px-4 py-2.5 text-sm text-teal-100 placeholder-teal-500/25 outline-none focus:border-teal-400/50 transition-colors" />
                      </div>
                      <div>
                        <label className="text-teal-400/60 text-xs mb-1.5 block">Teléfono *</label>
                        <input name="telefono" value={form.telefono} onChange={handleChange} required
                          placeholder="+57 300 123 4567"
                          className="w-full bg-[#071525] border border-teal-500/20 rounded-xl px-4 py-2.5 text-sm text-teal-100 placeholder-teal-500/25 outline-none focus:border-teal-400/50 transition-colors" />
                      </div>
                    </div>
                  </>
                )}
              </div>

              {/* Dirección */}
              <div className="bg-gradient-to-br from-[#0d2137]/80 to-[#071525]/80 border border-teal-500/15 rounded-2xl p-6">
                <p className="text-teal-300 text-xs tracking-[0.2em] uppercase font-semibold mb-5">Dirección de entrega</p>
                <div className="grid sm:grid-cols-2 gap-4">
                  <div>
                    <label className="text-teal-400/60 text-xs mb-1.5 block">Ciudad *</label>
                    <input name="ciudad" value={form.ciudad} onChange={handleChange} required
                      placeholder="Bogotá, D.C."
                      className="w-full bg-[#071525] border border-teal-500/20 rounded-xl px-4 py-2.5 text-sm text-teal-100 placeholder-teal-500/25 outline-none focus:border-teal-400/50 transition-colors" />
                  </div>
                  <div>
                    <label className="text-teal-400/60 text-xs mb-1.5 block">Dirección *</label>
                    <input name="direccion" value={form.direccion} onChange={handleChange} required
                      placeholder="Cra. 7 # 32-16"
                      className="w-full bg-[#071525] border border-teal-500/20 rounded-xl px-4 py-2.5 text-sm text-teal-100 placeholder-teal-500/25 outline-none focus:border-teal-400/50 transition-colors" />
                  </div>
                  <div className="sm:col-span-2">
                    <label className="text-teal-400/60 text-xs mb-1.5 block">Notas adicionales</label>
                    <textarea name="notas" value={form.notas} onChange={handleChange} rows={2}
                      placeholder="Horario de recepción, datos adicionales..."
                      className="w-full bg-[#071525] border border-teal-500/20 rounded-xl px-4 py-2.5 text-sm text-teal-100 placeholder-teal-500/25 outline-none focus:border-teal-400/50 transition-colors resize-none" />
                  </div>
                </div>
              </div>
            </div>

            {/* Order summary + pay */}
            <div className="lg:w-80 flex-shrink-0">
              <div className="sticky top-28 flex flex-col gap-4">
                {/* Summary */}
                <div className="bg-gradient-to-br from-[#0d2137]/90 to-[#071525]/90 border border-teal-500/20 rounded-2xl p-6 backdrop-blur-sm">
                  <p className="text-teal-300 text-xs tracking-[0.2em] uppercase font-semibold mb-4">Resumen</p>
                  <div className="flex flex-col gap-2 mb-4 max-h-48 overflow-y-auto pr-1">
                    {items.map(item => (
                      <div key={item.id} className="flex justify-between text-xs">
                        <span className="text-teal-100/50 truncate max-w-[150px]">{item.name.split(' ').slice(0, 3).join(' ')}… ×{item.qty}</span>
                        <span className="text-teal-100/70 ml-2 flex-shrink-0">{fmt(item.price * item.qty)}</span>
                      </div>
                    ))}
                  </div>
                  <div className="w-full h-px bg-gradient-to-r from-transparent via-teal-500/20 to-transparent mb-3" />
                  <div className="flex justify-between items-center">
                    <span className="text-white font-semibold text-sm">Total pedido</span>
                    <span className="font-['Cormorant_Garamond',serif] text-2xl font-bold text-teal-300">{fmt(total)}</span>
                  </div>
                </div>

                {/* MP Button */}
                <button type="submit" disabled={!isValid || loading}
                  className={`w-full py-4 rounded-2xl font-bold text-sm tracking-wide flex items-center justify-center gap-3 transition-all duration-300
                    ${isValid && !loading
                      ? 'bg-[#009ee3] hover:bg-[#007eb5] text-white shadow-[0_0_30px_rgba(0,158,227,0.4)] hover:shadow-[0_0_40px_rgba(0,158,227,0.6)]'
                      : 'bg-[#009ee3]/30 text-white/30 cursor-not-allowed'}`}>
                  {loading ? (
                    <>
                      <svg className="w-5 h-5 animate-spin" fill="none" viewBox="0 0 24 24">
                        <circle className="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" strokeWidth="4" />
                        <path className="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4z" />
                      </svg>
                      Redirigiendo...
                    </>
                  ) : (
                    <>
                      {/* Mercado Pago logo */}
                      <svg viewBox="0 0 32 32" className="w-6 h-6" fill="none">
                        <circle cx="16" cy="16" r="16" fill="white" fillOpacity="0.15" />
                        <text x="16" y="21" textAnchor="middle" fill="white" fontSize="11" fontWeight="bold" fontFamily="sans-serif">MP</text>
                      </svg>
                      Pagar con Mercado Pago
                    </>
                  )}
                </button>

                {/* Trust badges */}
                <div className="flex items-center justify-center gap-4 text-teal-400/30 text-xs">
                  <span className="flex items-center gap-1">
                    <svg className="w-3 h-3" fill="currentColor" viewBox="0 0 20 20">
                      <path fillRule="evenodd" d="M5 9V7a5 5 0 0110 0v2a2 2 0 012 2v5a2 2 0 01-2 2H5a2 2 0 01-2-2v-5a2 2 0 012-2zm8-2v2H7V7a3 3 0 016 0z" clipRule="evenodd" />
                    </svg>
                    Pago seguro
                  </span>
                  <span>·</span>
                  <span>SSL cifrado</span>
                  <span>·</span>
                  <span>INVIMA</span>
                </div>

                {!isValid && (
                  <p className="text-center text-amber-400/50 text-xs">Completa todos los campos obligatorios (*)</p>
                )}
              </div>
            </div>
          </form>
        </div>
      </div>
      <Footer onNavigate={onNavigate} />
    </div>
  );
}