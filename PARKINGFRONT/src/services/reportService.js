import apiClient from "../auth/middleware/apiClient";

const BASE_URL_REPORTS = "/reports";

export const getCurrentMonthlyReportReceipt = async () => {
  try {
    const response = await apiClient.get(`${BASE_URL_REPORTS}/income/monthly`);
    return response;
  } catch (error) {
    throw error;
  }
};

export const currentMonthlyReceiptReportSpecific = async (year, month) => {
  try {
    const response = await apiClient.post(
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
    const response = await apiClient.get(
      `${BASE_URL_REPORTS}/income/biweekly`
    );
    return response;
  } catch (error) {
    throw error;
  }
};

export const getCurrentWeeklyReportReceipt = async () => {
  try {
    const response = await apiClient.get(`${BASE_URL_REPORTS}/income/weekly`);
    return response;
  } catch (error) {
    throw error;
  }
};

// nightly
export const getCurrentMonthlyReportNightlyReceipt = async () => {
  try {
    const response = await apiClient.get(
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
    const response = await apiClient.post(
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
    const response = await apiClient.get(
      `${BASE_URL_REPORTS}/income/biweekly/nightly`
    );
    return response;
  } catch (error) {
    throw error;
  }
};

export const getCurrentWeeklyReportNightlyReceipt = async () => {
  try {
    const response = await apiClient.get(
      `${BASE_URL_REPORTS}/income/weekly/nightly`
    );
    return response;
  } catch (error) {
    throw error;
  }
};

export const getCurrentMonthlyReportVisitorReceipt = async () => {
  try {
    const response = await apiClient.get(
      `${BASE_URL_REPORTS}/income/monthly/visitor`
    );
    return response;
  } catch (error) {
    throw error;
  }
};

export const currentMonthlyVisitorReceiptReportSpecific = async (
  year,
  month
) => {
  try {
    const response = await apiClient.post(
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
    const response = await apiClient.get(
      `${BASE_URL_REPORTS}/income/biweekly/visitor`
    );
    return response;
  } catch (error) {
    throw error;
  }
};

export const getCurrentWeeklyReportVisitorReceipt = async () => {
  try {
    const response = await apiClient.get(
      `${BASE_URL_REPORTS}/income/weekly/visitor`
    );
    return response;
  } catch (error) {
    throw error;
  }
};

//unified
export const currentMonthlyUnifiedReceiptReportService = async () => {
  try {
    const response = await apiClient.get(
      `${BASE_URL_REPORTS}/income/monthly/unified`
    );
    return response;
  } catch (error) {
    throw error;
  }
};

export const currentMonthlyUnifiedReceiptReportSpecific = async (
  year,
  month
) => {
  try {
    const response = await apiClient.post(
      `${BASE_URL_REPORTS}/income/monthly/unified`,
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

export const getCurrentBiweeklyReportUnifiedReceipt = async () => {
  try {
    const response = await apiClient.get(
      `${BASE_URL_REPORTS}/income/biweekly/unified`
    );
    return response;
  } catch (error) {
    throw error;
  }
};

export const getCurrentWeeklyReportUnifiedReceipt = async () => {
  try {
    const response = await apiClient.get(
      `${BASE_URL_REPORTS}/income/weekly/unified`
    );
    return response;
  } catch (error) {
    throw error;
  }
};
