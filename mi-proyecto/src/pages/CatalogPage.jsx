import { useState, useMemo, useEffect } from 'react';
import { getProducts, getActiveCategories } from '../services/products';
import { useCart } from '../context/CartContext';
import Navbar from '../components/Navbar';
import Footer from '../components/Footer';
import Sidebar from '../components/Sidebar';

const fmt = (n) => new Intl.NumberFormat('es-CO', { style: 'currency', currency: 'COP', maximumFractionDigits: 0 }).format(n);

function StockBadge({ stock }) {
  if (stock === 0) return <span className="text-xs px-2 py-0.5 rounded-full bg-red-500/15 text-red-400 border border-red-500/20">Sin stock</span>;
  if (stock < 100) return <span className="text-xs px-2 py-0.5 rounded-full bg-amber-500/15 text-amber-400 border border-amber-500/20">Stock bajo</span>;
  return <span className="text-xs px-2 py-0.5 rounded-full bg-teal-500/15 text-teal-400 border border-teal-500/20">Disponible</span>;
}

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

export default function CatalogPage({ onNavigate }) {
  const { addItem, count } = useCart();
  
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const [selectedCategory, setSelectedCategory] = useState('Todos');
  const [selectedLab, setSelectedLab] = useState('Todos');
  const [priceRange, setPriceRange] = useState([0, 60000]);
  const [stockFilter, setStockFilter] = useState('todos');
  const [search, setSearch] = useState('');
  const [sortBy, setSortBy] = useState('nombre');
  const [addedId, setAddedId] = useState(null);
  const [sidebarOpen, setSidebarOpen] = useState(false);
  
  // Estado para filtro de fecha de vencimiento
  const [expirationFilter, setExpirationFilter] = useState('todos');
  const [categoriesFromBackend, setCategoriesFromBackend] = useState([]);

  // Cargar productos y categorías del backend
  useEffect(() => {
    const fetchData = async () => {
      try {
        setLoading(true);
        
        // Cargar productos
        const productsData = await getProducts();
        setProducts(productsData);
        
        // Cargar categorías del backend
        try {
          const categoriesData = await getActiveCategories();
          // Transformar categorías del backend al formato esperado
          const categoryNames = categoriesData.map(cat => cat.name);
          setCategoriesFromBackend(['Todos', ...categoryNames]);
        } catch (catErr) {
          console.warn('No se pudieron cargar categorías del backend:', catErr);
          // Usar categorías de los productos como fallback
        }
        
        setError(null);
      } catch (err) {
        console.error('Error loading products:', err);
        setError('No se pudieron cargar los productos. Intenta más tarde.');
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, []);

  // Obtener labs únicos de los productos
  const labs = useMemo(() => {
    const uniqueLabs = [...new Set(products.map(p => p.lab).filter(Boolean))];
    return ['Todos', ...uniqueLabs];
  }, [products]);

  // Obtener categorías (del backend con fallback a productos)
  const categories = useMemo(() => {
    // Si hay categorías del backend, usarlas
    if (categoriesFromBackend.length > 1) {
      return categoriesFromBackend;
    }
    // Fallback: extraer categorías de los productos
    const uniqueCategories = [...new Set(products.map(p => p.category).filter(Boolean))];
    return ['Todos', ...uniqueCategories];
  }, [products, categoriesFromBackend]);

  const filtered = useMemo(() => {
    let list = [...products];
    
    // Filtro por búsqueda (nombre)
    if (search) list = list.filter(p => p.name.toLowerCase().includes(search.toLowerCase()));
    
    // Filtro por categoría
    if (selectedCategory !== 'Todos') list = list.filter(p => p.category === selectedCategory);
    
    // Filtro por laboratorio
    if (selectedLab !== 'Todos') list = list.filter(p => p.lab === selectedLab);
    
    // Filtro por rango de precio
    list = list.filter(p => p.price >= priceRange[0] && p.price <= priceRange[1]);
    
    // Filtro por stock
    if (stockFilter === 'disponible') list = list.filter(p => p.stock > 0);
    if (stockFilter === 'bajo') list = list.filter(p => p.stock > 0 && p.stock < 100);
    
    // Filtro por fecha de vencimiento
    if (expirationFilter !== 'todos') {
      const today = new Date();
      const threeMonthsFromNow = new Date();
      threeMonthsFromNow.setMonth(today.getMonth() + 3);
      
      if (expirationFilter === 'vencidos') {
        // Productos vencidos
        list = list.filter(p => {
          if (!p.expirationDate) return false;
          const expDate = new Date(p.expirationDate);
          return expDate < today;
        });
      } else if (expirationFilter === 'proximos') {
        // Productos próximos a vencer (en los próximos 3 meses)
        list = list.filter(p => {
          if (!p.expirationDate) return false;
          const expDate = new Date(p.expirationDate);
          return expDate >= today && expDate <= threeMonthsFromNow;
        });
      } else if (expirationFilter === 'validos') {
        // Productos con más de 3 meses de vigencia
        list = list.filter(p => {
          if (!p.expirationDate) return true;
          const expDate = new Date(p.expirationDate);
          return expDate > threeMonthsFromNow;
        });
      }
    }
    
    // Ordenamiento
    if (sortBy === 'nombre') list = [...list].sort((a, b) => a.name.localeCompare(b.name));
    if (sortBy === 'precio_asc') list = [...list].sort((a, b) => a.price - b.price);
    if (sortBy === 'precio_desc') list = [...list].sort((a, b) => b.price - a.price);
    if (sortBy === 'stock') list = [...list].sort((a, b) => b.stock - a.stock);
    
    return list;
  }, [products, search, selectedCategory, selectedLab, priceRange, stockFilter, sortBy, expirationFilter]);

  const handleAdd = (product) => {
    if (product.stock === 0) return;
    addItem(product, product.minOrder || 1);
    setAddedId(product.id);
    setTimeout(() => setAddedId(null), 1500);
  };

  const resetFilters = () => {
    setSelectedCategory('Todos');
    setSelectedLab('Todos');
    setPriceRange([0, 60000]);
    setStockFilter('todos');
    setSearch('');
    setExpirationFilter('todos');
  };

  const sidebarProps = {
    search, setSearch,
    selectedCategory, setSelectedCategory,
    selectedLab, setSelectedLab,
    priceRange, setPriceRange,
    stockFilter, setStockFilter,
    expirationFilter, setExpirationFilter,
    onReset: resetFilters,
    categories,
    labs,
  };

  return (
    <div className="min-h-screen bg-[#050d1a]">
      <Navbar onNavigate={onNavigate} cartCount={count} />

      {/* Header */}
      <div className="pt-24 pb-10 px-8 bg-gradient-to-b from-[#071525] to-[#050d1a] border-b border-teal-500/10">
        <div className="container mx-auto">
          <p className="text-teal-400/60 tracking-[0.3em] text-xs uppercase mb-2">B2B — Solo clientes registrados</p>
          <h1 className="font-['Cormorant_Garamond',serif] text-4xl md:text-5xl font-bold text-white">
            Catálogo de{' '}
            <span className="text-transparent bg-clip-text bg-gradient-to-r from-teal-300 to-amber-300">Productos</span>
          </h1>
          <p className="text-teal-100/40 text-sm mt-2">{filtered.length} productos encontrados</p>
        </div>
      </div>

      <div className="container mx-auto px-8 py-10 flex gap-8">
        {/* Sidebar desktop */}
        <div className="hidden lg:block w-56 flex-shrink-0">
          <div className="sticky top-28 bg-gradient-to-br from-[#0d2137]/80 to-[#071525]/80 border border-teal-500/15 rounded-2xl p-5 backdrop-blur-sm">
            <Sidebar {...sidebarProps} />
          </div>
        </div>

        {/* Main */}
        <div className="flex-1 min-w-0">
          {/* Top bar */}
          <div className="flex items-center justify-between mb-6 gap-4">
            <button onClick={() => setSidebarOpen(true)}
              className="lg:hidden flex items-center gap-2 border border-teal-500/30 text-teal-300 px-4 py-2 rounded-xl text-sm hover:bg-teal-500/10 transition-all">
              <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M3 4h18M3 10h18M3 16h10" />
              </svg>
              Filtros
            </button>
            <div className="flex items-center gap-2 ml-auto">
              <span className="text-teal-400/50 text-xs hidden sm:block">Ordenar:</span>
              <select value={sortBy} onChange={e => setSortBy(e.target.value)}
                className="bg-[#0d2137] border border-teal-500/20 text-teal-100 text-sm rounded-xl px-3 py-2 outline-none">
                <option value="nombre">Nombre A-Z</option>
                <option value="precio_asc">Menor precio</option>
                <option value="precio_desc">Mayor precio</option>
                <option value="stock">Mayor stock</option>
              </select>
            </div>
            <button onClick={() => onNavigate('cart')}
              className="relative flex items-center gap-2 bg-gradient-to-r from-teal-500 to-cyan-500 text-white px-4 py-2 rounded-xl text-sm font-semibold hover:shadow-[0_0_20px_rgba(20,184,166,0.4)] transition-all">
              <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M3 3h2l.4 2M7 13h10l4-8H5.4M7 13L5.4 5M7 13l-2.293 2.293c-.63.63-.184 1.707.707 1.707H17m0 0a2 2 0 100 4 2 2 0 000-4zm-8 2a2 2 0 11-4 0 2 2 0 014 0z" />
              </svg>
              Carrito
              {count > 0 && (
                <span className="absolute -top-1.5 -right-1.5 w-5 h-5 rounded-full bg-amber-400 text-[#050d1a] text-xs font-bold flex items-center justify-center">
                  {count}
                </span>
              )}
            </button>
          </div>

          {/* Loading / Error / Grid */}
          {loading ? (
            <div className="text-center py-24">
              <div className="inline-block w-8 h-8 border-2 border-teal-500 border-t-transparent rounded-full animate-spin"></div>
              <p className="text-teal-400/40 mt-4">Cargando productos...</p>
            </div>
          ) : error ? (
            <div className="text-center py-24 text-red-400/60">
              <p className="text-4xl mb-3">⚠️</p>
              <p className="text-lg">{error}</p>
              <button onClick={() => window.location.reload()} className="mt-4 text-teal-400 underline text-sm">Reintentar</button>
            </div>
          ) : filtered.length === 0 ? (
            <div className="text-center py-24 text-teal-400/40">
              <p className="text-4xl mb-3">🔍</p>
              <p className="text-lg">No se encontraron productos</p>
              <button onClick={resetFilters} className="mt-4 text-teal-400 underline text-sm">Limpiar filtros</button>
            </div>
          ) : (
            <div className="grid grid-cols-1 sm:grid-cols-2 xl:grid-cols-3 gap-5">
              {filtered.map(product => (
                <div key={product.id}
                  className="group bg-gradient-to-br from-[#0d2137] to-[#071525] border border-teal-500/15 rounded-2xl p-5 flex flex-col hover:border-teal-400/35 hover:shadow-[0_0_25px_rgba(20,184,166,0.12)] transition-all duration-400">

                  {/* Icon */}
                  <div className="flex justify-center mb-4">
                    <div className="relative">
                      <div className="absolute inset-0 bg-teal-400/10 rounded-full blur-xl group-hover:bg-teal-400/20 transition-all duration-500" />
                      <MedIcon />
                    </div>
                  </div>

                  {/* Info */}
                  <div className="flex-1">
                    <div className="flex items-start justify-between gap-2 mb-2">
                      <p className="text-white text-sm font-medium leading-snug">{product.name}</p>
                      <StockBadge stock={product.stock} />
                    </div>
                    <div className="flex items-center gap-2 mb-1">
                      <span className="text-xs text-teal-400/60 bg-teal-500/10 px-2 py-0.5 rounded-full">{product.category}</span>
                      <span className="text-xs text-teal-400/50">{product.lab}</span>
                    </div>
                    <p className="text-xs text-teal-100/35 mt-1">Pedido mínimo: {product.minOrder || 1} unid.</p>
                  </div>

                  <div className="mt-4 flex items-center justify-between">
                    <p className="font-['Cormorant_Garamond',serif] text-xl font-bold text-teal-300">{fmt(product.price)}</p>
                    <button
                      onClick={() => handleAdd(product)}
                      disabled={product.stock === 0}
                      className={`px-4 py-2 rounded-xl text-xs font-semibold tracking-wide transition-all duration-300
                        ${product.stock === 0
                          ? 'bg-teal-500/5 text-teal-500/30 cursor-not-allowed border border-teal-500/10'
                          : addedId === product.id
                            ? 'bg-teal-500 text-white shadow-[0_0_16px_rgba(20,184,166,0.5)]'
                            : 'bg-teal-500/15 text-teal-300 border border-teal-500/30 hover:bg-teal-500/30'}`}>
                      {addedId === product.id ? '✓ Agregado' : product.stock === 0 ? 'Sin stock' : 'Agregar'}
                    </button>
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>
      </div>

      {/* Mobile sidebar overlay */}
      {sidebarOpen && (
        <div className="fixed inset-0 z-50 flex lg:hidden">
          <div className="absolute inset-0 bg-black/60 backdrop-blur-sm" onClick={() => setSidebarOpen(false)} />
          <div className="relative ml-0 w-72 h-full bg-[#0a1628] border-r border-teal-500/15 p-6 overflow-y-auto">
            <button onClick={() => setSidebarOpen(false)}
              className="absolute top-4 right-4 text-teal-400/60 hover:text-teal-300">
              <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M6 18L18 6M6 6l12 12" />
              </svg>
            </button>
            <p className="text-white font-semibold mb-6">Filtros</p>
            <Sidebar {...sidebarProps} />
          </div>
        </div>
      )}

      <Footer onNavigate={onNavigate} />
    </div>
  );
}
