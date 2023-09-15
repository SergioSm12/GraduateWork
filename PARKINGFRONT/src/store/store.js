import { configureStore } from "@reduxjs/toolkit";
import { authSlice } from "./slices/auth/authSlice";
import { usersSlice } from "./slices/user/usersSlice";
import { vehicleSlice } from "./slices/vehicle/vehicleSlice";

export const store = configureStore({
  reducer: {
    users:usersSlice.reducer,
    vehicles:vehicleSlice.reducer,
    auth: authSlice.reducer,
  },
});
