import React, { useState } from "react";

import { Link } from "react-router-dom";
import logoUsta from "../../assets/pngwing.com.png";
//Icons
import {
  RiMailLine,
  RiLockLine,
  RiEyeLine,
  RiEyeOffLine,
  RiUserLine,
  RiRoadsterLine,
  RiSmartphoneLine,
} from "react-icons/ri";
import { useUsers } from "../../hooks/useUsers";

export const Register = () => {
  const { initialUserForm, handlerAddUser, errors, handlerInitialErrors } =
    useUsers();

  //estado para el formulario
  const [userForm, setUserForm] = useState(initialUserForm);

  const [showPassword, setShowPassword] = useState(false);
  const [passwordMatch, setPasswordMatch] = useState(true);
  const [confirmPassword, setConfirmPassword] = useState("");

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

  const onSubmit = (e) => {
    e.preventDefault();

    if (userForm.password !== confirmPassword) {
      handlerInitialErrors();
      setPasswordMatch(false);
    } else {
      handlerAddUser(userForm, "/auth");
    }
  };

  return (
    <div className="bg-secondary-100 p-8 rounded-xl shadow-2xl w:auto lg:w-[450px]">
      <h1 className="text-3xl uppercase font-bold tracking-[5px] text-white mb-8">
        Crear <span className="text-primary">cuenta</span>
      </h1>
      <form className="mb-8" onSubmit={onSubmit}>
        <div className="flex items-center justify-center py-3 px-4 gap-4 bg-secondary-900 w-full rounded-full mb-8 text-gray-100">
          <img src={logoUsta} className="w-8 h-8" />
          Universidad Santo Tomás <RiRoadsterLine />
        </div>
        <div className="relative ">
          <RiUserLine className="absolute top-1/2 -translate-y-1/2 left-2 text-primary" />
          <input
            type="text"
            className="py-3 pl-8 pr-4 bg-secondary-900 w-full outline-none rounded-lg focus:border focus:border-primary"
            placeholder="Nombre(s)"
            name="name"
            value={userForm.name}
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
            value={userForm.lastName}
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
            value={userForm.email}
            onChange={onInputChange}
          />
        </div>
        <div className="relative mb-4">
          <p className="text-red-500">{errors?.email}</p>
        </div>
        <div className="relative">
          <RiLockLine className="absolute top-1/2 -translate-y-1/2 left-2 text-primary" />
          <input
            type={showPassword ? "text" : "password"}
            className="py-3 px-8 pr-4 bg-secondary-900 w-full outline-none rounded-lg focus:border focus:border-primary"
            placeholder="Contraseña"
            name="password"
            value={userForm.password}
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
        <div className="relative ">
          <RiSmartphoneLine className="absolute top-1/2 -translate-y-1/2 left-2 text-primary" />
          <input
            type="text"
            className="py-3 pl-8 pr-4 bg-secondary-900 w-full outline-none rounded-lg focus:border focus:border-primary"
            placeholder="Numero de celular"
            name="phoneNumber"
            value={userForm.phoneNumber}
            onChange={onInputChange}
          />
        </div>
        <div className="relative mb-4">
          <p className="text-red-500">{errors?.phoneNumber}</p>
        </div>

        <div>
          <button
            type="submit"
            className="bg-primary text-black uppercase font-bold text-sm w-full py-3 px-4 rounded-lg "
          >
            Registrarme
          </button>
        </div>
      </form>
      <span className="flex items-center justify-center gap-2 mb-2">
        ¿Ya tienes cuenta ?
        <Link
          to="/auth"
          className="text-primary hover:text-gray-100 transition-colors"
        >
          Ingresa
        </Link>
      </span>

      <span className="flex items-center justify-center gap-2">
        ¿Eres visitante?{" "}
        <Link
          className="text-primary hover:text-gray-100 transition-colors"
          to="/auth/register-visitor"
        >
          Registra tu vehículo
        </Link>
      </span>
    </div>
  );
};
