import usersApi from "../apis/usersApi";

const BASE_URL_USERS = "/users";
const BASE_URL_FACULTY = "/faculty";
const BASE_URL_RECEIPS = "/receips";

export const findAll = async () => {
  try {
    const response = await usersApi.get(BASE_URL_USERS);
    return response;
  } catch (error) {
    console.error(error);
    throw error;
  }
};

export const findAllFaculties = async () => {
  try {
    const response = await usersApi.get(BASE_URL_FACULTY);
    return response;
  } catch (error) {
    console.error(error);
    throw error;
  }
};

export const findAllPages = async (page = 0) => {
  try {
    const response = await usersApi.get(`${BASE_URL_USERS}/page/${page}`);
    return response;
  } catch (error) {
    console.error(error);
    throw error;
  }
};

export const findById = async (id) => {
  try {
    const response = await usersApi.get(`${BASE_URL_USERS}/${id}`);
    return response;
  } catch (error) {
    console.error(error);
    throw error;
  }
};

export const save = async ({ name, lastName, email, password, faculty, admin, guard }) => {
  try {
    return await usersApi.post(BASE_URL_USERS, {
      name,
      lastName,
      email,
      password,
      faculty,
      admin,
      guard
    });
  } catch (error) {
    throw error;
  }
};

export const update = async ({ id, name, lastName, email, faculty, admin, guard }) => {
  try {
    return await usersApi.put(`${BASE_URL_USERS}/${id}`, {
      name,
      lastName,
      email,
      faculty,
      admin,
      guard
      //password:"nothing"
    });
  } catch (error) {
    throw error;
  }
};

export const remove = async (id) => {
  try {
    await usersApi.delete(`${BASE_URL_USERS}/${id}`);
  } catch (error) {
    throw error;
  }
};
