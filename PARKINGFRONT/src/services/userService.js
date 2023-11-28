import parkingApi from "../apis/parkingApi";

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
    return await parkingApi.post(BASE_URL_USERS, {
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
    return await parkingApi.put(`${BASE_URL_USERS}/${id}`, {
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

export const findAllUsers = async () => {
  try {
    const response = await parkingApi.get(BASE_URL_USERS);
    return response;
  } catch (error) {
    console.log(error);
    throw error;
  }
};

export const totalCountUsers = async () => {
  try {
    const response = await parkingApi.get(`${BASE_URL_USERS}/count-total`);
    return response;
  } catch (error) {
    throw error;
  }
};

export const findActiveUsers = async () => {
  try {
    const response = await parkingApi.get(`${BASE_URL_USERS}/active-users`);
    return response;
  } catch (error) {
    throw error;
  }
};

export const findInactiveUsers = async () => {
  try {
    const response = await parkingApi.get(`${BASE_URL_USERS}/inactive-users`);
    return response;
  } catch (error) {
    throw error;
  }
};

export const findUserById = async (id) => {
  try {
    const response = await parkingApi.get(`${BASE_URL_USERS}/${id}`);
    return response;
  } catch (error) {
    throw error;
  }
};

export const activateUser = async (id) => {
  try {
    await parkingApi.put(`${BASE_URL_USERS}/activate/${id}`);
  } catch (error) {
    throw error;
  }
};

export const deactivateUser = async (id) => {
  try {
    await parkingApi.put(`${BASE_URL_USERS}/deactivate/${id}`);
  } catch (error) {
    throw error;
  }
};

export const remove = async (id) => {
  try {
    await parkingApi.delete(`${BASE_URL_USERS}/${id}`);
  } catch (error) {
    throw error;
  }
};
