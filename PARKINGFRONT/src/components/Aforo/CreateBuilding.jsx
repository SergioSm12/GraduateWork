import React, { useEffect, useState } from "react";
import { useBuildings } from "../../hooks/useBuildings";
import { RiBuildingLine, RiCarLine, RiMotorbikeLine } from "react-icons/ri";

export const CreateBuilding = ({ buildingSelected }) => {
  const { initialBuildingForm, handlerAddBuilding, errorsBuilding } =
    useBuildings();
  const [formBuilding, setformBuilding] = useState(initialBuildingForm);

  useEffect(() => {
    if (buildingSelected) {
      setformBuilding({
        ...buildingSelected,
      });
    }
  }, [buildingSelected]);

  const onInputChange = ({ target }) => {
    const { name, value } = target;
    setformBuilding({
      ...formBuilding,
      [name]: value,
    });
  };

  const handlerCancelEdit = () => {
    setformBuilding(initialBuildingForm);
  };

  const onSubmit = (e) => {
    e.preventDefault();
    handlerAddBuilding(formBuilding);
    setformBuilding(initialBuildingForm);
  };

  return (
    <div className="bg-secondary-900 p-8 rounded-xl shadow-2xl w-auto lg:w-[450px]">
      <div className="flex items-start justify-between">
        <h1 className=" text-2xl uppercase font-bold tracking-[5px] text-white mb-8">
          {formBuilding.id > 0 ? "Editar" : "Crear"}{" "}
          <span className="text-primary">edificio</span>
        </h1>
      </div>
      <form className="mb-8" onSubmit={onSubmit}>
        <div className="relative ">
          <RiBuildingLine className="absolute top-1/2 -translate-y-1/2 left-2 text-primary" />
          <input
            type="text"
            className="py-3 pl-8 pr-4 bg-secondary-100 w-full outline-none rounded-lg focus:border focus:border-primary"
            placeholder="Edificio"
            name="name"
            value={formBuilding.name}
            onChange={onInputChange}
          />
        </div>
        <div className="relative mb-4">
          <p className="text-red-500">{errorsBuilding.name}</p>
        </div>
        <div>
          <div className="flex items-center justify-center gap-2">
            <button
              type="submit"
              className="bg-primary/80 hover:bg-primary text-black uppercase font-bold text-sm w-full py-3 px-4 rounded-lg "
            >
              {formBuilding.id > 0 ? "Editar" : "Crear"}
            </button>

            {formBuilding.id > 0 ? (
              <button
                type="button"
                className=" py-3 px-4 text-red-600 hover:text-black bg-secondary-100/80  hover:bg-red-600 rounded-lg  transition-colors"
                onClick={() => handlerCancelEdit()}
              >
                Cancelar
              </button>
            ) : (
              ""
            )}
          </div>
        </div>
      </form>
    </div>
  );
};
