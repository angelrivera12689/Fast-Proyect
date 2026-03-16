# 📦 Sistema de Ventas con Pasarela de Pago
## Backend: Java + Spring Boot | Frontend: React + Vite

---

# 🎯 Objetivo del proyecto

Desarrollar una plataforma web de ventas B2B donde:

- Empresas se registran en el sistema
- Usuarios pertenecen a cada empresa
- Autenticación segura con JWT
- **Autenticación de dos factores (2FA)**
- **Recuperación y cambio de contraseña**
- **Cerrar sesión segura**
- CRUD de información y productos **(todo gestionable desde el admin)**
- Subida de imágenes
- Creación de pedidos (carrito)
- Pago mediante pasarela de pago
- Confirmación automática de compras
- **Gestión de contenido (CMS) desde el panel admin**
- Registro de auditoría y seguridad

⚠️ El sistema NO maneja suscripciones ni cobros mensuales.
Solo compras directas por pedido.

---

# 🧩 Alcance funcional

## 👤 Autenticación y Seguridad
- Registro de usuarios
- Login JWT
- Refresh token (renovación de sesión)
- **Autenticación de dos factores (2FA)** - TOTP con Google Authenticator o similar
- **Recuperación de contraseña** (token por email)
- **Cambio de contraseña** (desde perfil de usuario)
- **Cerrar sesión** (logout con invalidación de tokens)
- Roles y permisos
- Sesiones múltiples (opcional)
- Cierre de sesión desde todos los dispositivos

## 🏢 Empresas
- Registro con NIT y razón social
- Datos de contacto
- Múltiples usuarios por empresa
- **Gestión de empresa desde admin (CRUD)**

## 🛒 Ventas - Productos (Gestión desde Admin)
- **Catálogo de productos (CRUD completo)**
  - Nombre, descripción, precio, stock
  - **Categorías** (CRUD)
  - **Imágenes múltiples** por producto
  - **Atributos variables** (tilla, color, etc.)
- **Carrito / pedido**
  - **Carrito persistente** (se guarda aunque cierre sesión)
  - **Guardar carrito para después**
  - **Lista de deseos** (favoritos)
- **Cotizaciones (B2B)**
  - Solicitar cotización sin comprar inmediatamente
  - Precios especiales por empresa
  - Validez de cotización
- **Pedidos recurrentes** (compras automáticas programadas)
- Detalle de productos
- Pago por pasarela
- Historial de compras

## 💰 Descuentos y Precios Especiales
- **Descuentos por cantidad** (price tiers ya definido)
- **Descuentos por empresa** (precios especiales B2B)
- **Cupones de descuento**
- **Descuentos por método de pago**
- **Límites de crédito** (comprar ahora, pagar después)

## 📝 Gestión de Contenido (CMS) - Desde Admin
- **Página principal (hero, banners, destacados)**
- **Texto de About Us / Nosotros**
- **Información de contacto**
- **Políticas (términos, privacidad)**
- **Footer y encabezados**
- **Banners promocionales**
- **Noticias / Blog**

## 🖼️ Archivos
- Subida de imágenes
- Imágenes para productos
- Avatar de usuario
- Galerías multimedia

## 🔐 Seguridad
- JWT con access token y refresh token
- Hash de contraseñas (bcrypt/argon2)
- **2FA (TOTP)**
- Roles y permisos granulares
- Auditoría de acciones
- Rate limiting
- Protección CSRF
- HTTPS obligatorio

---

# 🏗️ Stack tecnológico

## Backend
- Java 21+
- Spring Boot
- Spring Security
- JWT (jjwt library)
- JPA / Hibernate
- **MySQL/MariaDB** (via XAMPP) o PostgreSQL
- Integración con pasarela de pago (Stripe/MercadoPago)
- Storage S3 o Cloudinary
- Librería TOTP para 2FA (e.g., Apache Commons Auth)
- **Lombok** (reducir boilerplate)
- **MapStruct** (mapping DTOs)
- **Flyway** (migraciones de base de datos)

## Frontend
- React
- Vite
- React Router
- Axios
- Context API o Redux
- Tailwind o Material UI
- react-qr-code (para mostrar QR de 2FA)

## Infraestructura
- Docker
- Nginx
- HTTPS
- Email service (SendGrid, AWS SES, o SMTP)

---

# 📂 Estructura sugerida

## Arquitectura N-Capas por Módulo (igual que Microfarma)

