import parkingApi from "../apis/parkingApi";

const BASE_URL_USERS = "/users";
const BASE_URL_FACULTY = "/faculty";

export const findAllFaculties = async () => {
  try {
    const response = await parkingApi.get(BASE_URL_FACULTY);
    return response;
  } catch (error) {
    console.error(error);
    throw error;
  }
};

export const save = async ({
  name,
  lastName,
  email,
  password,
  faculty,
  admin,
  guard,
}) => {
  try {
    return await parkingApi.post(BASE_URL_USERS, {
      name,
      lastName,
      email,
      password,
      faculty,
      admin,
      guard,
    });
  } catch (error) {
    throw error;
  }
};
