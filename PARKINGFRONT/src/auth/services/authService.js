import apiClient from "../middleware/apiClient";

export const loginUser = async ({ email, password }) => {
  try {
    return await apiClient.post("/login", {
      email,
      password,
    });
  } catch (error) {
    throw error;
  }
};
