import apiClient from "../auth/middleware/apiClient";

const BASE_URL_RATES = "/rate";

export const findAllRate = async () => {
  try {
    const response = await apiClient.get(BASE_URL_RATES);
    return response;
  } catch (error) {
    throw error;
  }
};

export const saveRate = async ({ amount, time, vehicleType }) => {
  try {
    return await apiClient.post(BASE_URL_RATES, {
      amount,
      time,
      vehicleType,
    });
  } catch (error) {
    throw error;
  }
};

export const updateRate = async ({ id, amount, time, vehicleType }) => {
  try {
    return await apiClient.put(`${BASE_URL_RATES}/${id}`, {
      amount,
      time,
      vehicleType,
    });
  } catch (error) {
    throw error;
  }
};

export const removeRate = async (id)=>{
  try {
    await apiClient.delete(`${BASE_URL_RATES}/${id}`);
  } catch (error) {
    throw error;
  }
}
