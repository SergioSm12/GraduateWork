import React, { useState } from "react";
import logoUsta from "../../assets/pngwing.com.png";
import {
  RiMailLine,
  RiLockLine,
  RiEyeLine,
  RiEyeOffLine,
  RiRoadsterLine,
} from "react-icons/ri";
import { Link } from "react-router-dom";
import { useAuth } from "../../auth/hooks/useAuth";
import Swal from "sweetalert2";

const initialLoginForm = {
  email: "",
  password: "",
};

export const Login = () => {
  const { handlerLogin } = useAuth();
  const [loginForm, setLoginForm] = useState(initialLoginForm);
  const { email, password } = loginForm;

  const [showPassword, setShowPassword] = useState(false);

  const onInputChange = ({ target }) => {
    const { name, value } = target;
    setLoginForm({
      ...loginForm,
      [name]: value,
    });
  };

  const onSubmit = (event) => {
    event.preventDefault();
    /*
    if (!email || !password) {
      Swal.fire("Error de validacion", "Email o password requeridos", "error");
    }*/

    handlerLogin({ email, password });
    setLoginForm(initialLoginForm);
  };
  return (
    <div className="bg-secondary-100 p-8 rounded-xl shadow-2xl w:auto lg:w-[450px]">
      <h1 className="text-3xl uppercase font-bold tracking-[5px] text-white mb-8">
        Iniciar <span className="text-primary">sesión</span>
      </h1>
      <form className="mb-8" onSubmit={onSubmit}>
        <button className="flex items-center justify-center py-3 px-4 gap-4 bg-secondary-900 w-full rounded-full mb-8 text-gray-100">
          <img src={logoUsta} className="w-8 h-8" />
          Universidad Santo Tomás <RiRoadsterLine />
        </button>
        <div className="relative mb-4">
          <RiMailLine className="absolute top-1/2 -translate-y-1/2 left-2 text-primary" />
          <input
            type="email"
            className="py-3 pl-8 pr-4 bg-secondary-900 w-full outline-none rounded-lg focus:border focus:border-primary"
            placeholder="Correo electronico"
            name="email"
            value={email}
            onChange={onInputChange}
          />
        </div>
        <div className="relative mb-8">
          <RiLockLine className="absolute top-1/2 -translate-y-1/2 left-2 text-primary" />
          <input
            type={showPassword ? "text" : "password"}
            className="py-3 px-8 pr-4 bg-secondary-900 w-full outline-none rounded-lg focus:border focus:border-primary"
            placeholder="Contraseña"
            name="password"
            value={password}
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

        <div>
          <button
            type="submit"
            className="bg-primary text-black uppercase font-bold text-sm w-full py-3 px-4 rounded-lg "
          >
            Ingresar{" "}
          </button>
        </div>
      </form>
      <div className="flex flex-col items-center gap-4">
        <Link
          to="/auth/forget-password"
          className="hover:text-primary transition-colors"
        >
          ¿Olvidaste tu constraseña?
        </Link>
        <span className="flex items-center gap-2">
          ¿No tienes cuenta ?
          <Link
            to="/auth/register"
            className="text-primary hover:text-gray-100 transition-colors"
          >
            Registrate
          </Link>
        </span>
        <span className="flex items-center gap-2">
          ¿Eres visitante?{" "}
          <Link
            className="text-primary hover:text-gray-100 transition-colors"
            to={"/auth/register-visitor"}
          >
            Registra tu vehículo
          </Link>
        </span>
      </div>
    </div>
  );
};
