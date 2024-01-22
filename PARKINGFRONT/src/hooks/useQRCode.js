import { useDispatch, useSelector } from "react-redux";
import {
  generateQRCodeImage,
  generateQRCodeVisitorImage,
} from "../services/QRService";
import {
  setQRCodeImage,
  setQRCodeImageVisitor,
} from "../store/slices/receipt/qrCodeSlice";
export const useQRCode = () => {
  const { qrCodeImage, qrCodeImageVisitor } = useSelector(
    (state) => state.qrCode
  );
  const dispatch = useDispatch();

  const getQRCodeUsers = async (id) => {
    try {
      const imageUrl = await generateQRCodeImage(id);
      dispatch(setQRCodeImage(imageUrl));
    } catch (error) {
      console.log("Error en el fetching de la imagen : ", error);
    }
  };

  const getQRCodeVisitors = async (id) => {
    try {
      const imageUrl = await generateQRCodeVisitorImage(id);
      dispatch(setQRCodeImageVisitor(imageUrl));
    } catch (error) {
      console.log("Error en el fetching de la imagen : ", error);
    }
  };

  return {
    qrCodeImage,
    getQRCodeUsers,
    qrCodeImageVisitor,
    getQRCodeVisitors,
  };
};
