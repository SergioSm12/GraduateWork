import { createSlice } from "@reduxjs/toolkit";

export const initialVisitorReceipt = {
  id: 0,
  plate: "",
  issueDate: null,
  dueDate: null,
  paymentStatus: false,
  rate: null,
};

export const initialErrorsVisitorReceipt = {
  plate: "",
  rate: null,
};

export const visitorReceiptSlice = createSlice({
  name: "visitorReceipts",
  initialState: {
    visitorReceipts: [],
    totalUnpaidVisitorState: 0,
    totalPaidVIstorState: 0,
    vistorReceiptSelected: initialVisitorReceipt,
    errorsVisitorReceipt: initialErrorsVisitorReceipt,
  },
  reducers: {
    addVisitorReceipt: (state, action) => {
      state.visitorReceipts = [
        ...state.visitorReceipts,
        {
          ...action.payload,
        },
      ];
    },

    loadingVisitorReceipt:(state,action)=>{
      state.visitorReceipts=action.payload;
    },

    loadingErrorVisitorReceipt: (state, action) => {
      state.errorsVisitorReceipt = action.payload;
    },
    
  },
});

export const { loadingVisitorReceipt,addVisitorReceipt, loadingErrorVisitorReceipt } =
  visitorReceiptSlice.actions;
