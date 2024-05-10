import { createSlice } from "@reduxjs/toolkit";

export const reportSlice = createSlice({
  name: "reports",
  initialState: {
    currentMonthlyReceiptReport: null,
    currentWeeklyReceiptReport: null,
    currentBiweeklyReceiptReport: null,
  },

  reducers: {
    loadingCurrentMonthlyReportReceipt: (state, action) => {
      state.currentMonthlyReceiptReport = action.payload;
    },
    loadingCurrentWeeklyReportReceipt: (state, action) => {
      state.currentWeeklyReceiptReport = action.payload;
    },
    loadingCurrentbiweeklyReportReceipt: (state, action) => {
      state.currentBiweeklyReceiptReport = action.payload;
    },
  },
});

export const {
  loadingCurrentMonthlyReportReceipt,
  loadingCurrentWeeklyReportReceipt,
  loadingCurrentbiweeklyReportReceipt,
} = reportSlice.actions;
