import React from "react";
import { Navigate, Route, Routes } from "react-router-dom";
import { LayoutAuth } from "./layout/LayoutAuth";
import { Login } from "./pages/auth/Login";
import { Register } from "./pages/auth/Register";
import { Error404 } from "./pages/Error404";
import { LayoutAdmin } from "./layout/LayoutAdmin";
import { ForgetPassword } from "./pages/auth/ForgetPassword";
import { useAuth } from "./auth/hooks/useAuth";

export const AppRoutes = () => {
  const { login } = useAuth();

  return (
    <Routes>
      {login.isAuth ? (
        <Route path="/*" element={<LayoutAdmin />}></Route>
      ) : (
        <>
          <Route path="/auth" element={<LayoutAuth />}>
            <Route index element={<Login />} />
            <Route path="register" element={<Register />} />
            <Route path="forget-password" element={<ForgetPassword />} />
          </Route>
          <Route path="/*" element={<Navigate to={"/auth"} />} />
        </>
      )}
    </Routes>
  );
};
