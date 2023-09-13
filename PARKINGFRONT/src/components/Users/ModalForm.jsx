import React from "react";
import { RiFileUserLine } from "react-icons/ri";
import { CreateUser } from "./CreateUser";
import { useUsers } from "../../hooks/useUsers";

export const ModalForm = () => {
    const {handlerCloseFormCreate} = useUsers();
  return (
    <div className="fixed inset-0 bg-black bg-opacity-30 backdrop-blur-sm flex justify-center items-center">
      <div className="bg-secondary-900 p-8  rounded-lg ">
        <CreateUser 
        handlerCloseFormCreate={handlerCloseFormCreate}
        />
      </div>
    </div>
  );
};
