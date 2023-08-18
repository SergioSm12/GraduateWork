import React, { useEffect, useState } from "react";
import { UserForm } from "../components/users/UserForm";
import { useUsers } from "../hooks/useUsers";
import { useParams } from "react-router-dom";

export const RegisterPage = () => {
  const { initialUserForm, users = [] } = useUsers();
  const [userSelected, setUserSelected] = useState(initialUserForm);

  const { id } = useParams();

  useEffect(() => {
    if (id) {
      const user = users.find((u) => u.id == id) || initialUserForm;
      setUserSelected(user);
    }
  }, [id]);
  return (
    <div className="container my-4">
      <h4>{userSelected.id > 0 ? "Editar" : "Registrar"} Usuario</h4>
      <div className="row">
        <div className="col">
          <UserForm userSelected={userSelected} />
        </div>
      </div>
    </div>
  );
};
