import { useDispatch, useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";
import { findAllFaculties, save } from "../services/userService";
import {
  addUser,
  loadingError,
  loadingFaculties,
  initialUserForm,
} from "../store/slices/user/usersSlice";
import Swal from "sweetalert2";
import { useAuth } from "../auth/hooks/useAuth";

export const useUsers = () => {
  const { users, faculties, userSelected, errors } = useSelector(
    (state) => state.users
  );

  const { login, handlerLogout } = useAuth();
  const dispatch = useDispatch();
  const navigate = useNavigate();

  const getFaculties = async () => {
    try {
      const result = await findAllFaculties();
      dispatch(loadingFaculties(result.data));
    } catch (error) {
      if (error.response?.status == 401) {
        //handlerLogout();
      }
    }
  };

  const handlerAddUser = async (user) => {
    let response;
    try {
      response = await save(user);
      dispatch(addUser(response.data));

      Swal.fire({
        position: "top",
        icon: "success",
        title: user.id === 0 ? "Usuario Creado" : "Usuario Actualizado",
        showConfirmButton: false,
        timer: 1500,
      });
      handlerInitialErrors();
      navigate("/auth");
    } catch (error) {
      if (error.response && error.response.status == 400) {
        dispatch(loadingError(error.response.data));
      } else if (error.response?.status == 401) {
        //manejamos el cierre de sesion cuando tengamos autenticacion
        //handlerLogout();
      } else {
        throw error;
      }
    }
  };

  const handlerInitialErrors = () => {
    dispatch(loadingError({}));
  };

  return {
    users,
    faculties,
    getFaculties,
    userSelected,
    initialUserForm,
    errors,
    handlerAddUser,
    handlerInitialErrors,
  };
};
