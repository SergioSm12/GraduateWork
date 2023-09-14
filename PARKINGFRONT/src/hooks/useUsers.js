import { useDispatch, useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";
import { findAllUsers, remove, save, update } from "../services/userService";
import {
  addUser,
  loadingError,
  initialUserForm,
  loadingUsers,
  onUserSelectedForm,
  onOpenFormCreate,
  onCloseFormCreate,
  updateUser,
  removeUser,
} from "../store/slices/user/usersSlice";
import Swal from "sweetalert2";
import { useAuth } from "../auth/hooks/useAuth";

export const useUsers = () => {
  const { users, userSelected, errors, isLoadingUsers, visibleFormCreate } =
    useSelector((state) => state.users);

  const { login, handlerLogout } = useAuth();
  const dispatch = useDispatch();
  const navigate = useNavigate();

  //Alertas
  const Toast = Swal.mixin({
    toast: true,
    position: "top",
    showConfirmButton: false,
    timer: 3000,
    timerProgressBar: true,
    didOpen: (toast) => {
      toast.addEventListener("mouseenter", Swal.stopTimer);
      toast.addEventListener("mouseleave", Swal.resumeTimer);
    },
  });

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
  const handlerAddUser = async (user, redirectTo) => {
    let response;
    try {
      if (user.id === 0) {
        response = await save(user);
        dispatch(addUser(response.data));
      } else {
        //actualizar
        response = await update(user);
        dispatch(updateUser(response.data));
      }

      Toast.fire({
        icon: "success",
        title: user.id === 0 ? "Usuario Creado" : "Usuario Actualizado",
      });

      if (redirectTo) {
        handlerCloseFormCreate();
        navigate(redirectTo);
      }
    } catch (error) {
      if (error.response && error.response.status == 400) {
        dispatch(loadingError(error.response.data));
      } else if (error.response?.status == 401) {
        //manejamos el cierre de sesion cuando tengamos autenticacion
        handlerLogout();
      } else {
        throw error;
      }
    }
  };

  const handlerRemoveUser = (id) => {
    //validacion para autorizacion

    Swal.fire({
      title: "Esta seguro que desea eliminar?",
      text: "Cuidado el usuario sera eliminado ",
      icon: "warning",
      showCancelButton: true,
      confirmButtonColor: "#1E293C",
      cancelButtonColor: "#d33",
      confirmButtonText: "Si, eliminar!",
    }).then(async (result) => {
      if (result.isConfirmed) {
        try {
          //Eliminar desde el backend
          await remove(id);

          dispatch(removeUser(id));

          Swal.fire(
            "Usuario Eliminado!",
            "El usuario ha sido eliminado con exito",
            "success"
          );
        } catch (error) {
          if (error.response?.status == 401) {
            handlerLogout();
          }
        }
      }
    });
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
    handlerRemoveUser,
    handlerInitialErrors,
    getUsers,
    handlerUserSelectedForm,
    handlerOpenFormCreate,
    handlerCloseFormCreate,
  };
};
