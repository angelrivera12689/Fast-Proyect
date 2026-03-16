const reviews = [
  {
    name: 'Carlos M.',
    role: 'Gerente — Farmacia El Bienestar',
    text: 'FAST ha sido nuestro proveedor por 5 años. La puntualidad en las entregas y la calidad de los productos es impecable. Nunca nos han fallado con la cadena de frío.',
    stars: 5,
  },
  {
    name: 'Dra. Laura V.',
    role: 'Jefa de Compras — Clínica Santa Cruz',
    text: 'Lo que más valoramos es la trazabilidad completa de cada lote y la atención personalizada. Tienen un portafolio muy completo y siempre encuentran solución cuando hay escasez.',
    stars: 5,
  },
  {
    name: 'Andrés R.',
    role: 'Director — Droguería Central',
    text: 'Desde que trabajamos con FAST redujimos nuestros tiempos de reposición a la mitad. Su plataforma de pedidos es clara y el servicio postventa es excelente.',
    stars: 5,
  },
];
 
function Stars({ count }) {
  return (
    <div className="flex gap-1">
      {Array.from({ length: count }).map((_, i) => (
        <svg key={i} className="w-4 h-4 text-amber-400" fill="currentColor" viewBox="0 0 20 20">
          <path d="M9.049 2.927c.3-.921 1.603-.921 1.902 0l1.07 3.292a1 1 0 00.95.69h3.462c.969 0 1.371 1.24.588 1.81l-2.8 2.034a1 1 0 00-.364 1.118l1.07 3.292c.3.921-.755 1.688-1.54 1.118l-2.8-2.034a1 1 0 00-1.175 0l-2.8 2.034c-.784.57-1.838-.197-1.539-1.118l1.07-3.292a1 1 0 00-.364-1.118L2.98 8.72c-.783-.57-.38-1.81.588-1.81h3.461a1 1 0 00.951-.69l1.07-3.292z" />
        </svg>
      ))}
    </div>
  );
}
 
export default function Reviews() {
  return (
    <section className="py-24 bg-gradient-to-b from-[#061525] to-[#050d1a] relative">
      <div className="absolute inset-0 opacity-[0.02]"
        style={{ backgroundImage: 'radial-gradient(circle, #0ff 1px, transparent 1px)', backgroundSize: '40px 40px' }} />
 
      <div className="container mx-auto px-8">
        <div className="text-center mb-14">
          <p className="text-teal-400/60 tracking-[0.3em] text-xs uppercase mb-3">Testimonios</p>
          <h2 className="font-['Cormorant_Garamond',serif] text-4xl md:text-5xl font-bold text-white">
            Lo que dicen nuestros{' '}
            <span className="text-transparent bg-clip-text bg-gradient-to-r from-teal-300 to-amber-300">
              Clientes
            </span>
          </h2>
          <div className="mt-4 flex justify-center">
            <div className="w-16 h-0.5 bg-gradient-to-r from-transparent via-teal-400 to-transparent" />
          </div>
        </div>
 
        <div className="grid md:grid-cols-3 gap-6">
          {reviews.map((r, i) => (
            <div key={i} className="bg-gradient-to-br from-[#0d2137]/80 to-[#071525]/80 border border-teal-500/15 rounded-2xl p-6 hover:border-teal-400/30 hover:shadow-[0_0_25px_rgba(20,184,166,0.1)] transition-all duration-500 backdrop-blur-sm">
              <Stars count={r.stars} />
              <p className="mt-4 text-teal-100/60 text-sm leading-relaxed font-light italic">"{r.text}"</p>
              <div className="mt-5 flex items-center gap-3">
                <div className="w-8 h-8 rounded-full bg-gradient-to-br from-teal-500 to-cyan-600 flex items-center justify-center text-white text-xs font-bold">
                  {r.name[0]}
                </div>
                <div>
                  <p className="text-white text-sm font-medium">{r.name}</p>
                  <p className="text-teal-400/50 text-xs">{r.role}</p>
                </div>
              </div>
            </div>
          ))}
        </div>
      </div>
    </section>
  );
}