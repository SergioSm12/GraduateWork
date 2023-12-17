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
    return await parkingApi.post(BASE_URL_VISITOR_RECEIPT, {
      plate,
      rate,
    });
  } catch (error) {
    throw error;
  }
};

export const updateVisitorReceipt = async ({
  id,
  plate,
  issueDate,
  dueDate,
  paymentStatus,
  rate,
}) => {
  try {
    return await parkingApi.put(`${BASE_URL_VISITOR_RECEIPT}/${id}`, {
      plate,
      issueDate,
      dueDate,
      paymentStatus,
      rate,
    });
  } catch (error) {
    throw error;
  }
};

export const changePaymentStatusVisitor = async (receiptId) => {
  try {
    await parkingApi.put(
      `${BASE_URL_VISITOR_RECEIPT}/change-payment/${receiptId}`
    );
  } catch (error) {
    throw error;
  }
};

export const deleteVisitorReceipt = async (visitorReceiptId)=>{
  try {
    await parkingApi.delete(`${BASE_URL_VISITOR_RECEIPT}/${visitorReceiptId}`);
  } catch (error) {
    throw error;
  }
}
