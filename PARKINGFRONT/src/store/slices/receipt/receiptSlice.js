import { createSlice } from "@reduxjs/toolkit";

export const receiptSlice = createSlice({
  name: "receipts",
  initialState: {
    receiptsByUser: [],
  },
  reducers: {
    loadingReceiptsByUser: (state, action) => {
      state.receiptsByUser = action.payload;
    },
  },
});

export const { loadingReceiptsByUser } = receiptSlice.actions;
