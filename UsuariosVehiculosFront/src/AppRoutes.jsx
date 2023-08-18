import React from "react";
import { Navigate, Route, Routes } from "react-router-dom";
import { useAuth } from "./auth/hooks/useAuth";
import { UserRoutes } from "./routes/UserRoutes";
import { LoginPage } from "./auth/pages/LoginPage";

export const AppRoutes = () => {
  const { login } = useAuth();
  return (
    <Routes>
      {login.isAuth ? (
        //Rutas Privadas
        <Route path="/*" element={<UserRoutes />} />
      ) : (
        <>
          <Route path="/login" element={<LoginPage />} />
          <Route path="/*" element={<Navigate to={"/login"} />} />
        </>
      )}
    </Routes>
  );
};
