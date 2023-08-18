import axios from "axios";

export const loginUser = async ({ email, password }) => {
  try {
    return await axios.post(`${import.meta.env.VITE_API_BASE_URL}/login`, {
      email,
      password,
    });
  } catch (error) {
    throw error;
  }
};
