import parkingApi from "../apis/parkingApi";

const BASE_URL_VEHICLETYPES = "/vehicleType";

export const findAllVehicleType = async () => {
  try {
    const response = await parkingApi.get(BASE_URL_VEHICLETYPES);
    return response;
  } catch (error) {
    throw error;
  }
};

export const saveVehicleType = async ({ name }) => {
  try {
    return await parkingApi.post(BASE_URL_VEHICLETYPES, { name });
  } catch (error) {
    throw error;
  }
};

export const updateVehicleType = async ({ id, name }) => {
  try {
    return await parkingApi.put(`${BASE_URL_VEHICLETYPES}/${id}`, { name });
  } catch (error) {
    throw error;
  }
};

export const removeVehicleType = async (id) => {
  try {
    await parkingApi.delete(`${BASE_URL_VEHICLETYPES}/${id}`);
  } catch (error) {
    throw error;
  }
};
