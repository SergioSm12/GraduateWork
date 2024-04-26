import React, { useEffect } from "react";
import {
  RiCloseCircleLine,
  RiLoaderLine,
  RiRefreshLine,
  RiRestartLine,
} from "react-icons/ri";
import { useReceipts } from "../../hooks/useReceipts";
import { useQRCode } from "../../hooks/useQRCode";
import { useState } from "react";
import { useVisitorReceipt } from "../../hooks/useVisitorReceipt";
import { useNightlyReceipts } from "../../hooks/useNightlyReceipts";

export const QRCode = () => {
  const { handlerCloseModalQRReceipt, idQRReceipt } = useReceipts();
  const { handlerCloseModalQRVisitorReceipt, idQRReceiptVisitor } =
    useVisitorReceipt();
  const { handlerCloseModalQRNightlyReceipt, idQRNightlyReceipt } =
    useNightlyReceipts();
  const {
    qrCodeImage,
    getQRCodeUsers,
    qrCodeImageVisitor,
    getQRCodeVisitors,
    qrcodeImageNightly,
    getQRCodeNightly,
  } = useQRCode();

  const [loading, setLoading] = useState(false);

  //recargar QR
  const reloadQRCodeImage = async () => {
    try {
      setLoading(true);
      if (idQRReceiptVisitor) {
        await getQRCodeVisitors(idQRReceiptVisitor);
      } else if (idQRNightlyReceipt) {
        await getQRCodeNightly(idQRNightlyReceipt);
      } else {
        await getQRCodeUsers(idQRReceipt);
      }
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    const fetchData = async () => {
      if (idQRReceiptVisitor) {
        await getQRCodeVisitors(idQRReceiptVisitor);
      } else if (idQRNightlyReceipt) {
        await getQRCodeNightly(idQRNightlyReceipt);
      } else {
        await getQRCodeUsers(idQRReceipt);
      }
    };

    fetchData();
  }, []);

  return (
    <div className="abrir-modal animacion fadeIn">
      <div className="fixed inset-0 bg-black bg-opacity-30 backdrop-blur-sm flex justify-center items-center transition-opacity duration-300">
        <div className="bg-secondary-900 p-8  rounded-lg ">
          <div className="bg-secondary-100 p-8 rounded-xl shadow-2xl w-auto lg:w-[450px]">
            <div className="flex items-start justify-between">
              <h1 className=" text-2xl uppercase font-bold tracking-[5px] text-white mb-8">
                Codigo <span className="text-primary"> QR </span>
              </h1>
              <button
                className=" py-2 px-2 text-red-600 hover:text-black bg-secondary-900/80  hover:bg-red-600/50 rounded-lg  transition-colors"
                type="button"
                onClick={
                  idQRReceiptVisitor
                    ? () => handlerCloseModalQRVisitorReceipt()
                    : idQRNightlyReceipt
                    ? () => handlerCloseModalQRNightlyReceipt()
                    : () => handlerCloseModalQRReceipt()
                }
              >
                <RiCloseCircleLine className="text-2xl " />
              </button>
            </div>
            <div>
              {qrCodeImage || qrCodeImageVisitor || qrcodeImageNightly ? (
                <div>
                  <img
                    className="mb-2"
                    src={
                      idQRReceipt == null
                        ? qrCodeImageVisitor || qrcodeImageNightly
                        : qrCodeImage
                    }
                    alt="QR Code"
                  />
                  <div className=" text-center">
                    <button
                      className="items-center gap-2 p-2 rounded-lg text-black bg-primary"
                      onClick={reloadQRCodeImage}
                      disabled={loading}
                    >
                      <RiRefreshLine />
                    </button>
                  </div>
                </div>
              ) : (
                <div className="text-center p-2 bg-secondary-900 rounded-lg">
                  {loading ? (
                    <div className="flex items-center gap-2 justify-center">
                      <RiRestartLine className="text-primary" />
                      <span>Recargando QR ...</span>
                    </div>
                  ) : (
                    <div className="flex items-center gap-2 justify-center">
                      <RiLoaderLine className="text-primary" />
                      <span>Cargando QR ...</span>
                    </div>
                  )}
                </div>
              )}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};
