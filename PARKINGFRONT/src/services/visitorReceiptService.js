import parkingApi from "../apis/parkingApi";
const BASE_URL_VISITOR_RECEIPT = "/visitor-receipt";

export const findAllVisitorReceipts = async () => {
  try {
    const response = await parkingApi.get(BASE_URL_VISITOR_RECEIPT);
    return response;
  } catch (error) {
    throw error;
  }
};

export const createVisitorReceipt = async ({ plate, rate }) => {
  try {
    console.log(BASE_URL_VISITOR_RECEIPT);
    return await parkingApi.post(BASE_URL_VISITOR_RECEIPT, {
      plate,
      rate,
    });
  } catch (error) {
    throw error;
  }
};
