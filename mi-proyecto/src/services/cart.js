import { API_BASE_URL } from './apiConstants';
import { getAuthToken } from './auth';

const CART_ENDPOINT = `${API_BASE_URL}/api/cart`;

// Headers con autenticación
const getAuthHeaders = () => {
  const token = getAuthToken();
  return token ? { Authorization: `Bearer ${token}` } : {};
};

/**
 * Obtiene el carrito del usuario
 */
export const getCart = async () => {
  try {
    const response = await fetch(CART_ENDPOINT, {
      headers: {
        'Content-Type': 'application/json',
        ...getAuthHeaders(),
      },
    });

    if (!response.ok) {
      throw new Error('Error al obtener el carrito');
    }

    const cartItems = await response.json();
    return transformCartItems(cartItems);
  } catch (error) {
    console.error('Error fetching cart:', error);
    throw error;
  }
};

/**
 * Agrega un producto al carrito
 */
export const addToCart = async (productId, quantity = 1) => {
  try {
    const response = await fetch(CART_ENDPOINT, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        ...getAuthHeaders(),
      },
      body: JSON.stringify({ productId, quantity }),
    });

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
export const updateCartItem = async (productId, quantity) => {
  try {
    const response = await fetch(`${CART_ENDPOINT}/${productId}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
        ...getAuthHeaders(),
      },
      body: JSON.stringify({ quantity }),
    });

    if (!response.ok) {
      const error = await response.json();
      throw new Error(error.error || 'Error al actualizar el carrito');
    }

    return await response.json();
  } catch (error) {
    console.error('Error updating cart:', error);
    throw error;
  }
};

/**
 * Elimina un producto del carrito
 */
export const removeFromCart = async (productId) => {
  try {
    const response = await fetch(`${CART_ENDPOINT}/${productId}`, {
      method: 'DELETE',
      headers: {
        'Content-Type': 'application/json',
        ...getAuthHeaders(),
      },
    });

    if (!response.ok) {
      const error = await response.json();
      throw new Error(error.error || 'Error al eliminar del carrito');
    }

    return await response.json();
  } catch (error) {
    console.error('Error removing from cart:', error);
    throw error;
  }
};

/**
 * Vacía el carrito
 */
export const clearCart = async () => {
  try {
    const response = await fetch(CART_ENDPOINT, {
      method: 'DELETE',
      headers: {
        'Content-Type': 'application/json',
        ...getAuthHeaders(),
      },
    });

    if (!response.ok) {
      throw new Error('Error al vaciar el carrito');
    }

    return await response.json();
  } catch (error) {
    console.error('Error clearing cart:', error);
    throw error;
  }
};

/**
 * Transforma los datos del backend al formato esperado por el frontend
 */
const transformCartItems = (cartItems) => {
  return cartItems.map(item => ({
    id: item.productId,
    cartId: item.id,
    name: item.productName,
    price: Number(item.price) || 0,
    stock: item.stock,
    qty: item.quantity,
    category: item.productCategory,
    lab: item.productLaboratory || 'General',
    imageUrl: item.imageUrl,
    minOrder: 1,
  }));
};
