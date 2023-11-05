import { useDispatch, useSelector } from "react-redux";
import {
  findAllVehicleType,
  removeVehicleType,
  saveVehicleType,
  updateVehicleType,
} from "../services/vehicleTypeService";
import {
  addVehicleTypeSlice,
  loadingVehicleTypes,
  onVehicleTypeSelected,
  removeVehicleTypeSlice,
  updateVehicleTypeSlice,
  vehicleTypeForm,
} from "../store/slices/vehicle/vehicleTypeSlice";
import Swal from "sweetalert2";
import { useAuth } from "../auth/hooks/useAuth";
import { useNavigate } from "react-router-dom";

export const useVehicleType = () => {
  const { vehicleTypes, errorsVehicleType, vehicleTypeSelected } = useSelector(
    (state) => state.vehicleType
  );

  const dispatch = useDispatch();
  const navigate = useNavigate();
  const { handlerLogout } = useAuth();
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

  const getVehicleTypes = async () => {
    try {
      const result = await findAllVehicleType();
      dispatch(loadingVehicleTypes(result.data));
    } catch (error) {
      throw error;
    }
  };

  const handlerAddVehicleType = async (vehicleType) => {
    let response;
    try {
      if (vehicleType.id === 0) {
        response = await saveVehicleType(vehicleType);
        dispatch(addVehicleTypeSlice(response.data));
      } else {
        response = await updateVehicleType(vehicleType);
        dispatch(updateVehicleTypeSlice(response.data));
      }
      Toast.fire({
        icon: "success",
        title:
          vehicleType.id === 0
            ? "Tipo de vehiculo creado"
            : "Tipo de vehiculo actualizado",
      });
      navigate("/vehicleType");
    } catch (error) {
      if (error.response && error.response.status == 400) {
        dispatch(loadingErrorRates(error.response.data));
      } else if (error.response?.status == 401) {
        handlerLogout();
      } else {
        throw error;
      }
    }
  };

  const handlerRemoveVehicleType = (id) => {
    Swal.fire({
      title: "Esta seguro que desea eliminar?",
      text: "Cuidado el tipo de vehículo sera eliminado",
      icon: "warning",
      showCancelButton: true,
      confirmButtonColor: "#1E293C",
      cancelButtonColor: "#d33",
      confirmButtonText: "Si, eliminar!",
    }).then(async (result) => {
      if (result.isConfirmed) {
        try {
          await removeVehicleType(id);
          dispatch(removeVehicleTypeSlice(id));

          Swal.fire(
            "Tipo de vehiculo eliminado!",
            "El tipo de vehículo ha sido eliminado con exito",
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

  const handlerVehicleTypeSelectedForm = (vehicleType) => {
    dispatch(onVehicleTypeSelected({ ...vehicleType }));
  };

  return {
    vehicleTypes,
    getVehicleTypes,
    handlerAddVehicleType,
    vehicleTypeForm,
    errorsVehicleType,
    vehicleTypeSelected,
    handlerVehicleTypeSelectedForm,
    handlerRemoveVehicleType
  };
};
