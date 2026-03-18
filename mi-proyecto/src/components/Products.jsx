import { useState, useEffect } from 'react';
import ProductCard from './ProductCard';
import { getProducts } from '../services/products';

export default function Products({ onNavigate }) {
  const nav = onNavigate || (() => {});
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchProducts = async () => {
      try {
        const data = await getProducts();
        // Tomar solo los primeros 6 productos para el home
        setProducts(data.slice(0, 6));
      } catch (error) {
        console.error('Error loading products:', error);
        // Si hay error, no mostrar productos
        setProducts([]);
      } finally {
        setLoading(false);
      }
    };

    fetchProducts();
  }, []);

  return (
    <section className="py-24 bg-gradient-to-b from-[#061525] via-[#081a2e] to-[#061525] relative overflow-hidden">
      <div className="absolute top-0 left-1/2 -translate-x-1/2 w-[800px] h-[400px] bg-teal-500/5 rounded-full blur-[100px] pointer-events-none" />
 
      <div className="container mx-auto px-8">
        <div className="text-center mb-14">
          <p className="text-teal-400/60 tracking-[0.3em] text-xs uppercase mb-3">Catálogo</p>
          <h2 className="font-['Cormorant_Garamond',serif] text-4xl md:text-5xl font-bold text-white">
            Amplio portafolio de{' '}
            <span className="text-transparent bg-clip-text bg-gradient-to-r from-teal-300 to-amber-300">
              Medicamentos
            </span>
          </h2>
          <div className="mt-4 flex justify-center">
            <div className="w-16 h-0.5 bg-gradient-to-r from-transparent via-teal-400 to-transparent" />
          </div>
        </div>
 
        {loading ? (
          <div className="flex justify-center py-12">
            <div className="w-8 h-8 border-2 border-teal-500 border-t-transparent rounded-full animate-spin"></div>
          </div>
        ) : products.length === 0 ? (
          <div className="text-center py-12 text-teal-400/40">
            <p>No hay productos disponibles</p>
            <p className="text-sm mt-2">Inicia sesión para ver el catálogo</p>
          </div>
        ) : (
          <div className="grid grid-cols-2 md:grid-cols-3 gap-5">
            {products.map((product, i) => (
              <ProductCard 
                key={product.id} 
                {...product} 
                price={product.price}
                onNavigate={nav} 
                delay={i * 80} 
              />
            ))}
          </div>
        )}

        <div className="text-center mt-12">
          <button onClick={() => nav('catalog')} className="px-10 py-3 border border-teal-500/40 text-teal-300 text-sm tracking-widest uppercase rounded-full hover:bg-teal-500/10 hover:border-teal-400 transition-all duration-300 hover:shadow-[0_0_20px_rgba(20,184,166,0.2)]">
            Ver Catálogo Completo
          </button>
        </div>
      </div>
    </section>
  );
}
