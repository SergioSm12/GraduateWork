import { useDispatch, useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";
import { loginUser } from "../services/authService";
import {
  onLogin,
  onLogout,
  onInitLogin,
} from "../../store/slices/auth/authSlice";
import Swal from "sweetalert2";
import { jwtDecode } from "jwt-decode";

export const useAuth = () => {
  const dispatch = useDispatch();

  const { user, isAdmin, isGuard, isAuth, isLoginLoading } = useSelector(
    (state) => state.auth
  );

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

  const handlerLogin = async ({ email, password }) => {
    try {
      dispatch(onInitLogin());
      const response = await loginUser({ email, password });
      const token = response.data.token;
      const claims = jwtDecode(token);
      const user = { email: response.data.user };
      const currentTime = Date.now() / 1000;

      //verificar si el token ha expirado
      if (claims.exp < currentTime) {
        throw new Error("Token ha expirado");
      }

      dispatch(
        onLogin({ user, isAdmin: claims.isAdmin, isGuard: claims.isGuard })
      );

      sessionStorage.setItem(
        "login",
        JSON.stringify({
          isAuth: true,
          isAdmin: claims.isAdmin,
          isGuard: claims.isGuard,
          user: user,
        })
      );
      sessionStorage.setItem("token", `Bearer ${token}`);

      Toast.fire({
        icon: "success",
        title: `Bienvenido ${email}`,
      });
      navigate("/");
    } catch (error) {
      dispatch(onLogout());
      if (error.response?.status == 401) {
        Swal.fire({
          position: "top",
          icon: "error",
          title: "correo o contraseña requeridos",
          showConfirmButton: false,
          timer: 1500,
        });
      } else if (error.response?.status == 403) {
        Swal.fire({
          position: "top",
          icon: "error",
          title: "Error de validacion",
          showConfirmButton: false,
          timer: 1500,
        });
      } else if (error.message === "Token ha expirado") {
        Swal.fire({
          position: "top",
          icon: "error",
          title: "La sesión ha expirado",
          showConfirmButton: false,
          timer: 1500,
        });
      } else {
        throw error;
      }
    }
  };

  const handlerLogout = () => {
    dispatch(onLogout());
    sessionStorage.removeItem("token");
    sessionStorage.removeItem("login");
    sessionStorage.clear();
  };

  return {
    login: { user, isAdmin, isGuard, isAuth, isLoginLoading },
    handlerLogin,
    handlerLogout,
  };
};
