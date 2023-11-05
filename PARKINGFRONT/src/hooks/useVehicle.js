import { useDispatch, useSelector } from "react-redux";
import {
  activateVehicle,
  findAllVehiclesActiveByUser,
  findAllVehiclesByUser,
  findInactiveVehiclesByUser,
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
  updateVehicleSlice,
  loadingVehiclesActive,
  loadingVehiclesInactive,
} from "../store/slices/vehicle/vehicleSlice";
import Swal from "sweetalert2";
import { useNavigate } from "react-router-dom";
import { findAllVehicleType } from "../services/vehicleTypeService";
import { loadingVehicleTypes } from "../store/slices/vehicle/vehicleTypeSlice";

export const useVehicle = () => {
  const {
    vehicles,
    vehiclesActive,
    vehiclesInactive,
    vehicleSelected,
    errorsVehicle,
    visibleFormVehicle,
  } = useSelector((state) => state.vehicles);
  const { vehicleTypes } = useSelector((state) => state.vehicleType);
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

  const getVehiclesActive = async (id) => {
    try {
      const result = await findAllVehiclesActiveByUser(id);
      dispatch(loadingVehiclesActive(result.data));
    } catch (error) {
      throw error;
    }
  };

  const getVehiclesInactive = async (id) => {
    try {
      const result = await findInactiveVehiclesByUser(id);
      dispatch(loadingVehiclesInactive(result.data));
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
      title: "Esta seguro que desea desactivar ?",
      text: "Cuidado el vehiculo sera desactivado ",
      icon: "warning",
      showCancelButton: true,
      confirmButtonColor: "#1E293C",
      cancelButtonColor: "#d33",
      confirmButtonText: "Si, desactivar!",
      cancelButtonText: "Cancelar",
    }).then(async (result) => {
      if (result.isConfirmed) {
        try {
          await removeVehicle(userId, id);
          getVehicles(userId);
          getVehiclesActive(userId);
          getVehiclesInactive(userId);

          Swal.fire(
            "Vehiculo desactivado!",
            "El vehiculo ha sido desactivado con exito",
            "success"
          );
        } catch (error) {
          throw error;
        }
      }
    });
  };

  const handlerActivateVehicle = (userId, id) => {
    Swal.fire({
      title: "¿ Desea activar el vehiculo ?",
      text: "¡ El vehiculo sera activado !  ",
      icon: "warning",
      showCancelButton: true,
      confirmButtonColor: "#1E293C",
      cancelButtonColor: "#d33",
      confirmButtonText: "Si, activar!",
      cancelButtonText: "Cancelar",
    }).then(async (result) => {
      if (result.isConfirmed) {
        try {
          await activateVehicle(userId, id);
          getVehicles(userId);
          getVehiclesActive(userId);
          getVehiclesInactive(userId);

          Swal.fire(
            "Vehiculo activado !",
            "El vehiculo ha sido activado con exito",
            "success"
          );
        } catch (error) {
          throw error;
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
    vehiclesActive,
    vehiclesInactive,
    visibleFormVehicle,
    vehicleSelected,
    errorsVehicle,
    handlerAddVehicle,
    handlerRemoveVehicle,
    handlerActivateVehicle,
    handlerInitialErrorsVehicle,
    getVehicles,
    getVehiclesActive,
    getVehiclesInactive,
    getVehicleTypes,
    handlerVehicleSelectedForm,
    handlerOpenFormVehicle,
    handlerCloseFormVehicle,
    initialVehicleForm,
    vehicleTypes,
  };
};
