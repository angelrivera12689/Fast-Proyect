import { API_BASE_URL } from './apiConstants';

// ============================================
// Servicio de Autenticación - ventas-api
// ============================================

// Endpoints de autenticación
const AUTH_ENDPOINTS = {
  LOGIN: `${API_BASE_URL}/api/auth/login`,
  REGISTER: `${API_BASE_URL}/api/auth/register`,
  REGISTER_WITH_COMPANY: `${API_BASE_URL}/api/auth/register-with-company`,
  FORGOT_PASSWORD: `${API_BASE_URL}/api/auth/forgot-password`,
  RESET_PASSWORD: `${API_BASE_URL}/api/auth/reset-password`,
  CHANGE_PASSWORD: `${API_BASE_URL}/api/auth/change-password`,
  REFRESH_TOKEN: `${API_BASE_URL}/api/auth/refresh`,
  // 2FA
  TWO_FACTOR_SETUP: `${API_BASE_URL}/api/auth/2fa/setup`,
  TWO_FACTOR_ENABLE: `${API_BASE_URL}/api/auth/2fa/enable`,
  TWO_FACTOR_DISABLE: `${API_BASE_URL}/api/auth/2fa/disable`,
  TWO_FACTOR_VERIFY: `${API_BASE_URL}/api/auth/2fa/verify`,
  USER_PROFILE: `${API_BASE_URL}/api/auth/me`,
};

// Keys para localStorage
const STORAGE_KEYS = {
  TOKEN: 'fast_auth_token',
  REFRESH_TOKEN: 'fast_refresh_token',
  USER: 'fast_user_data',
  COMPANY: 'fast_company_data',
};

// ============================================
// Funciones de Storage
// ============================================

/**
 * Obtiene el token de autenticación del localStorage
 */
export const getAuthToken = () => localStorage.getItem(STORAGE_KEYS.TOKEN);

/**
 * Obtiene el refresh token del localStorage
 */
export const getRefreshToken = () => localStorage.getItem(STORAGE_KEYS.REFRESH_TOKEN);

/**
 * Guarda los tokens en localStorage
 */
export const setAuthTokens = (token, refreshToken) => {
  localStorage.setItem(STORAGE_KEYS.TOKEN, token);
  localStorage.setItem(STORAGE_KEYS.REFRESH_TOKEN, refreshToken);
};

/**
 * Limpia los tokens (logout)
 */
export const clearAuthTokens = () => {
  localStorage.removeItem(STORAGE_KEYS.TOKEN);
  localStorage.removeItem(STORAGE_KEYS.REFRESH_TOKEN);
  localStorage.removeItem(STORAGE_KEYS.USER);
};

/**
 * Guarda datos del usuario en localStorage
 */
export const setUser = (userData) => {
  localStorage.setItem(STORAGE_KEYS.USER, JSON.stringify(userData));
};

/**
 * Obtiene datos del usuario desde localStorage
 */
export const getUser = () => {
  const userData = localStorage.getItem(STORAGE_KEYS.USER);
  return userData ? JSON.parse(userData) : null;
};

/**
 * Guarda datos de la empresa en localStorage
 */
export const setCompany = (companyData) => {
  localStorage.setItem(STORAGE_KEYS.COMPANY, JSON.stringify(companyData));
};

/**
 * Obtiene datos de la empresa desde localStorage
 */
export const getCompany = () => {
  const companyData = localStorage.getItem(STORAGE_KEYS.COMPANY);
  return companyData ? JSON.parse(companyData) : null;
};

/**
 * Headers por defecto
 */
const getDefaultHeaders = () => ({
  'Content-Type': 'application/json',
});

/**
 * Headers con autenticación
 */
const getAuthHeaders = () => {
  const token = getAuthToken();
  return token ? { Authorization: `Bearer ${token}` } : {};
};

/**
 * Headers completos para peticiones autenticadas
 */
const getAuthenticatedHeaders = () => ({
  ...getDefaultHeaders(),
  ...getAuthHeaders(),
});

// ============================================
// Funciones de Autenticación
// ============================================

/**
 * Inicia sesión de usuario
 * @param {Object} credentials - Datos de login: usernameOrEmail, password, twoFactorCode
 * @returns {Promise<Object>} Datos de respuesta incluyendo tokens y user info
 */
