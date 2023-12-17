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
    visitorReceiptSelected: initialVisitorReceipt,

    visibleShowReceiptVisitorModal: false,
    visibleFormReceiptVisitorModal: false,
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
      state.errorsVisitorReceipt = initialErrorsVisitorReceipt;
    },
    updateVisitorReceiptSlice: (state, action) => {
      state.visitorReceipts = state.visitorReceipts.map((vr) => {
        if (vr.id === action.payload.id) {
          return {
            ...action.payload,
          };
        }
        return vr;
      });
      state.visitorReceiptSelected = initialVisitorReceipt;
      state.errorsVisitorReceipt = initialErrorsVisitorReceipt;
    },

    loadingVisitorReceipt: (state, action) => {
      state.visitorReceipts = action.payload;
    },

    //info
    onOpenModalShowReceiptVisitor: (state, action) => {
      state.visibleShowReceiptVisitorModal = true;
      state.visitorReceiptSelected = action.payload;
    },
    onCloseModalShowReceiptVisitor: (state) => {
      state.visibleShowReceiptVisitorModal = false;
      state.visitorReceiptSelected = initialVisitorReceipt;
    },

    //Open Modal
    onOpenModalFormCreateVisitorReceipt: (state) => {
      state.visibleFormReceiptVisitorModal = true;
    },
    //Edit
    onOpenModalFormVisitorReceipt: (state, action) => {
      state.visibleFormReceiptVisitorModal = true;
      state.visitorReceiptSelected = action.payload;
    },
    onCloseModalFormVisitorReceipt: (state) => {
      state.visibleFormReceiptVisitorModal = false;
      state.visitorReceiptSelected = initialVisitorReceipt;
    },

    //eliminar del state
    removeVisitorReceipt: (state, action) => {
      state.visitorReceipts = state.visitorReceipts.filter(
        (vr) => vr.id !== action.payload
      );
    },

    loadingErrorVisitorReceipt: (state, action) => {
      state.errorsVisitorReceipt = action.payload;
    },
  },
});

export const {
  loadingVisitorReceipt,
  addVisitorReceipt,
  updateVisitorReceiptSlice,
  removeVisitorReceipt,
  loadingErrorVisitorReceipt,
  onOpenModalShowReceiptVisitor,
  onCloseModalShowReceiptVisitor,
  onOpenModalFormCreateVisitorReceipt,
  onOpenModalFormVisitorReceipt,
  onCloseModalFormVisitorReceipt,
} = visitorReceiptSlice.actions;
