import parkingApi from "../apis/parkingApi";

const BASE_URL_VEHICLES = "/vehicle";

export const findAllVehiclesByUser = async (id = 0) => {
  try {
    const response = await parkingApi.get(`${BASE_URL_VEHICLES}/${id}/list`);
    return response;
  } catch (error) {
    throw error;
  }
};

export const findAllVehicleType = async () => {
  try {
    const response = await parkingApi.get(`${BASE_URL_VEHICLES}/type`);
    return response;
  } catch (error) {
    throw error;
  }
};

export const saveVehicle = async (userId, { plate, vehicleType }) => {
  try {
    return await parkingApi.post(`${BASE_URL_VEHICLES}/${userId}/create`, {
      plate,
      vehicleType,
    });
  } catch (error) {
    throw error;
  }
};

export const updateVehicle = async (userId, { id, plate, vehicleType }) => {
  try {
    return await parkingApi.put(`${BASE_URL_VEHICLES}/${userId}/update/${id}`, {
      plate,
      vehicleType,
    });
  } catch (error) {
    throw error;
  }
};

export const removeVehicle = async (userId, id) => {
  try {
    await parkingApi.delete(`${BASE_URL_VEHICLES}/${userId}/delete/${id}`);
  } catch (error) {
    throw error;
  }
};
