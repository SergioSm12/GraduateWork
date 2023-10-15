import React, { useEffect, useState } from "react";
import {
  RiAdminLine,
  RiDiscussLine,
  RiFileUserLine,
  RiQuestionAnswerFill,
  RiSmartphoneLine,
  RiUser3Line,
  RiUserSettingsLine,
} from "react-icons/ri";
import { useUsers } from "../../hooks/useUsers";

export const ShowUserInformation = ({ userByid }) => {
  const { initialUserForm } = useUsers();
  const [userShow, setUserShow] = useState(initialUserForm);
  useEffect(() => {
    setUserShow({
      ...userByid,
      password: "",
    });
  }, [userByid]);
  return (
    <div className="bg-secondary-900 p-8 rounded-lg xl:mb-8">
      <h1 className="text-2xl text-white mb-8">Información</h1>
      <div>
        <div className="flex items-center gap-4 mb-8">
          <RiFileUserLine className="text-4xl text-primary" />
          <div className="flex flex-col gap-1">
            <h5 className="text-white">Nombre</h5>
            <p className="text-xs">
              {userShow.name} {userShow.lastName}
            </p>
          </div>
        </div>
        <div className="flex items-center gap-4 mb-8">
          <RiDiscussLine className="text-4xl text-primary" />
          <div className="flex flex-col gap-1">
            <h5 className="text-white">Correo</h5>
            <p className="text-xs">{userShow.email}</p>
          </div>
        </div>
        <div className="flex items-center gap-4 mb-8">
          <RiSmartphoneLine className="text-4xl text-primary" />
          <div className="flex flex-col gap-1">
            <h5 className="text-white">Phone</h5>
            <p className="text-xs">{userShow.phoneNumber}</p>
          </div>
        </div>
        {userShow.admin == true && userShow.guard == true ? (
          <div className="flex items-center gap-4 mb-8">
            <RiUserSettingsLine className="text-4xl text-primary" />
            <div className="flex flex-col gap-1">
              <h5 className="text-white">Role</h5>
              <p className="text-xs">Administrador</p>
            </div>
          </div>
        ) : userShow.admin === true ? (
          <div className="flex items-center gap-4 mb-8">
            <RiUserSettingsLine className="text-4xl text-primary" />
            <div className="flex flex-col gap-1">
              <h5 className="text-white">Role</h5>
              <p className="text-xs">Administrador</p>
            </div>
          </div>
        ) : (
          ""
        )}
        {userShow.admin === false && userShow.guard === false ? (
          <div className="flex items-center gap-4 mb-8">
            <RiUser3Line className="text-4xl text-primary" />
            <div className="flex flex-col gap-1">
              <h5 className="text-white">Role</h5>
              <p className="text-xs">Usuario</p>
            </div>
          </div>
        ) : (
          ""
        )}
        {userShow.guard === true && userShow.admin === false ? (
          <div className="flex items-center gap-4 mb-8">
            <RiAdminLine className="text-4xl text-primary" />
            <div className="flex flex-col gap-1">
              <h5 className="text-white">Role</h5>
              <p className="text-xs">Celador</p>
            </div>
          </div>
        ) : (
          ""
        )}
      </div>
    </div>
  );
};
