import { useDispatch, useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";
import {
  activateUser,
  deactivateUser,
  findActiveUsers,
  findAllUsers,
  findInactiveUsers,
  findUserById,
  remove,
  save,
  totalCountUsers,
  update,
} from "../services/userService";
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
  loadingUserById,
  loadingActiveUsers,
  loadingInactiveUsers,
  loadingTotalCountUser,
} from "../store/slices/user/usersSlice";
import Swal from "sweetalert2";
import { useAuth } from "../auth/hooks/useAuth";

export const useUsers = () => {
  const {
    users,
    activeUsers,
    inactiveUsers,
    totalCountState,
    userSelected,
    errors,
    isLoadingUsers,
    visibleFormCreate,
    userByid,
  } = useSelector((state) => state.users);

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

  const getTotalCountUsers = async () => {
    try {
      const total = await totalCountUsers();
      dispatch(loadingTotalCountUser(total.data));
    } catch (error) {
      if (error.response?.status == 401) {
        handlerLogout();
      } else {
        throw error;
      }
    }
  };

  const getActiveUsers = async () => {
    try {
      const result = await findActiveUsers();
      dispatch(loadingActiveUsers(result.data));
    } catch (error) {
      if (error.response?.status == 401) {
        handlerLogout();
      }
    }
  };

  const getInactiveUsers = async () => {
    try {
      const result = await findInactiveUsers();
      dispatch(loadingInactiveUsers(result.data));
    } catch (error) {
      if (error.response?.status == 401) {
        handlerLogout();
      }
    }
  };

  const getUserById = async (id) => {
    try {
      const result = await findUserById(id);
      dispatch(loadingUserById(result.data));
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

  const handlerActivateUser = (id) => {
    Swal.fire({
      title: "¿ Desea activar el usuario ?",
      text: "¡ El usuario sera activado !  ",
      icon: "warning",
      showCancelButton: true,
      confirmButtonColor: "#1E293C",
      cancelButtonColor: "#d33",
      confirmButtonText: "Si, activar!",
      cancelButtonText: "Cancelar",
    }).then(async (result) => {
      if (result.isConfirmed) {
        try {
          await activateUser(id);
          getUsers();
          getActiveUsers();
          getInactiveUsers();

          Swal.fire(
            "Usuario activado !",
            "El usuario ha sido activado con exito",
            "success"
          );
        } catch (error) {
          throw error;
        }
      }
    });
  };

  const handlerDeactivateUser = (id) => {
    Swal.fire({
      title: "¿ Esta seguro que desea desactivar ?",
      text: "Cuidado el usuario sera desactivado ",
      icon: "warning",
      showCancelButton: true,
      confirmButtonColor: "#1E293C",
      cancelButtonColor: "#d33",
      confirmButtonText: "Si, desactivar!",
      cancelButtonText: "Cancelar",
    }).then(async (result) => {
      if (result.isConfirmed) {
        try {
          await deactivateUser(id);
          getUsers();
          getActiveUsers();
          getInactiveUsers();

          Swal.fire(
            "Usuario desactivado!",
            "El usuario ha sido desactivado con exito",
            "success"
          );
        } catch (error) {
          throw error;
        }
      }
    });
  };

  const handlerRemoveUser = (id) => {
    //validacion para autorizacion

    Swal.fire({
      title: "Esta seguro que desea eliminar?",
      text: "El usuario sera eliminado junto con los recibos correspondientes a este usuario",
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
    activeUsers,
    inactiveUsers,
    visibleFormCreate,
    userSelected,
    initialUserForm,
    errors,
    isLoadingUsers,
    handlerAddUser,
    handlerActivateUser,
    handlerDeactivateUser,
    handlerRemoveUser,
    handlerInitialErrors,
    getUsers,
    getTotalCountUsers,
    getInactiveUsers,
    getActiveUsers,
    handlerUserSelectedForm,
    handlerOpenFormCreate,
    handlerCloseFormCreate,
    getUserById,
    userByid,
    totalCountState,
  };
};
