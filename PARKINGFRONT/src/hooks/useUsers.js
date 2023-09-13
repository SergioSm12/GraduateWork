import { useDispatch, useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";
import { findAllUsers, save } from "../services/userService";
import {
  addUser,
  loadingError,
  initialUserForm,
  loadingUsers,
  onUserSelectedForm,
  onOpenFormCreate,
  onCloseFormCreate,
} from "../store/slices/user/usersSlice";
import Swal from "sweetalert2";
import { useAuth } from "../auth/hooks/useAuth";

export const useUsers = () => {
  const { users, userSelected, errors, isLoadingUsers, visibleFormCreate } =
    useSelector((state) => state.users);

  const { login, handlerLogout } = useAuth();
  const dispatch = useDispatch();
  const navigate = useNavigate();

  const getUsers = async () => {
    try {
      const result = await findAllUsers();
      dispatch(loadingUsers(result.data));
    } catch (error) {
      if (error.response?.status == 401) {
        handlerLogout();
      }
    }
  };

  //Regustrar usuario
  const handlerAddUser = async (user,redirectTo) => {
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
     

      if(redirectTo){
        handlerCloseFormCreate();
        navigate(redirectTo);
      }
      
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

  //selccionar user para update
  const handlerUserSelectedForm = (user) => {
    dispatch(onUserSelectedForm({ ...user }));
  };

  //mostrar formulario
  const handlerOpenFormCreate = () => {
    dispatch(onOpenFormCreate());
  };

  //Ocultar formulario
  const handlerCloseFormCreate = () => {
    dispatch(onCloseFormCreate());
    dispatch(loadingError({}));
  };

  return {
    users,
    visibleFormCreate,
    userSelected,
    initialUserForm,
    errors,
    isLoadingUsers,
    handlerAddUser,
    handlerInitialErrors,
    getUsers,
    handlerUserSelectedForm,
    handlerOpenFormCreate,
    handlerCloseFormCreate,
  };
};
