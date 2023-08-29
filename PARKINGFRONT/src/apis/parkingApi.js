import axios from "axios";

const parkingApi = axios.create({
  baseURL: `${import.meta.env.VITE_API_BASE_URL}`,
});

parkingApi.interceptors.request.use((config) => {
  config.headers = {
    ...config.headers,
    Authorization: sessionStorage.getItem("token"),
  };
  return config;
});

export default parkingApi;
