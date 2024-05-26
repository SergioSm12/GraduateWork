import apiClient from "../auth/middleware/apiClient";

const BASE_URL_RECEIPT = "/receipt";

export const findAllReceipts = async () => {
  try {
    const response = await apiClient.get(BASE_URL_RECEIPT);
    return response;
  } catch (error) {
    throw error;
  }
};

export const findReceiptsByUser = async (id) => {
  try {
    const response = await apiClient.get(`${BASE_URL_RECEIPT}/user/${id}`);
    return response;
  } catch (error) {
    throw error;
  }
};

export const createReceiptByUser = async (userId, { vehicle, rate }) => {
  try {
    return await apiClient.post(`${BASE_URL_RECEIPT}/${userId}/create`, {
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
    return await apiClient.put(`${BASE_URL_RECEIPT}/${receiptId}/update`, {
      issueDate,
      dueDate,
      paymentStatus,
      rate,
    });
  } catch (error) {
    throw error;
  }
};

export const deleteReciptById = async (receiptId) => {
  try {
    await apiClient.delete(`${BASE_URL_RECEIPT}/${receiptId}`);
  } catch (error) {
    throw error;
  }
};

export const changePaymentStatus = async (receiptId) => {
  try {
    await apiClient.put(`${BASE_URL_RECEIPT}/change-payment/${receiptId}`);
  } catch (error) {
    throw error;
  }
};

export const totalUnpaid = async () => {
  try {
    const response = await apiClient.get(`${BASE_URL_RECEIPT}/count-unpaid`);
    return response;
  } catch (error) {
    throw error;
  }
};

export const totalPaid = async () => {
  try {
    const response = await apiClient.get(`${BASE_URL_RECEIPT}/count-paid`);
    return response;
  } catch (error) {
    throw error;
  }
};

export const totalCountReceipts = async () => {
  try {
    const response = await apiClient.get(`${BASE_URL_RECEIPT}/count-total`);
    return response;
  } catch (error) {
    throw error;
  }
};
