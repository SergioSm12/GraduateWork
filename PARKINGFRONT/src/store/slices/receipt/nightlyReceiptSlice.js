import { createSlice } from "@reduxjs/toolkit";
import { rate, user, vehicle } from "./receiptSlice";

export const initialNightlyReceipt = {
  id: 0,
  initialTime: null,
  departureTime: null,
  paymentStatus: false,
  rate: null,
  user: user,
  vehicle: vehicle,
};

export const nightlyReceiptSlice = createSlice({
  name: "nightlyReceipts",
  initialState: {
    nightlyReceipts: [],
    nightlyReceiptsByUser: [],
    totalNightUnPaidState: 0,
    totalNightPaidState: 0,
    totalNightState: 0,
    idQRNightlyReceipt: null,
    nightlyReceiptSelected: initialNightlyReceipt,

    visibleShowNightlyReceiptModal: false,
    visibleQRModalNightlyReceipt: false,
  },
  reducers: {
    removeNightlyReceipt: (state, action) => {
      state.nightlyReceipts = state.nightlyReceipts.filter(
        (receipt) => receipt.id !== action.payload
      );
    },

    loadingNightlyReceipts: (state, action) => {
      state.nightlyReceipts = action.payload;
    },

    //Total receipts nightly
    loadingNightUnpaidCount: (state, action) => {
      state.totalNightUnPaidState = action.payload;
    },

    loadingNightPaidCount: (state, action) => {
      state.totalNightPaidState = action.payload;
    },

    loadingNightTotalCount: (state, action) => {
      state.totalNightState = action.payload;
    },

    //info
    onCloseShowModalNightlyReceipt: (state) => {
      state.visibleShowNightlyReceiptModal = false;
      state.nightlyReceiptSelected = initialNightlyReceipt;
    },
    onNightlyReceiptShowModalSelected: (state, action) => {
      state.nightlyReceiptSelected = action.payload;
      state.visibleShowNightlyReceiptModal = true;
    },

    //QRcode
    onOpenQRModalNightlyReceipt: (state, action) => {
      state.visibleQRModalNightlyReceipt = true;
      state.idQRNightlyReceipt = action.payload;
    },

    onCloseQRModalNightlyReceipt: (state) => {
      state.visibleQRModalNightlyReceipt = false;
      state.idQRNightlyReceipt = null;
    },
  },
});

export const {
  removeNightlyReceipt,
  loadingNightlyReceipts,
  loadingNightUnpaidCount,
  loadingNightPaidCount,
  loadingNightTotalCount,

  onOpenQRModalNightlyReceipt,
  onCloseQRModalNightlyReceipt,

  onNightlyReceiptShowModalSelected,
  onCloseShowModalNightlyReceipt,
} = nightlyReceiptSlice.actions;
