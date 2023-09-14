import { useDispatch, useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";
import { loginUser } from "../services/authService";
import {
  onLogin,
  onLogout,
  onInitLogin,
} from "../../store/slices/auth/authSlice";
import Swal from "sweetalert2";

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
      const claims = JSON.parse(window.atob(token.split(".")[1]));
      const user = { email: response.data.user };
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
