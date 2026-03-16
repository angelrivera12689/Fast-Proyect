import ProductCard from './ProductCard';
 
const products = [
  { name: 'Amoxicilina 500mg', price: '18.500' },
  { name: 'Ibuprofeno 400mg', price: '12.900' },
  { name: 'Metformina 850mg', price: '22.000' },
  { name: 'Loratadina 10mg', price: '9.800' },
  { name: 'Omeprazol 20mg', price: '15.300' },
  { name: 'Atorvastatina 40mg', price: '28.700' },
];
 
export default function Products({ onNavigate }) {
  const nav = onNavigate || (() => {});
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
 
        <div className="grid grid-cols-2 md:grid-cols-3 gap-5">
          {products.map((product, i) => (
            <ProductCard key={i} {...product} onNavigate={nav} delay={i * 80} />
          ))}
        </div>
 
        <div className="text-center mt-12">
          <button onClick={() => nav('catalog')} className="px-10 py-3 border border-teal-500/40 text-teal-300 text-sm tracking-widest uppercase rounded-full hover:bg-teal-500/10 hover:border-teal-400 transition-all duration-300 hover:shadow-[0_0_20px_rgba(20,184,166,0.2)]">
            Ver Catálogo Completo
          </button>
        </div>
      </div>
    </section>
  );
}