```
ventas-api/src/main/java/com/app/ventas_api/
│
├── organizacion/              # MÓDULO: Empresas
│   ├── domain/               # Entidades, objetos de valor
│   ├── application/          # Casos de uso, servicios
│   ├── infrastructure/       # Repositorios, BD
│   └── presentation/        # Controladores, DTOs
│
├── productos/                 # MÓDULO: Productos
│   ├── domain/
│   ├── application/
│   ├── infrastructure/
│   └── presentation/
│
├── ventas/                   # MÓDULO: Pedidos y Pagos
│   ├── domain/
│   ├── application/
│   ├── infrastructure/
│   └── presentation/
│
├── seguridad/                # MÓDULO: Auth, JWT, Roles
│   ├── domain/              # User, Role, RefreshToken
│   ├── application/
│   ├── infrastructure/
│   └── presentation/
│
└── shared/                   # Común
    ├── kernel/              # Utilidades globales
    └── infrastructure/     # Config global
```

**Total: 4 módulos + shared** - Cada uno con sus 4 capas (domain, application, infrastructure, presentation)

---

# 📊 Modelo Entidad-Relación (MER)

---

## Company
Empresa cliente.

Campos:
- id (PK)
- nit
- business_name
- email
- phone
- address
- logo_url
- active
- created_at
- updated_at

Relaciones:
- 1:N Users
- 1:N Orders

---

## User
Usuarios del sistema.

Campos:
- id (PK)
- company_id (FK)
- username
- email
- password_hash
- phone
- avatar_url
- **two_factor_enabled (boolean)**
- **two_factor_secret (string)** - secreto TOTP
- **password_reset_token (string)**
- **password_reset_expires (datetime)**
- active
- created_at
- updated_at
- last_login_at

Relaciones:
- N:1 Company
- N:M Role
- 1:N Media
- 1:N AuditLog

---

## Role
Tipos de permisos.

Campos:
- id (PK)
- name (ADMIN, COMPANY_ADMIN, BUYER, CONTENT_MANAGER, etc.)
- description

---

## UserRole
Tabla intermedia.

Campos:
- user_id (FK)
- role_id (FK)

---

## Category
Categorías de productos.

Campos:
- id (PK)
- name
- description
- parent_id (FK) - categoría padre (para subcategorías)
- image_url
- active
- created_at

Relaciones:
- 1:N Products

---

## Product
Productos disponibles.

Campos:
- id (PK)
- category_id (FK)
- name
- description
- **base_price**
- **price_tiers** (JSON - precios por cantidad)
- stock
- **sku**
- **weight, dimensions**
- active
- created_at
- updated_at

Relaciones:
- N:1 Category
- 1:N ProductImage
- 1:N OrderItem

---

## ProductImage
Imágenes de productos.

Campos:
- id (PK)
- product_id (FK)
- url
- is_primary
- sort_order
- created_at

---

## Order
Pedido de compra.

Campos:
- id (PK)
- company_id (FK)
- user_id (FK) - quién creó el pedido
- total_amount
- status (CART, PENDING_PAYMENT, PAID, SHIPPED, DELIVERED, CANCELLED)
- shipping_address
- notes
- created_at
- updated_at

Relaciones:
- 1:N OrderItem
- N:1 Company
- N:1 User
- 1:1 Payment

---

## OrderItem
Detalle de productos del pedido.

Campos:
- id (PK)
- order_id (FK)
- product_id (FK)
- product_name (snapshot)
- product_sku (snapshot)
- quantity
- unit_price (snapshot)
- subtotal

---

## Payment
Registro de pago por pasarela.

Campos:
- id (PK)
- order_id (FK)
- amount
- provider (STRIPE, MERCADOPAGO, etc.)
- provider_ref
- payment_method
- status (PENDING, PAID, FAILED, REFUNDED)
- created_at
- paid_at

---

## Media
Imágenes o archivos.

Campos:
- id (PK)
- url
- filename
- type
- size
- user_id (FK)
- created_at

---

## RefreshToken
Sesiones seguras JWT.

Campos:
- id (PK)
- user_id (FK)
- token
- expires_at
- created_at
- revoked

---

## AuditLog
Historial de acciones.

Campos:
- id (PK)
- user_id (FK)
- action
- entity
- entity_id
- old_value (JSON)
- new_value (JSON)
- ip_address
- user_agent
- timestamp

---

## ContentBlock
Bloques de contenido editable (CMS).

Campos:
- id (PK)
- section (homepage, about, contact, footer, etc.)
- key (hero_title, hero_image, about_text, etc.)
- content (texto)
- image_url
- video_url
- sort_order
- active
- created_at
- updated_at

---

## Setting
Configuraciones globales del sistema.

Campos:
- id (PK)
- key (company_name, tax_rate, currency, logo_url, etc.)
- value (texto o JSON)
- type (STRING, NUMBER, BOOLEAN, JSON)
- description
- created_at
- updated_at

