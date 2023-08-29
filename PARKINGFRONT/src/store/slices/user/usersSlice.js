import { createSlice } from "@reduxjs/toolkit";

export const role = {
  id: 0,
  name: "",
};

export const faculty = {
  id: 0,
  nameFaculty: "",
};

export const initialUserForm = {
  id: 0,
  name: "",
  lastName: "",
  email: "",
  password: "",
  roles: role,
  faculty: faculty,
  admin: false,
  guard: false,
  vehicles: [],
};

const initialErrors = {
  name: "",
  lastName: "",
  email: "",
  password: "",
  faculty: faculty,
};

export const usersSlice = createSlice({
  name: "users",
  initialState: {
    users: [],
    faculties: [],
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
    loadingFaculties: (state, action) => {
      state.faculties = action.payload;
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

export const { addUser, loadingFaculties, onUserSelectedForm, loadingError } =
  usersSlice.actions;
