import React, { useEffect } from "react";
import { useUsers } from "../hooks/useUsers";
import { UsersList } from "../components/users/UsersList";
import { NavLink, useParams } from "react-router-dom";
import { Paginator } from "../components/users/paginator";
import { UserModalShow } from "../components/users/UserModalShow";

export const UsersPage = () => {
  const { page } = useParams();
  const { users, getUsers, paginator, visibleForm } = useUsers();

  useEffect(() => {
    getUsers(page);
  }, [, page]);
  return (
    <>
      {!visibleForm || <UserModalShow />}

      <div className="container my-4">
        <div className="card border-dark mb-3">
          <div className="card-header">Administrar Usuarios</div>
          <div className="card-body">
            <h5 className="card-title">Listado de Usuarios</h5>
            <div className="table-responsive">
              <button
                className="btn btn-outline-primary btn-sm mb-3"
                type="button"
              >
                <NavLink className="nav-link" to="/users/register">
                  Crear
                </NavLink>
              </button>
              {users.length === 0 ? (
                <div className="alert alert-info">
                  <span>No hay registros en la base de datos</span>
                </div>
              ) : (
                <>
                  <UsersList />
                  <Paginator url="/users/page" paginator={paginator} />
                </>
              )}
            </div>
          </div>
        </div>
      </div>
    </>
  );
};
