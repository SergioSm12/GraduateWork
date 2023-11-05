import { createSlice } from "@reduxjs/toolkit";
import { initialRateForm } from "../rate/rateSlice";

export const vehicleTypeForm = {
  id: 0,
  name: "",
};

const initialErrorsVehicleType = {
  name: "",
};

export const vehicleTypeSlice = createSlice({
  name: "vehicleType",
  initialState: {
    vehicleTypes: [],
    vehicleTypeSelected: vehicleTypeForm,
    errorsVehicleType: initialErrorsVehicleType,
  },
  reducers: {
    addVehicleTypeSlice: (state, action) => {
      state.vehicleTypes = [
        ...state.vehicleTypes,
        {
          ...action.payload,
        },
      ];
      state.vehicleTypeSelected = vehicleTypeForm;
    },

    updateVehicleTypeSlice: (state, action) => {
      state.vehicleTypes = state.vehicleTypes.map((vt) => {
        if (vt.id === action.payload.id) {
          return {
            ...action.payload,
          };
        }

        return vt;
      });

      state.vehicleTypeSelected = initialRateForm;
    },
    removeVehicleTypeSlice: (state, action) => {
      state.vehicleTypes = state.vehicleTypes.filter(
        (vt) => vt.id !== action.payload
      );
    },
    loadingVehicleTypes: (state, action) => {
      state.vehicleTypes = action.payload;
    },
    loadingErrorsVehicleType: (state, action) => {
      state.errorsVehicleType = action.payload;
      state.vehicleTypeSelected = vehicleTypeForm;
    },

    onVehicleTypeSelected: (state, action) => {
      state.vehicleTypeSelected = action.payload;
    },
  },
});

export const {
  loadingVehicleTypes,
  addVehicleTypeSlice,
  loadingErrorsVehicleType,
  updateVehicleTypeSlice,
  removeRateSlice,
  onVehicleTypeSelected,
  removeVehicleTypeSlice
} = vehicleTypeSlice.actions;
