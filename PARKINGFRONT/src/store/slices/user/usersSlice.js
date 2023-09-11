import { createSlice } from "@reduxjs/toolkit";

export const role = {
  id: 0,
  name: "",
};

export const initialUserForm = {
  id: 0,
  name: "",
  lastName: "",
  email: "",
  password: "",
  roles: role,
  phoneNumber: "",
  admin: false,
  guard: false,
  vehicles: [],
};

const initialErrors = {
  name: "",
  lastName: "",
  email: "",
  password: "",
  phoneNumber: "",
};

export const usersSlice = createSlice({
  name: "users",
  initialState: {
    users: [],
    paginator: {},
    userSelected: initialUserForm,
    visibleForm: false,
    errors: initialErrors,
  },
  reducers: {
    addUser: (state, action) => {
      state.users = [
        ...state.users,
        {
          ...action.payload,
        },
      ];
      state.userSelected = initialUserForm;
    },

    loadingUsers: (state, action) => {
      state.users = action.payload;
    },

    onUserSelectedForm: (state, action) => {
      state.userSelected = action.payload;
    },
    loadingError: (state, action) => {
      state.errors = action.payload;
      state.userSelected = initialUserForm;
    },
  },
});

export const { addUser, loadingUsers, onUserSelectedForm, loadingError } =
  usersSlice.actions;
