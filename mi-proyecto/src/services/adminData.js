export const mockOrders = [
  { id: 'ORD-001', client: 'Farmacia El Bienestar', date: '2026-03-10', total: 340000, status: 'entregado',  items: 4 },
  { id: 'ORD-002', client: 'Clínica Santa Cruz',    date: '2026-03-11', total: 820000, status: 'en_tránsito', items: 7 },
  { id: 'ORD-003', client: 'Droguería Central',     date: '2026-03-12', total: 195000, status: 'pendiente',  items: 3 },
  { id: 'ORD-004', client: 'Farmacia Salud Total',  date: '2026-03-12', total: 560000, status: 'entregado',  items: 6 },
  { id: 'ORD-005', client: 'Hospital Regional',     date: '2026-03-13', total: 1250000,status: 'pendiente',  items: 12 },
  { id: 'ORD-006', client: 'Droguería El Pino',     date: '2026-03-13', total: 430000, status: 'en_tránsito', items: 5 },
];
 
export const mockClients = [
  { id: 1, name: 'Farmacia El Bienestar', nit: '900.123.456-7', city: 'Bogotá',    email: 'compras@bienestar.com', status: 'activo',   orders: 18, joined: '2024-01-15' },
  { id: 2, name: 'Clínica Santa Cruz',    nit: '800.987.321-2', city: 'Medellín',  email: 'compras@santacruz.com', status: 'activo',   orders: 9,  joined: '2024-03-22' },
  { id: 3, name: 'Droguería Central',     nit: '901.456.789-0', city: 'Cali',      email: 'admin@dcentral.com',    status: 'activo',   orders: 25, joined: '2023-11-08' },
  { id: 4, name: 'Farmacia Salud Total',  nit: '700.321.654-1', city: 'Barranquilla', email: 'gerencia@saludtotal.com', status: 'inactivo', orders: 3, joined: '2025-06-01' },
  { id: 5, name: 'Hospital Regional',     nit: '891.234.567-3', city: 'Bucaramanga', email: 'licitaciones@hreg.gov.co', status: 'activo', orders: 41, joined: '2023-07-19' },
  { id: 6, name: 'Droguería El Pino',     nit: '902.111.222-4', city: 'Pereira',   email: 'ventas@elpino.com',     status: 'activo',   orders: 7,  joined: '2025-09-14' },
];
 
export const salesByMonth = [
  { month: 'Oct', value: 4200000 },
  { month: 'Nov', value: 5800000 },
  { month: 'Dic', value: 7100000 },
  { month: 'Ene', value: 3900000 },
  { month: 'Feb', value: 6300000 },
  { month: 'Mar', value: 3595000 },
];
 
export const topProducts = [
  { name: 'Metformina 850mg',    orders: 84, revenue: 1848000 },
  { name: 'Ibuprofeno 400mg',    orders: 76, revenue: 980400  },
  { name: 'Acetaminofén 500mg',  orders: 71, revenue: 695800  },
  { name: 'Omeprazol 20mg',      orders: 58, revenue: 887400  },
  { name: 'Loratadina 10mg',     orders: 53, revenue: 519400  },
];
 
export const heroContent = {
  badge: 'Distribución Nacional',
  title1: 'Tu aliado en',
  title2: 'distribución de',
  highlight1: 'Medicamentos',
  highlight2: 'de Calidad',
  description: 'Conectamos laboratorios y farmacias con la cadena de distribución más confiable y eficiente del país.',
  stat1Value: '500+', stat1Label: 'Productos',
  stat2Value: '200+', stat2Label: 'Clientes',
  stat3Value: '15+',  stat3Label: 'Años',
  cta: 'Ver Catálogo',
};