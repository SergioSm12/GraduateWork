import React, { useEffect, useState } from "react";
import { useUsers } from "../../hooks/useUsers";
import { faEye } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { RiEdit2Fill,RiDeleteBin5Fill,RiPoliceCarFill } from "react-icons/ri";
import "./styles/UserModal.css";
export const UserModalShow = () => {
  const { initialUserForm, userSelected, handlerCloseModal } = useUsers();
  const [userModal, setUserModal] = useState(initialUserForm);

  useEffect(() => {
    setUserModal({
      ...userSelected,
      password: "",
    });
  }, [userSelected]);

  const onCloseModal = () => {
    handlerCloseModal();
    setUserModal(initialUserForm);
  };

  return (
    <>
      <div className="abrir-modal animacion fadeIn">
        <div className="modal" tabIndex="-1" style={{ display: "block" }}>
          <div className="modal-dialog modal-lg">
            <div className="modal-content">
              <div className="modal-header">
                <h5 className="modal-title">
                  Show <FontAwesomeIcon icon={faEye} />
                </h5>
                <>
                  {userModal.admin == true && userModal.guard == true ? (
                    <span className="mx-4 badge text-bg-warning">
                      Administrador
                    </span>
                  ) : userModal.admin == true ? (
                    <span className="mx-4 badge text-bg-warning">
                      Administrador
                    </span>
                  ) : userModal.guard == true ? (
                    <span className="mx-4 badge text-bg-danger">
                      Guarda de seguridad
                    </span>
                  ) : (
                    <span className="mx-4 badge text-bg-info">Usuario</span>
                  )}
                </>
                <button
                  onClick={() => onCloseModal()}
                  type="button"
                  className="btn-close"
                  data-bs-dismiss="modal"
                  aria-label="Close"
                ></button>
              </div>
              <div className="modal-body">
                <div className="container">
                  <div className="row">
                    <div className="col-sm">
                      <ul className="list-group text-dark mb-1">
                        <li className="list-group-item active bg-black">
                          {userModal.name} {userModal.lastName}
                        </li>
                        <li className="list-group-item">{userModal.email}</li>
                        <li className="list-group-item">
                          {userModal.faculty.nameFaculty}
                        </li>
                      </ul>
                    </div>
                  </div>
                  <div className="row">
                    <div className="col-sm-6">
                      {userModal.vehicles.length > 0 ? (
                        <>
                        <h3>vehicles : </h3>
                          <table className="table">
                            <thead>
                              <tr>
                                <th>Plate</th>
                                <th>Vehicle Type</th>
                                <th colSpan={2} className="text-center">options</th>
                              </tr>
                            </thead>
                            <tbody>
                              {userModal.vehicles.map((vehicle) => (
                                <tr key={vehicle.id}>
                                  <td>{vehicle.plate}</td>
                                  <td>{vehicle.vehicleType.name}</td>
                                  <td> <button className="btn btn-info"><RiEdit2Fill/></button></td>
                                  <td><button className="btn btn-danger"><RiDeleteBin5Fill/></button></td>
                                </tr>
                              ))}
                            </tbody>
                          </table>
                        </>
                      ) : (
                        <div className="alert alert-info">
                          <span>No hay vehiculos registrados</span>
                        </div>
                      )}
                      <button className="btn btn-primary">Register vehicle <RiPoliceCarFill/></button>
                    </div>
                  </div>
             
                </div>
              </div>
              <div className="modal-footer">
                <button
                  onClick={() => onCloseModal()}
                  type="button"
                  className="btn btn-secondary"
                  data-bs-dismiss="modal"
                >
                  close
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </>
  );
};
