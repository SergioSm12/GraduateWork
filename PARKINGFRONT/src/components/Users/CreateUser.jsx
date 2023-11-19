import React, { useEffect, useState } from "react";

//Icons
import {
  RiMailLine,
  RiLockLine,
  RiEyeLine,
  RiEyeOffLine,
  RiUserLine,
  RiSmartphoneLine,
  RiCloseCircleLine,
} from "react-icons/ri";
import { useUsers } from "../../hooks/useUsers";

export const CreateUser = ({ userSelected, handlerCloseFormCreate }) => {
  const {
    initialUserForm,
    handlerAddUser,
    errors,
    handlerInitialErrors,
    getActiveUsers,
  } = useUsers();

  //estado para el formulario
  const [userForm, setUserForm] = useState({ initialUserForm, password: "" });

  const [showPassword, setShowPassword] = useState(false);
  const [passwordMatch, setPasswordMatch] = useState(true);
  const [confirmPassword, setConfirmPassword] = useState("");

  useEffect(() => {
    setUserForm({
      ...userSelected,
      password: userSelected.password || "",
    });
  }, [userSelected]);



  const onConfirmPasswordChange = (event) => {
    setConfirmPassword(event.target.value);
    setPasswordMatch(userForm.password === event.target.value);
  };
  const onInputChange = ({ target }) => {
    const { name, value } = target;

    setUserForm({
      ...userForm,
      [name]: value,
    });
  };

  const onCheckboxChange = (e, checkboxName) => {
    const { checked } = e.target;
    setUserForm((prevState) => ({
      ...prevState,
      [checkboxName]: checked,
    }));
  };

  const onSubmit = (e) => {
    e.preventDefault();

    if (userForm.password !== confirmPassword) {
      handlerInitialErrors();
      setPasswordMatch(false);
    } else {
      handlerAddUser(userForm, "/users").then(() => {
        getActiveUsers();
      });
    }
  };

  const onCloseForm = () => {
    handlerCloseFormCreate();
    setUserForm(initialUserForm);
  };

  return (
    <div className="bg-secondary-100 p-8 rounded-xl shadow-2xl w:auto lg:w-[450px]">
      <div className="flex items-start justify-between">
        <h1 className=" text-2xl uppercase font-bold tracking-[5px] text-white mb-8">
          Crear <span className="text-primary">usuario</span>
        </h1>

        <button
          className=" py-2 px-2 text-red-600 hover:text-black bg-secondary-900/80  hover:bg-red-600/50 rounded-lg  transition-colors"
          type="button"
          onClick={() => onCloseForm()}
        >
          <RiCloseCircleLine className="text-2xl " />
        </button>
      </div>

      <form className="mb-8" onSubmit={onSubmit}>
        <div className="relative ">
          <RiUserLine className="absolute top-1/2 -translate-y-1/2 left-2 text-primary" />
          <input
            type="text"
            className="py-3 pl-8 pr-4 bg-secondary-900 w-full outline-none rounded-lg focus:border focus:border-primary"
            placeholder="Nombre(s)"
            name="name"
            value={userForm.name || ""}
            onChange={onInputChange}
          />
        </div>
        <div className="relative mb-4">
          <p className="text-red-500">{errors?.name}</p>
        </div>

        <div className="relative">
          <RiUserLine className="absolute top-1/2 -translate-y-1/2 left-2 text-primary" />
          <input
            type="text"
            className="py-3 pl-8 pr-4 bg-secondary-900 w-full outline-none rounded-lg focus:border focus:border-primary"
            placeholder="Apellido(s)"
            name="lastName"
            value={userForm.lastName || ""}
            onChange={onInputChange}
          />
        </div>
        <div className="relative mb-4">
          <p className="text-red-500">{errors?.lastName}</p>
        </div>
        <div className="relative">
          <RiMailLine className="absolute top-1/2 -translate-y-1/2 left-2 text-primary" />
          <input
            type="email"
            className="py-3 pl-8 pr-4 bg-secondary-900 w-full outline-none rounded-lg focus:border focus:border-primary"
            placeholder="Coreo electronico"
            name="email"
            value={userForm.email || ""}
            onChange={onInputChange}
          />
        </div>
        <div className="relative mb-4">
          <p className="text-red-500">{errors?.email}</p>
        </div>
        {userForm.id > 0 || (
          <>
            <div className="relative">
              <RiLockLine className="absolute top-1/2 -translate-y-1/2 left-2 text-primary" />
              <input
                type={showPassword ? "text" : "password"}
                className="py-3 px-8 pr-4 bg-secondary-900 w-full outline-none rounded-lg focus:border focus:border-primary"
                placeholder="Contraseña"
                name="password"
                value={userForm.password || ""}
                onChange={onInputChange}
              />
              {showPassword ? (
                <RiEyeOffLine
                  onClick={() => setShowPassword(!showPassword)}
                  className="absolute top-1/2 -translate-y-1/2 right-2 hover:cursor-pointer text-primary"
                />
              ) : (
                <RiEyeLine
                  onClick={() => setShowPassword(!showPassword)}
                  className="absolute top-1/2 -translate-y-1/2 right-2 hover:cursor-pointer text-primary"
                />
              )}
            </div>
            <div className="relative mb-4">
              <p className="text-red-500">{errors?.password}</p>
            </div>
            <div className="relative">
              <RiLockLine className="absolute top-1/2 -translate-y-1/2 left-2 text-primary" />
              <input
                type={showPassword ? "text" : "password"}
                className="py-3 px-8 pr-4 bg-secondary-900 w-full outline-none rounded-lg focus:border focus:border-primary"
                placeholder="Confirmar contraseña"
                name="confirmPassword"
                value={confirmPassword}
                onChange={onConfirmPasswordChange}
              />
              {showPassword ? (
                <RiEyeOffLine
                  onClick={() => setShowPassword(!showPassword)}
                  className="absolute top-1/2 -translate-y-1/2 right-2 hover:cursor-pointer text-primary"
                />
              ) : (
                <RiEyeLine
                  onClick={() => setShowPassword(!showPassword)}
                  className="absolute top-1/2 -translate-y-1/2 right-2 hover:cursor-pointer text-primary"
                />
              )}
            </div>
            <div className="relative mb-4">
              {!passwordMatch && (
                <p className="text-red-500">La contraseña no coincide</p>
              )}
            </div>
          </>
        )}
        <div className="relative ">
          <RiSmartphoneLine className="absolute top-1/2 -translate-y-1/2 left-2 text-primary" />
          <input
            type="text"
            className="py-3 pl-8 pr-4 bg-secondary-900 w-full outline-none rounded-lg focus:border focus:border-primary"
            placeholder="Numero de celular"
            name="phoneNumber"
            value={userForm.phoneNumber || ""}
            onChange={onInputChange}
          />
        </div>
        <div className="relative mb-4">
          <p className="text-red-500">{errors?.phoneNumber}</p>
        </div>
        <div className="mb-8 flex flex-col md:flex-row md:items-center md:justify-between">
          {/*Aqui van los checkbox para guarda y administrador  */}
          <div className="relative">
            <label htmlFor="checkAdmin" className="flex items-center space-x-2">
              <input
                className=""
                type="checkbox"
                id="checkAdmin"
                name="admin"
                checked={userForm.admin || ""}
                onChange={(e) => onCheckboxChange(e, "admin")}
              />
              <span className="text-sm hover:text-primary ">Administrador</span>
            </label>
          </div>
          <div className="relative">
            <label htmlFor="checkGuard" className="flex items-center space-x-2">
              <input
                className=""
                type="checkbox"
                id="checkGuard"
                name="guard"
                checked={userForm.guard || ""}
                onChange={(e) => onCheckboxChange(e, "guard")}
              />
              <span className="text-sm hover:text-primary">
                Guarda de seguridad
              </span>
            </label>
          </div>
        </div>
        <input type="hidden" name="id" value={userForm.id || ""} />
        <div>
          <button
            type="submit"
            className="bg-primary text-black uppercase font-bold text-sm w-full py-3 px-4 rounded-lg "
          >
            {userForm.id > 0 ? "Editar" : "Crear"}
          </button>
        </div>
      </form>
      <hr className="border border-dashed border-gray-500/50 mb-2" />
    </div>
  );
};
