import React from "react";
import { CreateUser } from "./CreateUser";
import { useUsers } from "../../hooks/useUsers";

export const ModalForm = () => {
  const { userSelected, handlerCloseFormCreate } = useUsers();
  return (
    <div className="abrir-modal animacion fadeIn">
      <div className="fixed inset-0 bg-black bg-opacity-30 backdrop-blur-sm flex justify-center items-center transition-opacity duration-300">
        <div className="bg-secondary-900 p-8  rounded-lg ">
          <CreateUser
            handlerCloseFormCreate={handlerCloseFormCreate}
            userSelected={userSelected}
          />
        </div>
      </div>
    </div>
  );
};
