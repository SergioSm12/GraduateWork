import { createSlice } from "@reduxjs/toolkit";

export const reportSlice = createSlice({
  name: "reports",
  initialState: {
    currentMonthlyReceiptReport: null,
    currentWeeklyReceiptReport: null,
    currentBiweeklyReceiptReport: null,

    currentMonthlyNightlyReceiptReport: null,
    currentWeeklyNightlyReceiptReport: null,
    currentBiweeklyNightlyReceiptReport: null,

    currentMonthlyVisitorReceiptReport: null,
    currentWeeklyVisitorReceiptReport: null,
    currentBiweeklyVisitorReceiptReport: null,
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

    //nightly
    loadingCurrentMonthlyReportNightlyReceipt: (state, action) => {
      state.currentMonthlyNightlyReceiptReport = action.payload;
    },
    loadingCurrentWeeklyReportNightlyReceipt: (state, action) => {
      state.currentWeeklyNightlyReceiptReport = action.payload;
    },
    loadingCurrentbiweeklyReportNightlyReceipt: (state, action) => {
      state.currentBiweeklyNightlyReceiptReport = action.payload;
    },

    //Visitor
    loadingCurrentMonthlyReportVisitorReceipt: (state, action) => {
      state.currentMonthlyVisitorReceiptReport = action.payload;
    },
    loadingCurrentWeeklyReportVisitorReceipt: (state, action) => {
      state.currentWeeklyVisitorReceiptReport = action.payload;
    },
    loadingCurrentbiweeklyReportVisitorReceipt: (state, action) => {
      state.currentBiweeklyVisitorReceiptReport = action.payload;
    },
  },
});

export const {
  loadingCurrentMonthlyReportReceipt,
  loadingCurrentWeeklyReportReceipt,
  loadingCurrentbiweeklyReportReceipt,

  loadingCurrentMonthlyReportNightlyReceipt,
  loadingCurrentWeeklyReportNightlyReceipt,
  loadingCurrentbiweeklyReportNightlyReceipt,

  loadingCurrentMonthlyReportVisitorReceipt,
  loadingCurrentWeeklyReportVisitorReceipt,
  loadingCurrentbiweeklyReportVisitorReceipt
} = reportSlice.actions;
