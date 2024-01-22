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
    totalPaidVistorState: 0,
    totalVisitorReceipt: 0,
    visitorReceiptSelected: initialVisitorReceipt,
    idQRReceiptVisitor: null,

    visibleShowReceiptVisitorModal: false,
    visibleFormReceiptVisitorModal: false,
    visibleQRModalReceiptVisitor: false,
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

    //total receipts visitor
    loadingUnpaidCountVisitor: (state, action) => {
      state.totalUnpaidVisitorState = action.payload;
    },
    loadingPaidCountVisitor: (state, action) => {
      state.totalPaidVistorState = action.payload;
    },
    loadingTotalCountVisitor: (state, action) => {
      state.totalVisitorReceipt = action.payload;
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

    //QR Code
    onOpenQRModalVisitorReceipt: (state, action) => {
      state.visibleQRModalReceiptVisitor = true;
      state.idQRReceiptVisitor = action.payload;
    },

    onCloseQRModalVisitorReceipt: (state) => {
      state.visibleQRModalReceiptVisitor = false;
      state.idQRReceiptVisitor = null;
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

  loadingUnpaidCountVisitor,
  loadingPaidCountVisitor,
  loadingTotalCountVisitor,

  onOpenQRModalVisitorReceipt,
  onCloseQRModalVisitorReceipt,

  loadingErrorVisitorReceipt,
  onOpenModalShowReceiptVisitor,
  onCloseModalShowReceiptVisitor,
  onOpenModalFormCreateVisitorReceipt,
  onOpenModalFormVisitorReceipt,
  onCloseModalFormVisitorReceipt,
} = visitorReceiptSlice.actions;
