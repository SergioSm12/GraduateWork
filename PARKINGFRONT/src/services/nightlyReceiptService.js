import parkingApi from "../apis/parkingApi";

const BASE_URL_RECEIPT = "/nightly-receipt";

export const findAllNightlyReceipts = async () => {
  try {
    const response = await parkingApi.get(BASE_URL_RECEIPT);
    return response;
  } catch (error) {
    throw error;
  }
};

export const changePaymentStatusNight = async (receiptNightlyId) => {
  try {
    await parkingApi.patch(
      `${BASE_URL_RECEIPT}/change-payment/${receiptNightlyId}`
    );
  } catch (error) {
    throw error;
  }
};

export const deleteNightlyReciptById = async (nightlyReceiptId) => {
  try {
    await parkingApi.delete(`${BASE_URL_RECEIPT}/${nightlyReceiptId}`);
  } catch (error) {
    throw error;
  }
};

export const totalUnpaid = async () => {
  try {
    const response = await parkingApi.get(`${BASE_URL_RECEIPT}/count-unpaid`);
    return response;
  } catch (error) {
    throw error;
  }
};

export const totalPaid = async () => {
  try {
    const response = await parkingApi.get(`${BASE_URL_RECEIPT}/count-paid`);
    return response;
  } catch (error) {
    throw error;
  }
};

export const totalCountReceipts = async () => {
  try {
    const response = await parkingApi.get(`${BASE_URL_RECEIPT}/count-total`);
    return response;
  } catch (error) {
    throw error;
  }
};
