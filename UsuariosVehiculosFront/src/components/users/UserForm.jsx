import React, { useEffect, useState } from "react";
import { useUsers } from "../../hooks/useUsers";
import { faculty } from "../../store/slices/usersSlice";

export const UserForm = ({ userSelected }) => {
  const {
    faculties,
    initialUserForm,
    handlerAddUser,
    errors,
    getFaculties,
    handlerInitialErrors,
  } = useUsers();

  const [userForm, setUserForm] = useState(initialUserForm);
  const [selectedFaculty, setSelectedFaculty] = useState(null);

  const [passwordMatch, setPasswordMatch] = useState(true);
  const [confirmPassword, setConfirmPassword] = useState("");
  const [validatefaculty, setValidateFaculty] = useState("");
  const [checkedAdmin, setCheckedAdmin] = useState(userForm.admin);
  const [checkedGuard, setCheckedGuard] = useState(userForm.guard);

  //si viene el usuario seleccionado se puebla en el form
  useEffect(() => {
    //rellenar select del faculty
    if (userSelected && !selectedFaculty) {
      setSelectedFaculty(userSelected.faculty);
    }

    setUserForm({
      ...userForm,
      ...userSelected,
      password: "",
    });
  }, [userSelected, selectedFaculty]);

  //Puebla el formulario para editar
  useEffect(() => {
    if (userForm.id > 0 && userForm.faculty) {
      setSelectedFaculty(userForm.faculty);
    } else {
      setSelectedFaculty(null);
    }
  }, []);

  useEffect(() => {
    getFaculties();
  }, []);

  //Monitorea y guarda el cambio del select
  useEffect(() => {
    setUserForm({
      ...userForm,
      faculty: selectedFaculty,
    });
  }, [selectedFaculty]);

  const onInputChange = ({ target }) => {
    const { name, value } = target;

    //validar facultad y traer
    if (name == "faculty") {
      const selectedFacultyId = target.value;
      const faculty = faculties.find(
        (f) => f.id === parseInt(selectedFacultyId)
      );
      setSelectedFaculty(faculty);
    }

    setUserForm({
      ...userForm,
      [name]: value,
    });
  };

  const onConfirmPasswordChange = (event) => {
    setConfirmPassword(event.target.value);
    setPasswordMatch(userForm.password === event.target.value);
  };

  const onCheckboxAdminChange = () => {
    setCheckedAdmin(!checkedAdmin);
    setUserForm({ ...userForm, admin: checkedAdmin });
  };
  const onCheckboxGuardChange = () => {
    setCheckedGuard(!checkedGuard);
    setUserForm({ ...userForm, guard: checkedGuard });
  };

  const onSubmit = (e) => {
    e.preventDefault();

    if (userForm.faculty == null) {
      setValidateFaculty("El campo: faculty no puede estar vacio");
    }

    if (userForm.password !== confirmPassword) {
      handlerInitialErrors();
      setPasswordMatch(false);
    } else {
      handlerAddUser(userForm);
    }
  };
  return (
    <div className="container my-4">
      <div className="card border-dark mb-3">
        <div className="card-header">Registro de Usuario</div>
        <div className="card-body ">
          <form onSubmit={onSubmit}>
            <input
              className="form-control my-3 mx-5 w-75"
              placeholder="Name"
              name="name"
              value={userForm.name}
              onChange={onInputChange}
            />
            <p className="text-danger">{errors?.name}</p>
            <input
              className="form-control my-3 mx-5 w-75"
              placeholder="last Name"
              name="lastName"
              value={userForm.lastName}
              onChange={onInputChange}
            />
            <p className="text-danger">{errors?.lastName}</p>

            {userForm.id > 0 || (
              <>
                <input
                  className="form-control my-3 mx-5 w-75"
                  placeholder="Password"
                  type="password"
                  name="password"
                  value={userForm.password}
                  onChange={onInputChange}
                />
                <p className="text-danger">{errors?.password}</p>

                <input
                  className="form-control my-3 mx-5 w-75"
                  placeholder="Confirm password"
                  type="password"
                  name="confirmPassword"
                  value={confirmPassword}
                  onChange={onConfirmPasswordChange}
                />
                {!passwordMatch && (
                  <p className="text-danger">Las contraseñas no coinciden.</p>
                )}
              </>
            )}

            <input
              className="form-control my-3 mx-5 w-75"
              placeholder="Email"
              name="email"
              value={userForm.email}
              onChange={onInputChange}
            />
            <p className="text-danger">{errors?.email}</p>

            <select
              id="faculty"
              name="faculty"
              value={selectedFaculty ? selectedFaculty.id : ""}
              onChange={onInputChange}
              className="form-select my-3 mx-5 w-75"
              aria-label="Facultad"
            >
              <option value="">Select a faculty</option>
              {faculties.map((faculty) => (
                <option key={faculty.id} value={faculty.id}>
                  {faculty.nameFaculty}
                </option>
              ))}
            </select>

            <p className="text-danger">{validatefaculty}</p>

            <div className="my-3 mx-5 form-check form-switch">
              <input
                className="form-check-input"
                name="admin"
                type="checkbox"
                role="switch"
                id="admin"
                checked={userForm.admin}
                onChange={onCheckboxAdminChange}
              />
              <label className="form-check-label" htmlFor="admin">
                Administrador
              </label>
            </div>
            <div className="my-3 mx-5 form-check form-switch">
              <input
                className="form-check-input"
                name="guard"
                type="checkbox"
                role="switch"
                id="guard"
                checked={userForm.guard}
                onChange={onCheckboxGuardChange}
              />
              <label className="form-check-label" htmlFor="guard">
                Guarda de seguridad
              </label>
            </div>


            <input type="hidden" name="id" value={userForm.id} />

            <button className="btn btn-primary" type="submit">
              {userForm.id > 0 ? "Editar" : "Crear"}
            </button>
          </form>
        </div>
      </div>
    </div>
  );
};
