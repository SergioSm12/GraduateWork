import { useDispatch, useSelector } from "react-redux";
import { useAuth } from "../auth/hooks/useAuth";
import Swal from "sweetalert2";
import {
  findAllBuilding,
  removeBuilding,
  saveBuilding,
  updateBuilding,
} from "../services/buildingService";
import {
  addBuilding,
  loadingBuildings,
  loadingErrorBuilding,
  updateBuildingSlice,
  initialBuildingForm,
  onBuildingSelected,
  removeBuildingsSlice,
} from "../store/slices/building/buildingSlice";

export const useBuildings = () => {
  const { buildings, errorsBuilding, buildingSelected } = useSelector(
    (state) => state.buildings
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

  const getBuildings = async () => {
    try {
      const result = await findAllBuilding();
      dispatch(loadingBuildings(result.data));
    } catch (error) {
      if (error.response.status == 401) {
        handlerLogout();
      } else {
        throw error;
      }
    }
  };

  const handlerAddBuilding = async (building) => {
    let response;
    try {
      if (building.id === 0) {
        response = await saveBuilding(building);
        dispatch(addBuilding(response.data));
      } else {
        response = await updateBuilding(building);
        dispatch(updateBuildingSlice(response.data));
      }

      Toast.fire({
        icon: "success",
        title: building.id === 0 ? "Edificio creado" : "Edificio actualizado",
      });
    } catch (error) {
      if (error.response && error.response.status == 400) {
        dispatch(loadingErrorBuilding(error.response.data));
      } else if (error.response?.status == 401) {
        handlerLogout();
      } else {
        throw error;
      }
    }
  };

  const handlerRemoveBuilding = (id) => {
    Swal.fire({
      title: "¿Esta seguro que desea eliminar?",
      text: "Cuidado el edificio sera eliminado ",
      icon: "warning",
      showCancelButton: true,
      confirmButtonColor: "#1E293C",
      cancelButtonColor: "#d33",
      confirmButtonText: "Si, eliminar!",
    }).then(async (result) => {
      if (result.isConfirmed) {
        try {
          //Eliminar desde el backend
          await removeBuilding(id);

          dispatch(removeBuildingsSlice(id));

          Swal.fire(
            "Edificio eliminado!",
            "El edificio ha sido eliminada con exito",
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

  const handlerBuildingSelected = (building) => {
    dispatch(onBuildingSelected({ ...building }));
  };

  return {
    initialBuildingForm,
    buildings,
    getBuildings,
    handlerAddBuilding,
    errorsBuilding,
    buildingSelected,
    handlerBuildingSelected,
    handlerRemoveBuilding,
  };
};
