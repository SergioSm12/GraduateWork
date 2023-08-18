import { useDispatch, useSelector } from "react-redux";
import {
  findAllFaculties,
  findAllPages,
  save,
  update,
  remove,
} from "../services/userservice";
import {
  loadingUsers,
  onCloseModal,
  onOpenModal,
  onUserSelectedModal,
  initialUserForm,
  addUser,
  loadingError,
  loadingFaculties,
  updateUser,
  removeUser,
} from "../store/slices/usersSlice";
import Swal from "sweetalert2";
import { useNavigate } from "react-router-dom";

export const useUsers = () => {
  const { users, faculties, userSelected, visibleForm, paginator, errors } =
    useSelector((state) => state.users);
  const dispatch = useDispatch();
  const navigate = useNavigate();

  const getUsers = async (page = 0) => {
    try {
      const result = await findAllPages(page);
      //console.log(result.data);
      dispatch(loadingUsers(result.data));
    } catch (error) {
      console.error(error);
    }
  };

  const getFaculties = async () => {
    try {
      const result = await findAllFaculties();
      dispatch(loadingFaculties(result.data));
    } catch (error) {
      console.error(error);
    }
  };

  const handlerAddUser = async (user) => {
    let response;
    try {
      if (user.id === 0) {
        console.log(user);
        response = await save(user);
        dispatch(addUser(response.data));
      } else {
        //actualizar desde el backend

        response = await update(user);

        dispatch(updateUser(response.data));
      }

      Swal.fire(
        user.id === 0 ? "Usuario Creado" : "Usuario Actualizado",
        user.id === 0
          ? "El usuario ha sido creado con exito"
          : "El usuario ha sido actualizado con exito",
        "success"
      );
      handlerInitialErrors();
      navigate("/users");
    } catch (error) {
      if (error.response && error.response.status == 400) {
        dispatch(loadingError(error.response.data));
      }
      //Validacion de los uniques
      else if (
        error.response &&
        error.response.status == 500 &&
        error.response.data?.message?.includes("constraint")
      ) {
        //Esta constante se trae desde los constraints de la base de datos si
        const UK_email = "UK_6dotkott2kjsp8vw4d0m25fb7";
        if (error.response.data?.message?.includes(UK_email)) {
          dispatch(loadingError({ email: "El email ya existe" }));
        }
      } else if (error.response?.status == 401) {
        //manejamos el cierre de sesion cuando tengamos autenticacion
      } else {
        throw error;
      }
    }
  };

  const handlerRemoveUser = (id) => {
    Swal.fire({
      title: "Esta seguro que desea eliminar?",
      text: "Cuidado el usuario sera eliminado ",
      icon: "warning",
      showCancelButton: true,
      confirmButtonColor: "#3085d6",
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
          console.log(error);
        }
      }
    });
  };

  const handlerUserSelectedModal = (user) => {
    dispatch(onUserSelectedModal({ ...user }));
  };

  const handlerOpenModal = () => {
    dispatch(onOpenModal());
  };

  const handlerCloseModal = () => {
    dispatch(onCloseModal());
  };

  const handlerInitialErrors = () => {
    dispatch(loadingError({}));
  };

  return {
    users,
    faculties,
    paginator,
    getUsers,
    getFaculties,
    userSelected,
    initialUserForm,
    visibleForm,
    errors,
    handlerOpenModal,
    handlerCloseModal,
    handlerUserSelectedModal,
    handlerAddUser,
    handlerRemoveUser,
    handlerInitialErrors,
  };
};