---

## NotificationTemplate
Plantillas de notificaciones/email.

Campos:
- id (PK)
- type (ORDER_CONFIRMED, ORDER_SHIPPED, PASSWORD_RESET, WELCOME, etc.)
- subject
- body (con placeholders like {{user_name}}, {{order_id}})
- is_active
- created_at
- updated_at

---

## UserNotification
Notificaciones del usuario.

Campos:
- id (PK)
- user_id (FK)
- title
- message
- type (INFO, SUCCESS, WARNING, ERROR)
- is_read
- link_url
- created_at

---

## CompanyAddress
Múltiples direcciones por empresa.

Campos:
- id (PK)
- company_id (FK)
- label (Principal, Bodega, Oficina, etc.)
- address
- city
- department
- zip_code
- is_default
- active
- created_at

---

## ShippingZone
Zonas de envío disponibles.

Campos:
- id (PK)
- name
- department/region
- delivery_cost
- estimated_days
- active

---

## FavoriteProduct
Productos favoritos de usuarios.

Campos:
- id (PK)
- user_id (FK)
- product_id (FK)
- created_at

---

## Quote
Solicitudes de cotización (B2B).

Campos:
- id (PK)
- company_id (FK)
- user_id (FK)
- status (PENDING, APPROVED, REJECTED, EXPIRED)
- notes
- valid_until
- created_at
- updated_at

Relaciones:
- 1:N QuoteItem

---

## QuoteItem
Ítems de la cotización.

Campos:
- id (PK)
- quote_id (FK)
- product_id (FK)
- quantity
- unit_price (precio cotizado)
- notes

---

## SalesReport (Entidad para Reportes)
Reporte de ventas generado.

Campos:
- id (PK)
- report_type (DAILY, MONTHLY, YEARLY, CUSTOM)
- company_id (FK) - null para todos
- start_date
- end_date
- total_orders
- total_revenue
- total_products_sold
- average_order_value
- top_products (JSON)
- generated_by (FK)
- created_at

---

# 🧠 Diagrama ER (Actualizado)

```
Company
  ├──< User ───< UserRole >── Role
  │     ├── 2FA fields
  │     └── Password reset fields
  ├──< Category ───< Product ───< ProductImage
  ├──< Order ───< OrderItem >── Product
  │        └── Payment
  └──< AuditLog

ContentBlock (CMS)
  └──< Media
```

---

# 🚀 Flujo principal del sistema

## Flujo de Autenticación
1. Usuario se registra o inicia sesión
2. **(Opcional) 2FA**: Si está habilitado, solicitar código TOTP
3. Generar access token + refresh token
4. Acceso al sistema

## Flujo de Recuperación de Contraseña
1. Usuario solicita "Olvidé mi contraseña"
2. Sistema genera token de recuperación
3. Email con enlace de recuperación
4. Usuario ingresa nueva contraseña
5. Invalidar tokens anteriores

## Flujo de Compra
1. Empresa se registra
2. Usuario inicia sesión con JWT
3. Explora productos (catálogo dinámico desde BD)
4. Agrega productos al carrito
5. Crea pedido
6. Paga por pasarela
7. Pedido confirmado
8. Auditoría registrada

---

# ✅ Tablas necesarias (actualizadas)

- Company
- User (campos 2FA y password reset)
- Role
- UserRole
- Category
- Product
- ProductImage
- Order
- OrderItem
- Payment
- Media
- RefreshToken
- AuditLog
- ContentBlock (CMS)
- Setting (configuraciones globales)
- NotificationTemplate
- UserNotification
- CompanyAddress
- ShippingZone
- FavoriteProduct
- Quote
- QuoteItem
- SalesReport

---

# 🎛️ Panel de Administración (Admin Panel)

## Funcionalidades del Admin

### 1. Gestión de Contenido (CMS)
- Editar texto de página principal
- Gestionar banners e imágenes
- Actualizar About Us, Contacto
- Editar políticas y términos
- Gestionar footer

### 2. Gestión de Productos
- CRUD completo de productos
- CRUD de categorías
- Subida de imágenes múltiples
- Gestionar precio base y precios por volumen
- Control de inventario

### 3. Gestión de Empresas y Usuarios
- Ver empresas registradas
- Activar/desactivar empresas
- Gestionar usuarios por empresa
- Asignar roles

### 4. Gestión de Pedidos
- Ver todos los pedidos
- Cambiar estados
- Procesar devoluciones

### 5. Configuración de Seguridad
- Habilitar/deshabilitar 2FA
- Configurar políticas de contraseña
- Ver logs de auditoría

