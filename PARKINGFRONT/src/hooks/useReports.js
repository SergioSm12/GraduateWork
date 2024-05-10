import { useDispatch, useSelector } from "react-redux";
import {
  currentMonthlyReceiptReportSpecific,
  getCurrentBiweeklyReportReceipt,
  getCurrentMonthlyReportReceipt,
  getCurrentWeeklyReportReceipt,
} from "../services/reportService";
import {
  loadingCurrentbiweeklyReportReceipt,
  loadingCurrentMonthlyReportReceipt,
  loadingCurrentWeeklyReportReceipt,
} from "../store/slices/report/reportSlice";
import { useAuth } from "../auth/hooks/useAuth";
import Swal from "sweetalert2";

export const useReports = () => {
  const {
    currentMonthlyReceiptReport,
    currentWeeklyReceiptReport,
    currentBiweeklyReceiptReport,
  } = useSelector((state) => state.reports);

  const dispatch = useDispatch();
  const { login, handlerLogout } = useAuth();

  const Toast = Swal.mixin({
    toast: true,
    position: "top",
    showConfirmButton: false,
    timer: 3000,
    timerProgressBar: true,
    didOpen: (toast) => {
      toast.addEventListener("mouseenter", Swal.stopTimer);
      toast.addEventListener("mouseleave", Swal.resumeTimer);
    },
  });

  const getCurrentMonthlyReceiptReport = async () => {
    try {
      const result = await getCurrentMonthlyReportReceipt();
      dispatch(loadingCurrentMonthlyReportReceipt(result.data));
    } catch (error) {
      if (error.response.status == 401) {
        handlerLogout();
      } else {
        throw error;
      }
    }
  };

  const getCurrentMonthlyReceiptReportSpecific = async ({ year, month }) => {
    try {
      console.log(year, month);
      const response = await currentMonthlyReceiptReportSpecific(year, month);

      dispatch(loadingCurrentMonthlyReportReceipt(response.data));

      Toast.fire({
        icon: "success",
        title: `Reporte generado mes : ${month}`,
      });
    } catch (error) {
      if (error.response.status == 401) {
        handlerLogout();
      } else {
        throw error;
      }
    }
  };

  const getCurrentBiweeklyReceiptReport = async () => {
    try {
      const result = await getCurrentBiweeklyReportReceipt();
      dispatch(loadingCurrentbiweeklyReportReceipt(result.data));
    } catch (error) {
      if (error.response.status == 401) {
        handlerLogout();
      } else {
        throw error;
      }
    }
  };

  const getCurrentWeeklyReceiptReport = async () => {
    try {
      const result = await getCurrentWeeklyReportReceipt();
      dispatch(loadingCurrentWeeklyReportReceipt(result.data));
    } catch (error) {
      if (error.response.status == 401) {
        handlerLogout();
      } else {
        throw error;
      }
    }
  };
  return {
    getCurrentMonthlyReceiptReport,
    getCurrentMonthlyReceiptReportSpecific,
    currentMonthlyReceiptReport,
    getCurrentWeeklyReceiptReport,
    getCurrentBiweeklyReceiptReport,
    currentWeeklyReceiptReport,
    currentBiweeklyReceiptReport,
  };
};
