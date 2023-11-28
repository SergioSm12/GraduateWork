import { createSlice } from "@reduxjs/toolkit";

export const vehicleType = {
  id: 0,
  name: "",
};

export const vehicle = {
  id: 0,
  plate: "",
  vehicleType: vehicleType,
};

export const role = {
  id: 0,
  name: "",
};

export const user = {
  id: 0,
  name: "",
  lastName: "",
  email: "",
  password: "",
  roles: role,
  phoneNumber: "",
  admin: false,
  guard: false,
  vehicles: [],
};

export const rate = {
  id: 0,
  amount: 0,
  time: "",
  vehicleType: vehicleType,
};

export const initialReceiptForm = {
  id: 0,
  issueDate: new Date().toISOString(),
  dueDate: null,
  paymentStatus: false,
  rate: null,
  user: user,
  vehicle: vehicle,
};

const initialErrosReceipt = {
  vehicle: vehicle,
  rate: rate,
};

export const receiptSlice = createSlice({
  name: "receipts",
  initialState: {
    receipts: [],
    receiptsByUser: [],
    totalUnpaidState: 0,
    totalPaidState: 0,
    totalState: 0,
    receiptSelected: initialReceiptForm,
    vehicleSelected: vehicle,

    visibleShowReceiptModal: false,
    visibleFormReceiptModal: false,
    errorsReceipt: initialErrosReceipt,
  },
  reducers: {
    addReceipt: (state, action) => {
      state.receiptsByUser = [
        ...state.receiptsByUser,
        {
          ...action.payload,
        },
      ];
      state.receiptSelected = initialReceiptForm;
      state.visibleFormReceiptModal = false;
    },

    updateReceiptSlice: (state, action) => {
      state.receiptsByUser = state.receiptsByUser.map((r) => {
        if (r.id === action.payload.id) {
          return {
            ...action.payload,
          };
        }
        return r;
      });
      state.receipts = state.receipts.map((r) => {
        if (r.id === action.payload.id) {
          return {
            ...action.payload,
          };
        }
        return r;
      });
      state.receiptSelected = initialReceiptForm;
      state.visibleFormReceiptModal = false;
    },

    removeReceipt: (state, action) => {
      state.receiptsByUser = state.receiptsByUser.filter(
        (receipt) => receipt.id !== action.payload
      );
    },

    loadingReceipts: (state, action) => {
      state.receipts = action.payload;
    },

    loadingReceiptsByUser: (state, action) => {
      state.receiptsByUser = action.payload;
    },

    loadingUnpaidCount: (state, action) => {
      state.totalUnpaidState = action.payload;
    },
    loadingPaidCount: (state, action) => {
      state.totalPaidState = action.payload;
    },
    loadingTotalCount: (state, action) => {
      state.totalState = action.payload;
    },

    //form
    onReceiptSelectedForm: (state, action) => {
      state.receiptSelected = action.payload;
      state.visibleFormReceiptModal = true;
    },
    onOpenModalFormReceipt: (state, action) => {
      state.vehicleSelected = action.payload;
      state.visibleFormReceiptModal = true;
    },
    onCloseModalFormReceipt: (state) => {
      state.visibleFormReceiptModal = false;
      state.receiptSelected = initialReceiptForm;
      state.vehicleSelected = vehicle;
    },

    //info
    onOpenModalShowReceipt: (state) => {
      state.visibleShowReceiptModal = true;
    },
    onCloseShowModalReceipt: (state) => {
      state.visibleShowReceiptModal = false;
      state.receiptSelected = initialReceiptForm;
    },
    onReceiptShowModalSelected: (state, action) => {
      state.receiptSelected = action.payload;
      state.visibleShowReceiptModal = true;
    },
    loadingErrorReceipt: (state, action) => {
      state.errorsReceipt = action.payload;
    },
  },
});

export const {
  addReceipt,
  updateReceiptSlice,
  loadingReceipts,
  loadingReceiptsByUser,
  loadingUnpaidCount,
  loadingPaidCount,
  loadingTotalCount,
  onReceiptSelectedForm,
  onOpenModalFormReceipt,
  onCloseModalFormReceipt,

  onReceiptShowModalSelected,
  onOpenModalShowReceipt,
  onCloseShowModalReceipt,
  removeReceipt,

  loadingErrorReceipt,
} = receiptSlice.actions;
