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
    visibleFormCreate: false,
    errors: initialErrors,
    isLoadingUsers: true,
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
      state.visibleFormCreate = false;
    },

    loadingUsers: (state, action) => {
      state.users = action.payload;
      state.isLoadingUsers = false;
    },

    onUserSelectedForm: (state, action) => {
      state.userSelected = action.payload;
      state.visibleFormCreate = true;
    },
    onOpenFormCreate: (state) => {
      state.visibleFormCreate = true;
    },
    onCloseFormCreate: (state) => {
      state.visibleFormCreate = false;
      state.userSelected = initialUserForm;
    },
    loadingError: (state, action) => {
      state.errors = action.payload;
    },
  },
});

export const {
  addUser,
  loadingUsers,
  onUserSelectedForm,
  onOpenFormCreate,
  onCloseFormCreate,
  loadingError,
} = usersSlice.actions;
