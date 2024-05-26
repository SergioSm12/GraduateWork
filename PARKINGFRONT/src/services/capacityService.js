import apiClient from "../auth/middleware/apiClient";

const BASE_URL_CAPACITY = "/capacity";

export const findAllCapacity = async () => {
  try {
    const response = await apiClient.get(BASE_URL_CAPACITY);
    return response;
  } catch (error) {
    throw error;
  }
};

export const saveCapacity = async ({
  vehicleType,
  building,
  parkingSpaces,
}) => {
  try {
    return await apiClient.post(BASE_URL_CAPACITY, {
      vehicleType,
      building,
      parkingSpaces,
    });
  } catch (error) {
    throw error;
  }
};

export const updateCapacity = async ({
  id,
  vehicleType,
  building,
  parkingSpaces,
}) => {
  try {
    return await apiClient.put(`${BASE_URL_CAPACITY}/${id}`, {
      vehicleType,
      building,
      parkingSpaces,
    });
  } catch (error) {
    throw error;
  }
};

export const removeCapacity = async (id) => {
  try {
    await apiClient.delete(`${BASE_URL_CAPACITY}/${id}`);
  } catch (error) {
    throw error;
  }
};

export const vehicleEntry = async (id) => {
  try {
    await apiClient.patch(`${BASE_URL_CAPACITY}/vehicle-entry/${id}`);
  } catch (error) {
    throw error;
  }
};

export const vehicleExit = async (id) => {
  try {
    await apiClient.patch(`${BASE_URL_CAPACITY}/vehicle-exit/${id}`);
  } catch (error) {
    throw error;
  }
};
