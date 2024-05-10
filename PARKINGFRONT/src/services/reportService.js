import parkingApi from "../apis/parkingApi";

const BASE_URL_REPORTS = "/reports";

export const getCurrentMonthlyReportReceipt = async () => {
  try {
    const response = await parkingApi.get(`${BASE_URL_REPORTS}/income/monthly`);
    return response;
  } catch (error) {
    throw error;
  }
};

export const currentMonthlyReceiptReportSpecific = async (year, month) => {
  try {
    const response = await parkingApi.post(
      `${BASE_URL_REPORTS}/income/monthly`,
      {
        year,
        month,
      }
    );
    return response;
  } catch (error) {
    throw error;
  }
};

export const getCurrentBiweeklyReportReceipt = async () => {
  try {
    const response = await parkingApi.get(`${BASE_URL_REPORTS}/income/biweekly`);
    return response;
  } catch (error) {
    throw error;
  }
};


export const getCurrentWeeklyReportReceipt = async () => {
  try {
    const response = await parkingApi.get(`${BASE_URL_REPORTS}/income/weekly`);
    return response;
  } catch (error) {
    throw error;
  }
};
