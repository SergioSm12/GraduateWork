import apiClient from "../auth/middleware/apiClient";

const BASE_URL_VEHICLETYPES = "/vehicleType";

export const findAllVehicleType = async () => {
  try {
    const response = await apiClient.get(BASE_URL_VEHICLETYPES);
    return response;
  } catch (error) {
    throw error;
  }
};

export const saveVehicleType = async ({ name }) => {
  try {
    return await apiClient.post(BASE_URL_VEHICLETYPES, { name });
  } catch (error) {
    throw error;
  }
};

export const updateVehicleType = async ({ id, name }) => {
  try {
    return await apiClient.put(`${BASE_URL_VEHICLETYPES}/${id}`, { name });
  } catch (error) {
    throw error;
  }
};

export const removeVehicleType = async (id) => {
  try {
    await apiClient.delete(`${BASE_URL_VEHICLETYPES}/${id}`);
  } catch (error) {
    throw error;
  }
};
