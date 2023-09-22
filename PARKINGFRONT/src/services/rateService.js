import parkingApi from "../apis/parkingApi";

const BASE_URL_RATES = "/rate";

export const findAllRate = async () => {
  try {
    const response = await parkingApi.get(BASE_URL_RATES);
    return response;
  } catch (error) {
    throw error;
  }
};

export const saveRate = async ({ amount, time, vehicleType }) => {
  try {
    return await parkingApi.post(BASE_URL_RATES, {
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
    return await parkingApi.put(`${BASE_URL_RATES}/${id}`, {
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
    await parkingApi.delete(`${BASE_URL_RATES}/${id}`);
  } catch (error) {
    throw error;
  }
}
