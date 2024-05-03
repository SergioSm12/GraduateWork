import { useDispatch, useSelector } from "react-redux";
import {
  generateQRCodeImage,
  generateQRCodeNightlyImage,
  generateQRCodeVisitorImage,
} from "../services/QRService";
import {
  setQRCodeImage,
  setQRCodeImageNightly,
  setQRCodeImageVisitor,
} from "../store/slices/receipt/qrCodeSlice";
export const useQRCode = () => {
  const { qrCodeImage, qrCodeImageVisitor, qrcodeImageNightly } = useSelector(
    (state) => state.qrCode
  );
  const dispatch = useDispatch();

  const getQRCodeUsers = async (id) => {
    try {
      const imageUrl = await generateQRCodeImage(id);
      dispatch(setQRCodeImage(imageUrl));
      return imageUrl;
    } catch (error) {
      console.log("Error en el fetching de la imagen : ", error);
    }
  };

  const getQRCodeNightly = async (id) => {
    try {
      const imageUrl = await generateQRCodeNightlyImage(id);
      dispatch(setQRCodeImageNightly(imageUrl));
      return imageUrl;
    } catch (error) {
      console.log("Error en el fetching de la imagen : ", error);
    }
  };

  const getQRCodeVisitors = async (id) => {
    try {
      const imageUrl = await generateQRCodeVisitorImage(id);
      dispatch(setQRCodeImageVisitor(imageUrl));
      return imageUrl;
    } catch (error) {
      console.log("Error en el fetching de la imagen : ", error);
    }
  };

  return {
    qrCodeImage,
    getQRCodeUsers,
    qrCodeImageVisitor,
    getQRCodeVisitors,
    qrcodeImageNightly,
    getQRCodeNightly,
  };
};