export const login = async (credentials) => {
  try {
    const response = await fetch(AUTH_ENDPOINTS.LOGIN, {
      method: 'POST',
      headers: getDefaultHeaders(),
      body: JSON.stringify(credentials),
    });

    const data = await response.json();

    if (!response.ok) {
      throw new Error(data.error || 'Error al iniciar sesión');
    }

    // Guardar tokens y datos de usuario
    if (data.token && data.refreshToken) {
      setAuthTokens(data.token, data.refreshToken);
      setUser({
        id: data.userId,
        username: data.username,
        email: data.email,
        role: data.role,
        twoFactorEnabled: data.twoFactorRequired === false,
      });
    }

    return data;
  } catch (error) {
    console.error('Error en login:', error);
    throw error;
  }
};

/**
 * Registra un nuevo usuario
 * @param {Object} userData - Datos de registro: username, email, password, phone, companyId
 * @returns {Promise<Object>} Datos de respuesta incluyendo tokens y user info
 */
export const register = async (userData) => {
  try {
    const response = await fetch(AUTH_ENDPOINTS.REGISTER, {
      method: 'POST',
      headers: getDefaultHeaders(),
      body: JSON.stringify(userData),
    });

    const data = await response.json();

    if (!response.ok) {
      throw new Error(data.error || 'Error al registrar usuario');
    }

    // Guardar tokens y datos de usuario si se devuelven
    if (data.token && data.refreshToken) {
      setAuthTokens(data.token, data.refreshToken);
      setUser({
        id: data.userId,
        username: data.username,
        email: data.email,
        role: data.role,
      });
    }

    return data;
  } catch (error) {
    console.error('Error en registro:', error);
    throw error;
  }
};

/**
 * Registra un usuario con empresa (B2B)
 * @param {Object} data - Datos de registro: username, email, password, phone, company (nit, businessName, email, phone, address, logoUrl)
 * @returns {Promise<Object>} Datos de respuesta
 */
export const registerWithCompany = async (data) => {
  try {
    const response = await fetch(AUTH_ENDPOINTS.REGISTER_WITH_COMPANY, {
      method: 'POST',
      headers: getDefaultHeaders(),
      body: JSON.stringify(data),
    });

    const result = await response.json();

    if (!response.ok) {
      throw new Error(result.error || 'Error al registrar usuario con empresa');
    }

    return result;
  } catch (error) {
    console.error('Error en registro con empresa:', error);
    throw error;
  }
};

/**
 * Solicita recuperación de contraseña
 * @param {Object} data - Email del usuario
 * @returns {Promise<string>} Mensaje de confirmación
 */
export const forgotPassword = async (data) => {
  try {
    const response = await fetch(AUTH_ENDPOINTS.FORGOT_PASSWORD, {
      method: 'POST',
      headers: getDefaultHeaders(),
      body: JSON.stringify(data),
    });

    if (!response.ok) {
      const errorData = await response.json();
      throw new Error(errorData.error || 'Error al enviar correo de recuperación');
    }

    return await response.text();
  } catch (error) {
    console.error('Error en forgotPassword:', error);
    throw error;
  }
};

/**
 * Resetea la contraseña
 * @param {Object} data - Token y nueva contraseña
 * @returns {Promise<string>} Mensaje de confirmación
 */
export const resetPassword = async (data) => {
  try {
    const response = await fetch(AUTH_ENDPOINTS.RESET_PASSWORD, {
      method: 'POST',
      headers: getDefaultHeaders(),
      body: JSON.stringify(data),
    });

    if (!response.ok) {
      const errorData = await response.json();
      throw new Error(errorData.error || 'Error al resetear contraseña');
    }

    return await response.text();
  } catch (error) {
    console.error('Error en resetPassword:', error);
    throw error;
  }
};

/**
 * Cambia la contraseña (requiere autenticación)
 * @param {Object} data - Contraseña actual y nueva contraseña
 * @returns {Promise<string>} Mensaje de confirmación
 */
export const changePassword = async (data) => {
  try {
    const response = await fetch(AUTH_ENDPOINTS.CHANGE_PASSWORD, {
      method: 'POST',
      headers: getAuthenticatedHeaders(),
      body: JSON.stringify(data),
    });

    if (!response.ok) {
      const errorData = await response.json();
      throw new Error(errorData.error || 'Error al cambiar contraseña');
    }

    return await response.text();
  } catch (error) {
    console.error('Error en changePassword:', error);
    throw error;
  }
};

/**
 * Refresca el token de acceso
 * @param {string} refreshToken - Token de refresco
 * @returns {Promise<Object>} Nuevo token y refresh token
 */
export const refreshToken = async (refreshToken) => {
  try {
    const response = await fetch(AUTH_ENDPOINTS.REFRESH_TOKEN, {
      method: 'POST',
      headers: getDefaultHeaders(),
      body: JSON.stringify({ refreshToken }),
    });

    const data = await response.json();

    if (!response.ok) {
      throw new Error(data.error || 'Error al refrescar token');
    }

    setAuthTokens(data.token, data.refreshToken || refreshToken);
    return data;
  } catch (error) {
    console.error('Error en refreshToken:', error);
    clearAuthTokens(); // Limpiar datos si hay error
    throw error;
  }
};

