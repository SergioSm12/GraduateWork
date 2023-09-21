import { createSlice } from "@reduxjs/toolkit";
import { initialUserForm } from "../user/usersSlice";

export const vehicleType = {
  id: 0,
  name: "",
};

export const initialRateForm = {
  id: 0,
  amount: 0,
  time: "",
  vehicleType: vehicleType,
};

const initialErrorsRate = {
  amount: "",
  time: "",
  vehicleType: vehicleType,
};

export const rateSlice = createSlice({
  name: "rates",
  initialState: {
    rates: [],
    rateSelected: initialRateForm,
    errorsRate: initialErrorsRate,
  },
  reducers: {
    addRate: (state, action) => {
      state.rates = [
        ...state.rates,
        {
          ...action.payload,
        },
      ];
      state.rateSelected = initialRateForm;
    },
    updateRateSlice: (state, action) => {
      state.rates = state.rates.map((r) => {
        if (r.id === action.payload.id) {
          return {
            ...action.payload,
          };
        }
        return r;
      });
      state.rateSelected = initialRateForm;
    },
    loadingRates: (state, action) => {
      state.rates = action.payload;
    },
    loadingErrorRates: (state, action) => {
      state.errorsRate = action.payload;
      state.rateSelected = initialUserForm;
    },
    onRateSelectedForm: (state, action) => {
      state.rateSelected = action.payload;
    },
  },
});

export const {
  loadingRates,
  addRate,
  loadingErrorRates,
  updateRateSlice,
  onRateSelectedForm,
} = rateSlice.actions;
