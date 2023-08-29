import { configureStore } from "@reduxjs/toolkit";
import { authSlice } from "./slices/auth/authSlice";
import { usersSlice } from "./slices/user/usersSlice";

export const store = configureStore({
  reducer: {
    users:usersSlice.reducer,
    auth: authSlice.reducer,
  },
});