### 6. Dashboard y Reportes
- **Estadísticas de ventas**
  - Gráfico de ventas diarias/semanales/mensuales
  - Ingresos totales
  - Cantidad de pedidos
  - Valor promedio por pedido
- **Reportes por empresa**
  - Ventas por empresa
  - Empresas más activas
  - Empresas nuevas
- **Reporte de inventario**
  - Productos más vendidos
  - Stock bajo
  - Productos sin movimiento
- **KPI's principales**
  - Ticket promedio
  - Tasa de conversión
  - Usuarios activos
- **Exportar reportes** (Excel, PDF)

### 7. Gestión de Cotizaciones
- Ver solicitudes de cotización
- Aprobar/rechazar cotizaciones
- Definir precios especiales
- Enviar cotizaciones por email

### 8. Notificaciones del Sistema
- Enviar notificaciones a usuarios
- Notificaciones masivas
- Historial de notificaciones

---

# 🔒 Flujo 2FA (Autenticación de Dos Factores)

1. Usuario habilita 2FA desde su perfil
2. Sistema genera secreto TOTP
3. Se muestra QR para Google Authenticator
4. Usuario verifica con código inicial
5. En login, después de contraseña:
   - Si 2FA habilitado → solicitar código
   - Si no → continuar normalmente

---

# 📧 Flujo Recuperación de Contraseña

1. Usuario hace clic en "Olvidé mi contraseña"
2. Ingresa su email
3. Sistema genera token único (expiración 1 hora)
4. Envia email con enlace
5. Usuario hace clic → formulario nueva contraseña
6. Sistema valida token y actualiza contraseña
7. Invalidar todos los tokens de sesión anteriores

---

# 🔄 Flujo Cambio de Contraseña (Usuario Logueado)

1. Usuario va a su perfil
2. Clic en "Cambiar contraseña"
3. Ingresa contraseña actual + nueva contraseña
4. Sistema valida contraseña actual
5. Actualiza hash de contraseña
6. **Invalidar todos los tokens activos** (logout de todos los dispositivos)
7. Enviar email de notificación

---

# 🚪 Flujo Cerrar Sesión (Logout)

1. Usuario hace clic en "Cerrar sesión"
2. Frontend envía solicitud al backend
3. Backend marca refresh token como revoke
4. Frontend elimina access token del storage
5. Redireccionar a login
6. (Opcional) Opción de "Cerrar todas las sesiones"

---

# 🛠️ Notas de Implementación

## Seguridad
- Usar **bcrypt** o **Argon2id** para passwords
- Tokens JWT cortos (15-30 min access, 7 días refresh)
- Store refresh tokens en DB con revoke capability
- Loguear todas las acciones sensibles

## CMS
- ContentBlock usa pares section/key para identificar contenido
- Frontend consume estos valores dinámicamente
- Admin panel proporciona editor WYSIWYG simple

## Productos
- Precio base + precios por cantidad (price_tiers JSON)
- SKU único por producto
- Control de stock transaccional

---

# ⏱️ ALCANCE REALISTA PARA 1 MES (MVP)

⚠️ **1 mes solo = Proyecto reducido obligatorio**

## ✅ Version 1.0 - Solo lo esencial

### Semana 1: Base y Autenticación
- [ ] Estructura del proyecto Spring Boot + React
- [ ] Entities básicas: User, Company, Role
- [ ] Login JWT sin 2FA
- [ ] Registro de empresas
- [ ] Logout básico

### Semana 2: Productos y Carrito
- [ ] CRUD Products + Categories
- [ ] Imágenes (solo 1 por producto)
- [ ] Carrito en memoria (no persistente)
- [ ] Crear Order

### Semana 3: Pagos e Inventario
- [ ] Integrar 1 pasarela (Stripe o MercadoPago)
- [ ] Webhook de confirmación
- [ ] Actualizar stock al comprar
- [ ] Historial de pedidos

### Semana 4: Admin Básico y Pulir
- [ ] Admin: ver pedidos
- [ ] Admin: activar/desactivar productos
- [ ] Login empresa basic
- [ ] Deploy básico

## ❌ NO incluye V1 (versión 2)
- 2FA
- CMS/ContentBlock
- Dashboard de reportes
- Cotizaciones B2B
- Wishlist
- Carrito persistente
- Múltiples imágenes
- Notificaciones
- Configuraciones globales

## 🎯 Prioridades de desarrollo

1. **Día 1-7**: Login + Registro Empresa + Productos
2. **Día 8-14**: Carrito + Order + Payment
3. **Día 15-21**: Admin básico + Inventario
4. **Día 22-30**: Testing + Fixes + Deploy

---

Documento listo para usar como base técnica del proyecto.
