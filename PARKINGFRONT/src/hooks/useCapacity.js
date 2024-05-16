import React from "react";
import { useDispatch, useSelector } from "react-redux";
import { useAuth } from "../auth/hooks/useAuth";
import Swal from "sweetalert2";
import {
  findAllCapacity,
  removeCapacity,
  saveCapacity,
  updateCapacity,
} from "../services/capacityService";
import {
  addCapacity,
  loadingCapacities,
  loadingErrorCapacity,
  initialCapacityForm,
  updateCapacitySlice,
  onCapacitySelected,
  removeCapacitiesSlice,
} from "../store/slices/capacity/capacitySlice";

export const useCapacity = () => {
  const { capacities, capacitySelected, errorsCapacity } = useSelector(
    (state) => state.capacities
  );
  const dispatch = useDispatch();
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

  const getCapacities = async () => {
    try {
      const result = await findAllCapacity();
      dispatch(loadingCapacities(result.data));
    } catch (error) {
      if (error.response.status == 401) {
        handlerLogout();
      } else {
        throw error;
      }
    }
  };

  const handlerAddCapacity = async (capacity) => {
    let response;
    try {
      if (capacity.id === 0) {
        response = await saveCapacity(capacity);
        dispatch(addCapacity(response.data));
      } else {
        response = await updateCapacity(capacity);
        dispatch(updateCapacitySlice(response.data));
      }

      Toast.fire({
        icon: "success",
        title: capacity.id === 0 ? "Aforo  creado" : "Aforo  actualizado",
      });
    } catch (error) {
      if (error.response && error.response.status == 400) {
        dispatch(loadingErrorCapacity(error.response.data));
      } else if (error.response?.status == 401) {
        handlerLogout();
      } else {
        throw error;
      }
    }
  };

  const handlerCapacitySelected = (capacity) => {
    dispatch(onCapacitySelected({ ...capacity }));
  };

  const handlerRemoveCapacity = (id) => {
    Swal.fire({
      title: "¿Esta seguro que desea eliminar?",
      text: "Cuidado el aforo sera eliminado ",
      icon: "warning",
      showCancelButton: true,
      confirmButtonColor: "#1E293C",
      cancelButtonColor: "#d33",
      confirmButtonText: "Si, eliminar!",
    }).then(async (result) => {
      if (result.isConfirmed) {
        try {
          //Eliminar desde el backend
          await removeCapacity(id);

          dispatch(removeCapacitiesSlice(id));

          Swal.fire(
            "Aforo Eliminado!",
            "El aforo ha sido eliminada con exito",
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

  return {
    getCapacities,
    capacities,
    handlerAddCapacity,
    capacitySelected,
    errorsCapacity,
    initialCapacityForm,
    handlerCapacitySelected,
    handlerRemoveCapacity
  };
};
