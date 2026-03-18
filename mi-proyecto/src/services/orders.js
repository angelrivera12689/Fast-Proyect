import { API_BASE_URL } from './apiConstants';
import { getAuthToken, getUser } from './auth';

const ORDERS_ENDPOINT = `${API_BASE_URL}/api/orders`;

// Headers con autenticación
const getAuthHeaders = () => {
  const token = getAuthToken();
  return token ? { Authorization: `Bearer ${token}` } : {};
};

/**
 * Obtiene todas las órdenes del usuario actual
 */
export const getOrders = async () => {
  try {
    const response = await fetch(ORDERS_ENDPOINT, {
      headers: {
        'Content-Type': 'application/json',
        ...getAuthHeaders(),
      },
    });

    if (!response.ok) {
      throw new Error('Error al obtener las órdenes');
    }

    return await response.json();
  } catch (error) {
    console.error('Error fetching orders:', error);
    throw error;
  }
};

/**
 * Obtiene una orden por ID
 */
export const getOrderById = async (orderId) => {
  try {
    const response = await fetch(`${ORDERS_ENDPOINT}/${orderId}`, {
      headers: {
        'Content-Type': 'application/json',
        ...getAuthHeaders(),
      },
    });

    if (!response.ok) {
      throw new Error('Orden no encontrada');
    }

    return await response.json();
  } catch (error) {
    console.error('Error fetching order:', error);
    throw error;
  }
};

/**
 * Obtiene el carrito activo del usuario
 */
export const getActiveCart = async () => {
  try {
    const user = getUser();
    if (!user || !user.id) {
      throw new Error('Usuario no autenticado');
    }

    const response = await fetch(`${ORDERS_ENDPOINT}/cart/active/${user.id}`, {
      headers: {
        'Content-Type': 'application/json',
        ...getAuthHeaders(),
      },
    });

    if (!response.ok) {
      if (response.status === 404) {
        return null; // No hay carrito activo
      }
      throw new Error('Error al obtener el carrito');
    }

    return await response.json();
  } catch (error) {
    console.error('Error fetching active cart:', error);
    return null;
  }
};

/**
 * Crea o obtiene un carrito
 */
export const getOrCreateCart = async (companyId) => {
  try {
    const user = getUser();
    if (!user || !user.id) {
      throw new Error('Usuario no autenticado');
    }

    const response = await fetch(`${ORDERS_ENDPOINT}/cart?userId=${user.id}&companyId=${companyId}`, {
      headers: {
        'Content-Type': 'application/json',
        ...getAuthHeaders(),
      },
    });

    if (!response.ok) {
      throw new Error('Error al obtener/crear carrito');
    }

    return await response.json();
  } catch (error) {
    console.error('Error creating cart:', error);
    throw error;
  }
};

/**
 * Agrega un producto al carrito
 */
export const addToOrderCart = async (companyId, productId, quantity = 1) => {
  try {
    const user = getUser();
    if (!user || !user.id) {
      throw new Error('Usuario no autenticado');
    }

    const response = await fetch(
      `${ORDERS_ENDPOINT}/cart/add?userId=${user.id}&companyId=${companyId}&productId=${productId}&quantity=${quantity}`,
      {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          ...getAuthHeaders(),
        },
      }
    );

    if (!response.ok) {
      const error = await response.json();
      throw new Error(error.error || 'Error al agregar al carrito');
    }

    return await response.json();
  } catch (error) {
    console.error('Error adding to cart:', error);
    throw error;
  }
};

/**
 * Actualiza la cantidad de un producto en el carrito
 */
export const updateOrderCartItem = async (cartId, itemId, quantity) => {
  try {
    const response = await fetch(`${ORDERS_ENDPOINT}/cart/${cartId}/item/${itemId}?quantity=${quantity}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
        ...getAuthHeaders(),
      },
    });

    if (!response.ok) {
      throw new Error('Error al actualizar cantidad');
    }

    return await response.json();
  } catch (error) {
    console.error('Error updating cart item:', error);
    throw error;
  }
};

/**
 * Elimina un producto del carrito
 */
export const removeOrderCartItem = async (cartId, itemId) => {
  try {
    const response = await fetch(`${ORDERS_ENDPOINT}/cart/${cartId}/item/${itemId}`, {
      method: 'DELETE',
      headers: {
        'Content-Type': 'application/json',
        ...getAuthHeaders(),
      },
    });

    if (!response.ok) {
      throw new Error('Error al eliminar del carrito');
    }

    return await response.json();
  } catch (error) {
    console.error('Error removing cart item:', error);
    throw error;
  }
};

/**
 * Realiza el checkout - convierte el carrito en orden
 */
export const checkout = async (cartId, shippingAddress, notes) => {
  try {
    const response = await fetch(`${ORDERS_ENDPOINT}/cart/${cartId}/checkout`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        ...getAuthHeaders(),
      },
      body: JSON.stringify({ shippingAddress, notes }),
    });

    if (!response.ok) {
      const error = await response.json();
      throw new Error(error.error || 'Error al realizar checkout');
    }

    return await response.json();
  } catch (error) {
    console.error('Error in checkout:', error);
    throw error;
  }
};

/**
 * Confirma el pago de una orden (usado después del webhook de payment)
 */
export const confirmPayment = async (orderId) => {
  try {
    const response = await fetch(`${ORDERS_ENDPOINT}/${orderId}/confirm-payment`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        ...getAuthHeaders(),
      },
    });

    if (!response.ok) {
      throw new Error('Error al confirmar pago');
    }

    return await response.json();
  } catch (error) {
    console.error('Error confirming payment:', error);
    throw error;
  }
};

/**
 * Cancela una orden
 */
export const cancelOrder = async (orderId) => {
  try {
    const response = await fetch(`${ORDERS_ENDPOINT}/${orderId}/cancel`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        ...getAuthHeaders(),
      },
    });

    if (!response.ok) {
      throw new Error('Error al cancelar orden');
    }

    return await response.json();
  } catch (error) {
    console.error('Error canceling order:', error);
    throw error;
  }
};

export default {
  getOrders,
  getOrderById,
  getActiveCart,
  getOrCreateCart,
  addToOrderCart,
  updateOrderCartItem,
  removeOrderCartItem,
  checkout,
  confirmPayment,
  cancelOrder,
};
