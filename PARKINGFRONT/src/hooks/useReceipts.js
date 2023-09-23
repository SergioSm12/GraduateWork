import { useDispatch, useSelector } from "react-redux";
import { findReceiptsByUser } from "../services/receiptService";
import { loadingReceiptsByUser } from "../store/slices/receipt/receiptSlice";

export const useReceipts = () => {
  const { receiptsByUser } = useSelector((state) => state.receipts);
  const dispatch = useDispatch();

  const getReciptsByUser = async (id) => {
    try {
      const result = await findReceiptsByUser(id);
      dispatch(loadingReceiptsByUser(result.data));
    } catch (error) {
      throw error;
    }
  };

  return {
    getReciptsByUser,
    receiptsByUser,
  };
};
