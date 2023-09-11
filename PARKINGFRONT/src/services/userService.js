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

export const findAllUsers = async () => {
  try {
    const response = await parkingApi.get(BASE_URL_USERS);
    return response;
  } catch (error) {
    console.log(error);
    throw error;
  }
};
