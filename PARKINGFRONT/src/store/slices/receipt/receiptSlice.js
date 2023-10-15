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
  rate: rate,
  user: user,
  vehicle: vehicle,
};

export const receiptSlice = createSlice({
  name: "receipts",
  initialState: {
    receiptsByUser: [],
    receiptSelected: initialReceiptForm,
    visibleShowReceiptModal: false,
  },
  reducers: {
    loadingReceiptsByUser: (state, action) => {
      state.receiptsByUser = action.payload;
    },
    onReceiptSelected: (state, action) => {
      state.receiptSelected = action.payload;
    },
    //info
    onOpenModalShowReceipt: (state) => {
      state.visibleShowReceiptModal = true;
    },
    onCloseShowModalReceipt: (state) => {
      state.visibleShowReceiptModal = false;
      state.receiptSelected = initialReceiptForm;
    },
    onReceiptShowModalSelected : (state, action)=>{
      state.receiptSelected = action.payload;
      state.visibleShowReceiptModal=true;
    }
  },
});

export const {
  loadingReceiptsByUser,
  onReceiptSelected,
  onReceiptShowModalSelected,
  onOpenModalShowReceipt,
  onCloseShowModalReceipt,
 
} = receiptSlice.actions;
