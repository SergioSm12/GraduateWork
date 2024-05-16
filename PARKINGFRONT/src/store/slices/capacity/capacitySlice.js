import { createSlice } from "@reduxjs/toolkit";
import { initialBuildingForm } from "../building/buildingSlice";
import { vehicleType } from "../vehicle/vehicleSlice";

export const initialCapacityForm = {
  id: 0,
  building: null,
  vehicleType: null,
  parkingSpaces: 0,
};

export const initialErrorCapacity = {
  id: 0,
  building: "",
  vehicleType: "",
  parkingSpaces: "",
};

export const capacitySlice = createSlice({
  name: "capacities",
  initialState: {
    capacities: [],
    capacitySelected: initialCapacityForm,
    errorsCapacity: initialErrorCapacity,
  },
  reducers: {
    addCapacity: (state, action) => {
      state.capacities = [
        ...state.capacities,
        {
          ...action.payload,
        },
      ];
      state.capacitySelected = initialBuildingForm;
    },
    updateCapacitySlice: (state, action) => {
      state.capacities = state.capacities.map((c) => {
        if (c.id === action.payload.id) {
          return {
            ...action.payload,
          };
        }
        return c;
      });
      state.capacitySelected = initialCapacityForm;
    },
    onCapacitySelected: (state, action) => {
      state.capacitySelected = action.payload;
    },
    loadingCapacities: (state, action) => {
      state.capacities = action.payload;
    },

    removeCapacitiesSlice: (state, action) => {
      state.capacities = state.capacities.filter(
        (capacity) => capacity.id !== action.payload
      );
    },
    loadingErrorCapacity: (state, action) => {
      state.errorsCapacity = action.payload;
      state.capacitySelected = initialCapacityForm;
    },
  },
});

export const {
  addCapacity,
  updateCapacitySlice,
  loadingCapacities,
  onCapacitySelected,
  loadingErrorCapacity,
  removeCapacitiesSlice
} = capacitySlice.actions;
