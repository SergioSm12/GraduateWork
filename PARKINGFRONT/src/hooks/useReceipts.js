import { useDispatch, useSelector } from "react-redux";
import { findReceiptsByUser } from "../services/receiptService";
import {
  loadingReceiptsByUser,
  onCloseShowModalReceipt,
  onOpenModalShowReceipt,
  onReceiptShowModalSelected,
  initialReceiptForm
} from "../store/slices/receipt/receiptSlice";

export const useReceipts = () => {
  const { receiptsByUser, visibleShowReceiptModal, receiptSelected } = useSelector(
    (state) => state.receipts
  );
  const dispatch = useDispatch();

  const getReciptsByUser = async (id) => {
    try {
      const result = await findReceiptsByUser(id);
      dispatch(loadingReceiptsByUser(result.data));
    } catch (error) {
      throw error;
    }
  };

  const handlerReceiptSelectedModalShow = (receipt) => {
    dispatch(onReceiptShowModalSelected({ ...receipt }));
  };

  const handlerOpenModalShowReceipt = () => {
    dispatch(onOpenModalShowReceipt());
  };

  const handlerCloseModalShowReceipt = () => {
    dispatch(onCloseShowModalReceipt());
  };

  return {
    getReciptsByUser,
    receiptsByUser,
    handlerOpenModalShowReceipt,
    handlerReceiptSelectedModalShow,
    handlerCloseModalShowReceipt,
    visibleShowReceiptModal,
    receiptSelected,
    initialReceiptForm
  };
};
