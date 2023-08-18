import React from "react";
import { useDispatch, useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";
import { loginUser } from "../services/authService";
import { onLogin, onLogout } from "../../store/slices/auth/authSlice";
import Swal from "sweetalert2";

export const useAuth = () => {
  const dispatch = useDispatch();

  const { user, isAdmin, isGuard, isAuth } = useSelector((state) => state.auth);

  const navigate = useNavigate();

  const handlerLogin = async ({ email, password }) => {
    try {
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
      sessionStorage.setItem("token",`Bearer ${token}`);
      navigate("/users");
    } catch (error) {
      dispatch(onLogout());
      if (error.response?.status == 401) {
        Swal.fire("Error Login", "Username o password invalidos", "error");
      } else if (error.response?.status == 403) {
        Swal.fire(
          "Error Login",
          "No tiene acceso al recurso o permisos!",
          "error"
        );
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
    login: { user, isAdmin, isGuard, isAuth },
    handlerLogin,
    handlerLogout,
  };
};
