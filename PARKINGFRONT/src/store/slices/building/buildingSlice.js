import { createSlice } from "@reduxjs/toolkit";

export const initialBuildingForm = {
  id: 0,
  name: "",
};

const initialErrorsBuilding = {
  name: "",
};

export const buildingSlice = createSlice({
  name: "buildings",
  initialState: {
    buildings: [],
    buildingSelected: initialBuildingForm,
    errorsBuilding: initialErrorsBuilding,
  },
  reducers: {
    addBuilding: (state, action) => {
      state.buildings = [
        ...state.buildings,
        {
          ...action.payload,
        },
      ];
      state.buildingSelected = initialBuildingForm;
    },

    updateBuildingSlice: (state, action) => {
      state.buildings = state.buildings.map((b) => {
        if (b.id === action.payload.id) {
          return {
            ...action.payload,
          };
        }
        return b;
      });
      state.buildingSelected = initialBuildingForm;
    },

    onBuildingSelected: (state, action) => {
      state.buildingSelected = action.payload;
    },

    loadingBuildings: (state, action) => {
      state.buildings = action.payload;
    },

    removeBuildingsSlice: (state, action) => {
      state.buildings = state.buildings.filter(
        (building) => building.id !== action.payload
      );
    },
    loadingErrorBuilding: (state, action) => {
      state.errorsBuilding = action.payload;
      state.buildingSelected = initialBuildingForm;
    },
  },
});

export const {
  addBuilding,
  updateBuildingSlice,
  loadingErrorBuilding,
  loadingBuildings,
  onBuildingSelected,
  removeBuildingsSlice
} = buildingSlice.actions;
