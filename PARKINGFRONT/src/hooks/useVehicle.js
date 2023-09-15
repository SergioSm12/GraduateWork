import { useDispatch, useSelector } from "react-redux";
import { findAllVehiclesByUser } from "../services/vehicleService";
import { loadingVehicles } from "../store/slices/vehicle/vehicleSlice";

export const useVehicle = () => {
  const { vehicles } = useSelector((state) => state.vehicles);
  const dispatch = useDispatch();

  const getVehicles = async (id) => {
    try {
      const result = await findAllVehiclesByUser(id);
      dispatch(loadingVehicles(result.data));
    } catch (error) {
      console.log(error);
    }
  };
  return { vehicles, getVehicles };
};
