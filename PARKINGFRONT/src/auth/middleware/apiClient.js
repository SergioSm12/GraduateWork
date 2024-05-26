import axios from "axios";
import { jwtDecode } from "jwt-decode";
import { onLogout } from "../../store/slices/auth/authSlice";
import { store } from "../../store/store";
import Swal from "sweetalert2";

const apiClient = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL,
});

const handleTokenExpiration = () => {
  store.dispatch(onLogout());
  sessionStorage.clear();
  Swal.fire({
    position: "top",
    icon: "error",
    title: "La sesión ha expirado",
    showConfirmButton: false,
    timer: 1500,
  });
};

apiClient.interceptors.request.use(
  (config) => {
    const token = sessionStorage.getItem("token");
    if (token) {
      const claims = jwtDecode(token.replace("Bearer ", ""));
      const currentTime = Date.now() / 1000;
      if (claims.exp < currentTime) {
        handleTokenExpiration();
        return Promise.reject(new Error("Token ha expirado"));
      }
      config.headers.Authorization = token;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

apiClient.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response.status === 401) {
      handleTokenExpiration();
    }
    return Promise.reject(error);
  }
);

export default apiClient;
