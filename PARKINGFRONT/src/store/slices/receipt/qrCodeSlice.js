import { createSlice } from "@reduxjs/toolkit";
export const qrCodeSlice = createSlice({
  name: "qrCode",
  initialState: {
    qrCodeImage: null,
    qrCodeImageVisitor: null,
    qrcodeImageNightly: null,
  },
  reducers: {
    setQRCodeImage: (state, action) => {
      state.qrCodeImage = action.payload;
    },
    resetQRCodeImage: (state) => {
      state.qrCodeImage = null;
    },
    setQRCodeImageVisitor: (state, action) => {
      state.qrCodeImageVisitor = action.payload;
    },
    resetQRCodeImageVisitor: (state) => {
      state.qrCodeImageVisitor = null;
    },
    setQRCodeImageNightly: (state, action) => {
      state.qrCodeImageVisitor = action.payload;
    },
    resetQRCodeImageNightly: (state) => {
      state.qrcodeImageNightly = null;
    },
  },
});

export const {
  setQRCodeImage,
  resetQRCodeImage,
  setQRCodeImageVisitor,
  resetQRCodeImageVisitor,
  setQRCodeImageNightly,
  resetQRCodeImageNightly,
} = qrCodeSlice.actions;
