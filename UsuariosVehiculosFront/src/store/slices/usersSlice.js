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
      state.visibleForm = false;
    },

    updateUser: (state, action) => {
      state.users = state.users.map((u) => {
        if (u.id === action.payload.id) {
          return {
            ...u, // Mantener todas las propiedades del usuario intactas
            name: action.payload.name,
            lastName: action.payload.lastName,
            email: action.payload.email,
            faculty: action.payload.faculty,
            admin: action.payload.admin,
            guard: action.payload.guard,
          };
        }
        return u;
      });
      state.userSelected = initialUserForm;
    },

    loadingFaculties: (state, action) => {
      state.faculties = action.payload;
    },

    loadingUsers: (state, { payload }) => {
      state.users = payload.content;
      state.paginator = payload;
    },

    removeUser: (state, action) => {
      state.users = state.users.filter((user) => user.id !== action.payload);
    },

    onUserSelectedForm: (state, action) => {
      state.userSelected = action.payload;
    },
    onOpenModal: (state) => {
      state.visibleForm = true;
    },
    onCloseModal: (state) => {
      state.visibleForm = false;
      state.userSelected = initialUserForm;
    },
    onUserSelectedModal: (state, action) => {
      state.userSelected = action.payload;
      state.visibleForm = true;
    },
    loadingError: (state, action) => {
      state.errors = action.payload;
      state.userSelected = initialUserForm;
    },
  },
});

export const {
  addUser,
  loadingUsers,
  onUserSelectedModal,
  onOpenModal,
  onCloseModal,
  loadingUser,
  loadingError,
  loadingFaculties,
  updateUser,
  removeUser,
} = usersSlice.actions;
