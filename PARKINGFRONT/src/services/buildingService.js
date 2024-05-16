import parkingApi from "../apis/parkingApi";

const BASE_URL_BUILDING = "/building";

export const findAllBuilding = async () => {
  try {
    const response = await parkingApi.get(BASE_URL_BUILDING);
    return response;
  } catch (error) {
    throw error;
  }
};

export const saveBuilding = async ({ name }) => {
  try {
    return await parkingApi.post(BASE_URL_BUILDING, {
      name,
    });
  } catch (error) {
    throw error;
  }
};

export const updateBuilding = async ({
  id,
  name,

}) => {
  try {
    return await parkingApi.put(`${BASE_URL_BUILDING}/${id}`, {
      name,
    });
  } catch (error) {
    throw error;
  }
};

export const removeBuilding = async (id) => {
  try {
    await parkingApi.delete(`${BASE_URL_BUILDING}/${id}`);
  } catch (error) {
    throw error;
  }
};
