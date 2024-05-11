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
    const response = await parkingApi.get(
      `${BASE_URL_REPORTS}/income/biweekly`
    );
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

// nightly
export const getCurrentMonthlyReportNightlyReceipt = async () => {
  try {
    const response = await parkingApi.get(
      `${BASE_URL_REPORTS}/income/monthly/nightly`
    );
    return response;
  } catch (error) {
    throw error;
  }
};

export const currentMonthlyNightlyReceiptReportSpecific = async (
  year,
  month
) => {
  try {
    const response = await parkingApi.post(
      `${BASE_URL_REPORTS}/income/monthly/nightly`,
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

export const getCurrentBiweeklyReportNightlyReceipt = async () => {
  try {
    const response = await parkingApi.get(
      `${BASE_URL_REPORTS}/income/biweekly/nightly`
    );
    return response;
  } catch (error) {
    throw error;
  }
};

export const getCurrentWeeklyReportNightlyReceipt = async () => {
  try {
    const response = await parkingApi.get(
      `${BASE_URL_REPORTS}/income/weekly/nightly`
    );
    return response;
  } catch (error) {
    throw error;
  }
};

export const getCurrentMonthlyReportVisitorReceipt = async () => {
  try {
    const response = await parkingApi.get(`${BASE_URL_REPORTS}/income/monthly/visitor`);
    return response;
  } catch (error) {
    throw error;
  }
};

export const currentMonthlyVisitorReceiptReportSpecific = async (year, month) => {
  try {
    const response = await parkingApi.post(
      `${BASE_URL_REPORTS}/income/monthly/visitor`,
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

export const getCurrentBiweeklyReportVisitorReceipt = async () => {
  try {
    const response = await parkingApi.get(
      `${BASE_URL_REPORTS}/income/biweekly/visitor`
    );
    return response;
  } catch (error) {
    throw error;
  }
};

export const getCurrentWeeklyReportVisitorReceipt = async () => {
  try {
    const response = await parkingApi.get(`${BASE_URL_REPORTS}/income/weekly/visitor`);
    return response;
  } catch (error) {
    throw error;
  }
};