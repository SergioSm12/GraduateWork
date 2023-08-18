import React from "react";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faEye, faPencil, faTrashCan } from "@fortawesome/free-solid-svg-icons";
import { useUsers } from "../../hooks/useUsers";
import { NavLink } from "react-router-dom";

export const UserRow = ({
  id,
  name,
  lastName,
  email,
  faculty,
  admin,
  guard,
  vehicles
}) => {
  const { handlerUserSelectedModal, handlerRemoveUser } = useUsers();
  return (
    <tr>
      <td>{id}</td>
      <td>
        <button
          className="btn btn-outline-info"
          type="button"
          onClick={() => {
            handlerUserSelectedModal({
              id: id,
              name: name,
              lastName: lastName,
              email: email,
              faculty: faculty,
              admin: admin,
              guard: guard,
              vehicles:vehicles,
            });
          }}
        >
          <FontAwesomeIcon icon={faEye} />
        </button>
      </td>
      <td>{name}</td>
      <td>{lastName}</td>
      <td>{email}</td>
      <td>
        <NavLink className="btn btn-outline-primary" to={"/users/edit/" + id}>
          <FontAwesomeIcon icon={faPencil} />
        </NavLink>
      </td>
      <td>
        <button
          type="button"
          className="btn btn-outline-danger"
          onClick={() => handlerRemoveUser(id)}
        >
          <FontAwesomeIcon icon={faTrashCan} />
        </button>
      </td>
    </tr>
  );
};
