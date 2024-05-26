import React from "react";
import { Header } from "../components/Header";
import { Outlet } from "react-router-dom";

export const LayoutUser = () => {
  return (
    <div className="min-h-screen grid grid-cols-1 xl:grid-cols-6">
      <div className="xl:col-span-6 ">
        <Header />
        <div className="h-[90vh] overflow-y-scroll p-8">
          <Outlet />
        </div>
      </div>
    </div>
  );
};
