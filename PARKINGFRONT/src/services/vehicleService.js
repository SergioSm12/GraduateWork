import apiClient from "../auth/middleware/apiClient";

const BASE_URL_VEHICLES = "/vehicle";

export const totalRegisteredVehiclesCount = async () => {
  try {
    const response = await apiClient.get(`${BASE_URL_VEHICLES}/count-total`);
    return response;
  } catch (error) {
    throw error;
  }
};

export const findAllVehiclesByUser = async (id) => {
  try {
    const response = await apiClient.get(`${BASE_URL_VEHICLES}/${id}/list`);
    return response;
  } catch (error) {
    throw error;
  }
};

export const findAllVehicles = async () => {
  try {
    const response = await apiClient.get(`${BASE_URL_VEHICLES}/list`);
    return response;
  } catch (error) {
    throw error;
  }
};

export const findAllVehiclesActiveByUser = async (id) => {
  try {
    const response = await apiClient.get(
      `${BASE_URL_VEHICLES}/${id}/active-vehicles`
    );
    return response;
  } catch (error) {
    throw error;
  }
};

export const findInactiveVehiclesByUser = async (id) => {
  try {
    const response = await apiClient.get(
      `${BASE_URL_VEHICLES}/${id}/inactive-vehicles`
    );
    return response;
  } catch (error) {
    throw error;
  }
};

export const saveVehicle = async (userId, { plate, vehicleType }) => {
  try {
    return await apiClient.post(`${BASE_URL_VEHICLES}/${userId}/create`, {
      plate,
      vehicleType,
    });
  } catch (error) {
    throw error;
  }
};

export const updateVehicle = async (
  userId,
  { id, plate, vehicleType, active }
) => {
  try {
    return await apiClient.put(`${BASE_URL_VEHICLES}/${userId}/update/${id}`, {
      plate,
      vehicleType,
      active,
    });
  } catch (error) {
    throw error;
  }
};

export const removeVehicle = async (userId, id) => {
  try {
    await apiClient.delete(`${BASE_URL_VEHICLES}/${userId}/delete/${id}`);
  } catch (error) {
    throw error;
  }
};

export const activateVehicle = async (userId, id) => {
  try {
    await apiClient.get(
      `${BASE_URL_VEHICLES}/${userId}/activate-vehicle/${id}`
    );
  } catch (error) {
    throw error;
  }
};
