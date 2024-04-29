import { createSlice } from "@reduxjs/toolkit";
import { onReceiptSelectedForm, rate, user, vehicle } from "./receiptSlice";

export const initialNightlyReceipt = {
  id: 0,
  initialTime: new Date().toISOString(),
  departureTime: new Date().toISOString(),
  paymentStatus: false,
  rate: null,
  user: user,
  vehicle: vehicle,
};

const initialErrorsNightlyReceipt = {
  vehicle: vehicle,
  rate: rate,
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
    vehicleSelected: vehicle,

    visibleShowNightlyReceiptModal: false,
    visibleQRModalNightlyReceipt: false,
    visibleFormNightlyReceiptModal: false,
    errorsNightlyReceipt: initialErrorsNightlyReceipt,
  },
  reducers: {
    addNightlyReceiptSlice: (state, action) => {
      state.nightlyReceipts = [
        ...state.nightlyReceipts,
        {
          ...action.payload,
        },
      ];

      state.nightlyReceiptsByUser = [
        ...state.nightlyReceiptsByUser,
        {
          ...action.payload,
        },
      ];

      state.nightlyReceiptSelected = initialNightlyReceipt;
      state.visibleFormNightlyReceiptModal = false;
    },

    updateNightlyReceiptSlice: (state, action) => {
      state.nightlyReceipts = state.nightlyReceipts.map((nr) => {
        if (nr.id === action.payload.id) {
          return {
            ...action.payload,
          };
        }
        return nr;
      });

      state.nightlyReceiptsByUser = state.nightlyReceiptsByUser.map((nr) => {
        if (nr.id === action.payload.id) {
          return {
            ...action.payload,
          };
        }
        return nr;
      });

      state.nightlyReceiptSelected = initialNightlyReceipt;
      state.visibleFormNightlyReceiptModal = false;
    },

    removeNightlyReceipt: (state, action) => {
      state.nightlyReceipts = state.nightlyReceipts.filter(
        (receipt) => receipt.id !== action.payload
      );

      state.nightlyReceiptsByUser = state.nightlyReceiptsByUser.filter(
        (receipt) => receipt.id !== action.payload
      );
    },

    loadingNightlyReceipts: (state, action) => {
      state.nightlyReceipts = action.payload;
    },

    loadingNightlyReceiptsByUser: (state, action) => {
      state.nightlyReceiptsByUser = action.payload;
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

    //form

    onNightlyReceiptSelectedForm: (state, action) => {
      state.nightlyReceiptSelected = action.payload;
      state.visibleFormNightlyReceiptModal = true;
    },

    onOpenModalFormNightlyReceipt: (state, action) => {
      state.vehicleSelected = action.payload;
      state.visibleFormNightlyReceiptModal = true;
    },

    onCloseModalFormNightlyReceipt: (state) => {
      state.visibleFormNightlyReceiptModal = false;
      state.nightlyReceiptSelected = initialNightlyReceipt;
      state.vehicleSelected = vehicle;
    },
    loadingErrorsNightlyReceipt: (state, action) => {
      state.errorsNightlyReceipt = action.payload;
    },
  },
});

export const {
  addNightlyReceiptSlice,
  updateNightlyReceiptSlice,
  removeNightlyReceipt,
  loadingNightlyReceipts,
  loadingNightlyReceiptsByUser,
  loadingNightUnpaidCount,
  loadingNightPaidCount,
  loadingNightTotalCount,

  onOpenQRModalNightlyReceipt,
  onCloseQRModalNightlyReceipt,

  onNightlyReceiptShowModalSelected,
  onCloseShowModalNightlyReceipt,
  onNightlyReceiptSelectedForm,
  onOpenModalFormNightlyReceipt,
  onCloseModalFormNightlyReceipt,

  loadingErrorsNightlyReceipt,
} = nightlyReceiptSlice.actions;
