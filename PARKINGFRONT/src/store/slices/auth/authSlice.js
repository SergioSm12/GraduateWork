import { createSlice } from "@reduxjs/toolkit";

const initialLogin = JSON.parse(sessionStorage.getItem("login")) || {
  isAuth: false,
  isAdmin: false,
  isGuard: false,
  user: undefined,
  isLoginLoading: false,
};

export const authSlice = createSlice({
  name: "auth",
  initialState: initialLogin,
  reducers: {
    onLogin: (state, action) => {
      state.isAuth = true;
      state.isAdmin = action.payload.isAdmin;
      state.isGuard = action.payload.isGuard;
      state.user = action.payload.user;
      state.isLoginLoading = false;
    },
    onLogout: (state) => {
      state.isAuth = false;
      state.isAdmin = false;
      state.isGuard = false;
      state.user = undefined;
      state.isLoginLoading = false;
    },

    onInitLogin: (state) => {
      state.isLoginLoading = true;
    },
  },
});

export const { onLogin, onLogout, onInitLogin } = authSlice.actions;
