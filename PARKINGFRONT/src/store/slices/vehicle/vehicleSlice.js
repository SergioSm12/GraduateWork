import { createSlice } from "@reduxjs/toolkit";
import { initialUserForm } from "../user/usersSlice";

export const vehicleType = {
  id: 0,
  name: "",
};

export const initialVehicleForm = {
  id: 0,
  plate: "",
  vehicleType: vehicleType,
};

const initialErrorsVehicle = {
  plate: "",
  vehicleType: vehicleType,
};

export const vehicleSlice = createSlice({
  name: "vehicles",
  initialState: {
    vehicles: [],
    vehicleTypes: [],
    vehicleSelected: initialVehicleForm,
    visibleFormVehicle: false,
    errorsVehicle: initialErrorsVehicle,
  },
  reducers: {
    addVehicle: (state, action) => {
      state.vehicles = [
        ...state.vehicles,
        {
          ...action.payload,
        },
      ];
      state.vehicleSelected = initialUserForm;
      state.visibleFormVehicle = false;
    },
    loadingVehicleTypes: (state, action) => {
      state.vehicleTypes = action.payload;
    },
    loadingVehicles: (state, action) => {
      state.vehicles = action.payload;
    },
    onVehicleSelectedForm: (state, action) => {
      state.vehicleSelected = action.payload;
      state.visibleFormVehicle = true;
    },
    onOpenFormVehicle: (state) => {
      state.visibleFormVehicle = true;
    },
    onCloseFormVehicle: (state) => {
      state.visibleFormVehicle = false;
      state.vehicleSelected = initialVehicleForm;
    },
    loadingErrorVehicle: (state, action) => {
      state.errorsVehicle = action.payload;
    },
  },
});

export const {
  addVehicle,
  loadingVehicles,
  loadingVehicleTypes,
  onVehicleSelectedForm,
  onOpenFormVehicle,
  onCloseFormVehicle,
  loadingErrorVehicle,
} = vehicleSlice.actions;
