import { configureStore } from "@reduxjs/toolkit";
import { usersSlice } from "./slices/usersSlice";
import { authSlice } from "./slices/auth/authSlice";

export const store = configureStore({
  reducer: {
    users: usersSlice.reducer,
    auth: authSlice.reducer,
  },
});