// ============================================
// Funciones de 2FA
// ============================================

/**
 * Genera la configuración para 2FA
 * @returns {Promise<Object>} Datos de configuración 2FA
 */
export const setupTwoFactor = async () => {
  try {
    const response = await fetch(AUTH_ENDPOINTS.TWO_FACTOR_SETUP, {
      method: 'GET',
      headers: getAuthenticatedHeaders(),
    });

    const data = await response.json();

    if (!response.ok) {
      throw new Error(data.error || 'Error al configurar 2FA');
    }

    return data;
  } catch (error) {
    console.error('Error en setupTwoFactor:', error);
    throw error;
  }
};

/**
 * Habilita autenticación de dos factores
 * @param {Object} data - Código de verificación
 * @returns {Promise<Object>} Resultado de la operación
 */
export const enableTwoFactor = async (data) => {
  try {
    const response = await fetch(AUTH_ENDPOINTS.TWO_FACTOR_ENABLE, {
      method: 'POST',
      headers: getAuthenticatedHeaders(),
      body: JSON.stringify(data),
    });

    const result = await response.json();

    if (!response.ok) {
      throw new Error(result.error || 'Error al habilitar 2FA');
    }

    // Actualizar datos del usuario
    const user = getUser();
    if (user) {
      setUser({ ...user, twoFactorEnabled: true });
    }

    return result;
  } catch (error) {
    console.error('Error en enableTwoFactor:', error);
    throw error;
  }
};

/**
 * Deshabilita autenticación de dos factores
 * @param {Object} data - Código de verificación
 * @returns {Promise<Object>} Resultado de la operación
 */
export const disableTwoFactor = async (data) => {
  try {
    const response = await fetch(AUTH_ENDPOINTS.TWO_FACTOR_DISABLE, {
      method: 'POST',
      headers: getAuthenticatedHeaders(),
      body: JSON.stringify(data),
    });

    const result = await response.json();

    if (!response.ok) {
      throw new Error(result.error || 'Error al deshabilitar 2FA');
    }

    // Actualizar datos del usuario
    const user = getUser();
    if (user) {
      setUser({ ...user, twoFactorEnabled: false });
    }

    return result;
  } catch (error) {
    console.error('Error en disableTwoFactor:', error);
    throw error;
  }
};

/**
 * Verifica el código de 2FA
 * @param {Object} data - Código de verificación
 * @returns {Promise<Object>} Resultado de la verificación
 */
export const verifyTwoFactor = async (data) => {
  try {
    const response = await fetch(AUTH_ENDPOINTS.TWO_FACTOR_VERIFY, {
      method: 'POST',
      headers: getDefaultHeaders(),
      body: JSON.stringify(data),
    });

    const result = await response.json();

    if (!response.ok) {
      throw new Error(result.error || 'Error al verificar código 2FA');
    }

    return result;
  } catch (error) {
    console.error('Error en verifyTwoFactor:', error);
    throw error;
  }
};

/**
 * Obtiene el perfil del usuario actual con datos de la empresa
 * @returns {Promise<Object>} Datos del perfil del usuario
 */
export const getUserProfile = async () => {
  try {
    const response = await fetch(AUTH_ENDPOINTS.USER_PROFILE, {
      method: 'GET',
      headers: getAuthenticatedHeaders(),
    });

    const result = await response.json();

    if (!response.ok) {
      throw new Error(result.error || 'Error al obtener perfil');
    }

    // Guardar datos de empresa en localStorage
    if (result.company) {
      setCompany(result.company);
    }

    return result;
  } catch (error) {
    console.error('Error en getUserProfile:', error);
    throw error;
  }
};

// ============================================
// Función para verificar si el usuario está autenticado
// ============================================
export const isAuthenticated = () => {
  const token = getAuthToken();
  return !!token;
};

export default {
  // Endpoints (para acceder desde otros servicios si es necesario)
  endpoints: AUTH_ENDPOINTS,
  // Funciones de storage
  getAuthToken,
  getRefreshToken,
  setAuthTokens,
  clearAuthTokens,
  setUser,
  getUser,
  // Funciones de autenticación
  login,
  register,
  registerWithCompany,
  forgotPassword,
  resetPassword,
  changePassword,
  refreshToken,
  // Funciones de 2FA
  setupTwoFactor,
  enableTwoFactor,
  disableTwoFactor,
  verifyTwoFactor,
  getUserProfile,
  // Estado de autenticación
  isAuthenticated,
};
