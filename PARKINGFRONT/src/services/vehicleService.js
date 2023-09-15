import parkingApi from "../apis/parkingApi";

const BASE_URL_VEHICLES = "/vehicle";

export const findAllVehiclesByUser = async (id=0)=>{
    try {
        const response = await parkingApi.get(`${BASE_URL_VEHICLES}/${id}/list`);
        return response; 
    } catch (error) {
        throw error;
    }
}