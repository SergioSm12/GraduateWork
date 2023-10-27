import React from "react";
import { CreateReceipt } from "./CreateReceipt";

export const ModalFormReceipt = () => {
  return (
    <div className="abrir-modal animacion fadeIn">
      <div className="fixed inset-0 bg-black bg-opacity-30 backdrop-blur-sm flex justify-center items-center transition-opacity duration-300">
        <div className="bg-secondary-900 p-8  rounded-lg ">
          <CreateReceipt />
        </div>
      </div>
    </div>
  );
};
