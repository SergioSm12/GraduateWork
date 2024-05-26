import apiClient from "../auth/middleware/apiClient";

const BASE_URL_USERS = "/users";

export const save = async ({
  name,
  lastName,
  email,
  password,
  phoneNumber,
  admin,
  guard,
}) => {
  try {
    return await apiClient.post(BASE_URL_USERS, {
      name,
      lastName,
      email,
      password,
      phoneNumber,
      admin,
      guard,
    });
  } catch (error) {
    throw error;
  }
};

export const update = async ({
  id,
  name,
  lastName,
  email,
  phoneNumber,
  admin,
  guard,
}) => {
  try {
    return await apiClient.put(`${BASE_URL_USERS}/${id}`, {
      name,
      lastName,
      email,
      phoneNumber,
      admin,
      guard,
    });
  } catch (error) {
    throw error;
  }
};

export const changePasswordUser = async ({ password, id }) => {
  try {
    return await apiClient.patch(`${BASE_URL_USERS}/changePassword/${id}`, {
      password,
    });
  } catch (error) {
    throw error;
  }
};

export const findAllUsers = async () => {
  try {
    const response = await apiClient.get(BASE_URL_USERS);
    return response;
  } catch (error) {
    console.log(error);
    throw error;
  }
};

export const totalCountUsers = async () => {
  try {
    const response = await apiClient.get(`${BASE_URL_USERS}/count-total`);
    return response;
  } catch (error) {
    throw error;
  }
};

export const findActiveUsers = async () => {
  try {
    const response = await apiClient.get(`${BASE_URL_USERS}/active-users`);
    return response;
  } catch (error) {
    throw error;
  }
};

export const findInactiveUsers = async () => {
  try {
    const response = await apiClient.get(`${BASE_URL_USERS}/inactive-users`);
    return response;
  } catch (error) {
    throw error;
  }
};

export const findUserById = async (id) => {
  try {
    const response = await apiClient.get(`${BASE_URL_USERS}/${id}`);
    return response;
  } catch (error) {
    throw error;
  }
};

export const findUserByEmail = async (email) => {
  try {
    const response = await apiClient.get(`${BASE_URL_USERS}/email`, {
      params: { email },
    });
    return response.data;
  } catch (error) {
    throw error;
  }
};

export const activateUser = async (id) => {
  try {
    await apiClient.put(`${BASE_URL_USERS}/activate/${id}`);
  } catch (error) {
    throw error;
  }
};

export const deactivateUser = async (id) => {
  try {
    await apiClient.put(`${BASE_URL_USERS}/deactivate/${id}`);
  } catch (error) {
    throw error;
  }
};

export const remove = async (id) => {
  try {
    await apiClient.delete(`${BASE_URL_USERS}/${id}`);
  } catch (error) {
    throw error;
  }
};
