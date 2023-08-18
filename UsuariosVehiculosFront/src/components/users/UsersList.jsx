import React from "react";
import { useUsers } from "../../hooks/useUsers";
import { UserRow } from "./UserRow";

export const UsersList = () => {
  const { users } = useUsers();

  return (
    <>
      <table className="table table-striped table-dark">
        <thead>
          <tr>
            <th>#</th>
            <th>Show</th>
            <th>Name</th>
            <th>last Name</th>
            <th>Email</th>
            <th>Edit</th>
            <th>Delete</th>
          </tr>
        </thead>
        <tbody>
          {users.map(({ id, name, lastName, email, faculty,admin, guard, vehicles}) => (
            <UserRow
              key={id}
              id={id}
              name={name}
              lastName={lastName}
              email={email}
              faculty={faculty}
              admin={admin}
              guard={guard}
              vehicles={vehicles}
            />
          ))}
        </tbody>
      </table>
    </>
  );
};
