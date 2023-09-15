import { createSlice } from "@reduxjs/toolkit";

export const vehicleSlice = createSlice({
  name: "vehicles",
  initialState: {
    vehicles: [],
  },
  reducers: {
    loadingVehicles: (state, action) => {
      state.vehicles = action.payload;
    },
  },
});

export const { loadingVehicles } = vehicleSlice.actions;
