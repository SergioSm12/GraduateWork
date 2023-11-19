import parkingApi from "../apis/parkingApi";

const BASE_URL_RECEIPT = "/receipt";

export const findReceiptsByUser = async (id) => {
  try {
    const response = await parkingApi.get(`${BASE_URL_RECEIPT}/user/${id}`);
    return response;
  } catch (error) {
    throw error;
  }
};

export const createReceiptByUser = async (userId, { vehicle, rate }) => {
  try {
    return await parkingApi.post(`${BASE_URL_RECEIPT}/${userId}/create`, {
      vehicle,
      rate,
    });
  } catch (error) {
    throw error;
  }
};

export const updateReceipt = async (
  receiptId,
  { issueDate, dueDate, paymentStatus, rate }
) => {
  try {
    return await parkingApi.put(`${BASE_URL_RECEIPT}/${receiptId}/update`, {
      issueDate,
      dueDate,
      paymentStatus,
      rate,
    });
  } catch (error) {
    throw error;
  }
};
