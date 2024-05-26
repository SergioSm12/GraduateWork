import React from "react";
import { Navigate, Route, Routes } from "react-router-dom";
import { LayoutAuth } from "./layout/LayoutAuth";
import { Login } from "./pages/auth/Login";
import { Register } from "./pages/auth/Register";
import { Error404 } from "./pages/Error404";
import { LayoutAdmin } from "./layout/LayoutAdmin";
import { ForgetPassword } from "./pages/auth/ForgetPassword";
import { useAuth } from "./auth/hooks/useAuth";
import { Home } from "./pages/admin/Home";
import { UserRoutes } from "./routes/UserRoutes";
import { Rates } from "./pages/admin/Rates";
import { VehicleTypes } from "./pages/admin/VehicleTypes";
import { RegisterVisitor } from "./pages/visitor/RegisterVisitor";

import { ReportRutes } from "./routes/ReportRutes";
import { Aforo } from "./pages/admin/Aforo";
import { LayoutSecurityGuard } from "./layout/LayoutSecurityGuard";
import { LayoutUser } from "./layout/LayoutUser";
import { HomeSecurityGuard } from "./pages/guard/HomeSecurityGuard";
import { ShowUser } from "./components/Users/ShowUser";

export const AppRoutes = () => {
  const { login } = useAuth();

  if (login.isLoginLoading) {
    return (
      <div className="container my-4 flex justify-center items-center h-screen">
        <div className="animate-spin rounded-full border-t-4 border-secondary border-opacity-50 h-12 w-12"></div>
      </div>
    );
  }
  
  return (
    <Routes>
      {login.isAuth ? (
        login.isAdmin ? (
          <Route path="/" element={<LayoutAdmin />}>
            <Route index element={<Home />} />
            <Route path="users/*" element={<UserRoutes />} />
            <Route path="rate" element={<Rates />} />
            <Route path="vehicleType" element={<VehicleTypes />} />
            <Route path="reports/*" element={<ReportRutes />} />
            <Route path="aforo" element={<Aforo />} />
          </Route>
        ) : login.isGuard ? (
          <Route path="/" element={<LayoutSecurityGuard />}>
            <Route index element={<HomeSecurityGuard />} />
          </Route>
        ) : (
          <Route path="/" element={<LayoutUser />}>
            <Route index element={<ShowUser/>}/>
          </Route>
        )
      ) : (
        <>
          <Route path="/auth" element={<LayoutAuth />}>
            <Route index element={<Login />} />
            <Route path="register" element={<Register />} />
            <Route path="forget-password" element={<ForgetPassword />} />
            <Route path="register-visitor" element={<RegisterVisitor />} />
          </Route>
          <Route path="/*" element={<Navigate to={"/auth"} />} />
        </>
      )}
    </Routes>
  );
};
