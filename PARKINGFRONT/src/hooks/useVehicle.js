import { useDispatch, useSelector } from "react-redux";
import {
  findAllVehicleType,
  findAllVehiclesByUser,
  removeVehicle,
  saveVehicle,
  updateVehicle,
} from "../services/vehicleService";
import {
  addVehicle,
  loadingErrorVehicle,
  loadingVehicles,
  initialVehicleForm,
  onCloseFormVehicle,
  onOpenFormVehicle,
  onVehicleSelectedForm,
  loadingVehicleTypes,
  updateVehicleSlice,
  removeVehicleSlice,
} from "../store/slices/vehicle/vehicleSlice";
import Swal from "sweetalert2";
import { useNavigate } from "react-router-dom";

export const useVehicle = () => {
  const {
    vehicles,
    vehicleTypes,
    vehicleSelected,
    errorsVehicle,
    visibleFormVehicle,
  } = useSelector((state) => state.vehicles);
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

  const getVehicles = async (id) => {
    try {
      const result = await findAllVehiclesByUser(id);
      dispatch(loadingVehicles(result.data));
    } catch (error) {
      throw error;
    }
  };

  const getVehicleTypes = async () => {
    try {
      const result = await findAllVehicleType();
      dispatch(loadingVehicleTypes(result.data));
    } catch (error) {
      throw error;
    }
  };

  const handlerAddVehicle = async (userId, vehicle) => {
    let response;
    try {
      if (vehicle.id === 0) {
        response = await saveVehicle(userId, vehicle);
        dispatch(addVehicle(response.data));
      } else {
        //console.log(vehicle);
        response = await updateVehicle(userId, vehicle);
        dispatch(updateVehicleSlice(response.data));
      }
      Toast.fire({
        icon: "success",
        title: vehicle.id === 0 ? "Vehiculo creado" : "Vehiculo Actualizado",
      });
      handlerCloseFormVehicle();
      navigate(`/users/show/${userId}`);
    } catch (error) {
      if (error.response && error.response.status == 400) {
        dispatch(loadingErrorVehicle(error.response.data));
      } else if (error.response?.status == 401) {
        //manejamos el cierre de sesion cuando tengamos autenticacion
        // handlerLogout();
      } else {
        throw error;
      }
    }
  };

  const handlerRemoveVehicle = (userId, id) => {
    Swal.fire({
      title: "Esta seguro que desea eliminar?",
      text: "Cuidado el vehiculo sera eliminado ",
      icon: "warning",
      showCancelButton: true,
      confirmButtonColor: "#1E293C",
      cancelButtonColor: "#d33",
      confirmButtonText: "Si, eliminar!",
    }).then(async (result) => {
      if (result.isConfirmed) {
        try {
          await removeVehicle(userId, id);

          dispatch(removeVehicleSlice(id));
          Swal.fire(
            "Vehiculo Eliminado!",
            "El vehiculo ha sido eliminado con exito",
            "success"
          );
        } catch (error) {
          console.log(error);
        }
      }
    });
  };

  const handlerInitialErrorsVehicle = () => {
    dispatch(loadingErrorVehicle({}));
  };

  //selccionar user para update
  const handlerVehicleSelectedForm = (vehicle) => {
    dispatch(onVehicleSelectedForm({ ...vehicle }));
  };

  //mostrar formulario
  const handlerOpenFormVehicle = () => {
    dispatch(onOpenFormVehicle());
  };

  //Ocultar formulario
  const handlerCloseFormVehicle = () => {
    dispatch(onCloseFormVehicle());
    dispatch(loadingErrorVehicle({}));
  };

  return {
    vehicles,
    visibleFormVehicle,
    vehicleSelected,
    errorsVehicle,
    handlerAddVehicle,
    handlerRemoveVehicle,
    handlerInitialErrorsVehicle,
    getVehicles,
    getVehicleTypes,
    handlerVehicleSelectedForm,
    handlerOpenFormVehicle,
    handlerCloseFormVehicle,
    initialVehicleForm,
    vehicleTypes,
  };
};
