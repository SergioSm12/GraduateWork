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
    updateVehicleSlice: (state, action) => {
      state.vehicles = state.vehicles.map((v) => {
        if (v.id === action.payload.id) {
          return {
            ...action.payload,
          };
        }
        return v;
      });
      state.vehicleSelected = initialVehicleForm;
      state.visibleFormVehicle = false;
    },
    removeVehicleSlice: (state, action) => {
      state.vehicles = state.vehicles.filter(
        (vehicle) => vehicle.id !== action.payload
      );
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
  updateVehicleSlice,
  removeVehicleSlice,
  loadingVehicles,
  loadingVehicleTypes,
  onVehicleSelectedForm,
  onOpenFormVehicle,
  onCloseFormVehicle,
  loadingErrorVehicle,
} = vehicleSlice.actions;
