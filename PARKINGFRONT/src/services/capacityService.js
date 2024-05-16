import parkingApi from "../apis/parkingApi";

const BASE_URL_CAPACITY = "/capacity";

export const findAllCapacity = async () => {
  try {
    const response = await parkingApi.get(BASE_URL_CAPACITY);
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
    return await parkingApi.post(BASE_URL_CAPACITY, {
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
    return await parkingApi.put(`${BASE_URL_CAPACITY}/${id}`, {
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
    await parkingApi.delete(`${BASE_URL_CAPACITY}/${id}`);
  } catch (error) {
    throw error;
  }
};
