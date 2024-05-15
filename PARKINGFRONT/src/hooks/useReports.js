import { useDispatch, useSelector } from "react-redux";
import {
  currentMonthlyNightlyReceiptReportSpecific,
  currentMonthlyReceiptReportSpecific,
  currentMonthlyUnifiedReceiptReportService,
  currentMonthlyUnifiedReceiptReportSpecific,
  currentMonthlyVisitorReceiptReportSpecific,
  getCurrentBiweeklyReportNightlyReceipt,
  getCurrentBiweeklyReportReceipt,
  getCurrentBiweeklyReportUnifiedReceipt,
  getCurrentBiweeklyReportVisitorReceipt,
  getCurrentMonthlyReportNightlyReceipt,
  getCurrentMonthlyReportReceipt,
  getCurrentMonthlyReportVisitorReceipt,
  getCurrentWeeklyReportNightlyReceipt,
  getCurrentWeeklyReportReceipt,
  getCurrentWeeklyReportUnifiedReceipt,
  getCurrentWeeklyReportVisitorReceipt,
} from "../services/reportService";
import {
  loadingCurrentbiweeklyReportNightlyReceipt,
  loadingCurrentbiweeklyReportReceipt,
  loadingCurrentbiweeklyReportUnifiedReceipt,
  loadingCurrentbiweeklyReportVisitorReceipt,
  loadingCurrentMonthlyReportNightlyReceipt,
  loadingCurrentMonthlyReportReceipt,
  loadingCurrentMonthlyReportUnifiedReceipt,
  loadingCurrentMonthlyReportVisitorReceipt,
  loadingCurrentWeeklyReportNightlyReceipt,
  loadingCurrentWeeklyReportReceipt,
  loadingCurrentWeeklyReportUnifiedReceipt,
  loadingCurrentWeeklyReportVisitorReceipt,
} from "../store/slices/report/reportSlice";
import { useAuth } from "../auth/hooks/useAuth";
import Swal from "sweetalert2";

export const useReports = () => {
  const {
    currentMonthlyReceiptReport,
    currentWeeklyReceiptReport,
    currentBiweeklyReceiptReport,

    currentMonthlyNightlyReceiptReport,
    currentWeeklyNightlyReceiptReport,
    currentBiweeklyNightlyReceiptReport,

    currentMonthlyVisitorReceiptReport,
    currentWeeklyVisitorReceiptReport,
    currentBiweeklyVisitorReceiptReport,

    currentMonthlyUnifiedReceiptReport,
    currentWeeklyUnifiedReceiptReport,
    currentBiweeklyUnifiedReceiptReport,
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

  //nightly
  const getCurrentMonthlyNightlyReceiptReport = async () => {
    try {
      const result = await getCurrentMonthlyReportNightlyReceipt();
      dispatch(loadingCurrentMonthlyReportNightlyReceipt(result.data));
    } catch (error) {
      if (error.response.status == 401) {
        handlerLogout();
      } else {
        throw error;
      }
    }
  };

  const getCurrentMonthlyNightlyReceiptReportSpecific = async ({
    year,
    month,
  }) => {
    try {
      const response = await currentMonthlyNightlyReceiptReportSpecific(
        year,
        month
      );

      dispatch(loadingCurrentMonthlyReportNightlyReceipt(response.data));

      Toast.fire({
        icon: "success",
        title: `Reporte generado recibo nocturno, mes : ${month}`,
      });
    } catch (error) {
      if (error.response.status == 401) {
        handlerLogout();
      } else {
        throw error;
      }
    }
  };

  const getCurrentBiweeklyNightlyReceiptReport = async () => {
    try {
      const result = await getCurrentBiweeklyReportNightlyReceipt();
      dispatch(loadingCurrentbiweeklyReportNightlyReceipt(result.data));
    } catch (error) {
      if (error.response.status == 401) {
        handlerLogout();
      } else {
        throw error;
      }
    }
  };

  const getCurrentWeeklyNightlyReceiptReport = async () => {
    try {
      const result = await getCurrentWeeklyReportNightlyReceipt();
      dispatch(loadingCurrentWeeklyReportNightlyReceipt(result.data));
    } catch (error) {
      if (error.response.status == 401) {
        handlerLogout();
      } else {
        throw error;
      }
    }
  };

  //Visitor

  const getCurrentMonthlyVisitorReceiptReport = async () => {
    try {
      const result = await getCurrentMonthlyReportVisitorReceipt();
      dispatch(loadingCurrentMonthlyReportVisitorReceipt(result.data));
    } catch (error) {
      if (error.response.status == 401) {
        handlerLogout();
      } else {
        throw error;
      }
    }
  };

  const getCurrentMonthlyVisitorReceiptReportSpecific = async ({
    year,
    month,
  }) => {
    try {
      const response = await currentMonthlyVisitorReceiptReportSpecific(
        year,
        month
      );

      dispatch(loadingCurrentMonthlyReportVisitorReceipt(response.data));

      Toast.fire({
        icon: "success",
        title: `Reporte generado visitantes mes : ${month}`,
      });
    } catch (error) {
      if (error.response.status == 401) {
        handlerLogout();
      } else {
        throw error;
      }
    }
  };

  const getCurrentBiweeklyVisitorReceiptReport = async () => {
    try {
      const result = await getCurrentBiweeklyReportVisitorReceipt();
      dispatch(loadingCurrentbiweeklyReportVisitorReceipt(result.data));
    } catch (error) {
      if (error.response.status == 401) {
        handlerLogout();
      } else {
        throw error;
      }
    }
  };

  const getCurrentWeeklyVisitorReceiptReport = async () => {
    try {
      const result = await getCurrentWeeklyReportVisitorReceipt();
      dispatch(loadingCurrentWeeklyReportVisitorReceipt(result.data));
    } catch (error) {
      if (error.response.status == 401) {
        handlerLogout();
      } else {
        throw error;
      }
    }
  };

  //unified
  const getCurrentMonthlyUnifiedReceiptReport = async () => {
    try {
      const result = await currentMonthlyUnifiedReceiptReportService();
      dispatch(loadingCurrentMonthlyReportUnifiedReceipt(result.data));
    } catch (error) {
      if (error.response.status == 401) {
        handlerLogout();
      } else {
        throw error;
      }
    }
  };

  const getCurrentMonthlyUnifiedReceiptReportSpecific = async ({
    year,
    month,
  }) => {
    try {
      const response = await currentMonthlyUnifiedReceiptReportSpecific(
        year,
        month
      );

      dispatch(loadingCurrentMonthlyReportUnifiedReceipt(response.data));

      Toast.fire({
        icon: "success",
        title: `Reporte unificado generado mes : ${month}`,
      });
    } catch (error) {
      if (error.response.status == 401) {
        handlerLogout();
      } else {
        throw error;
      }
    }
  };

  const getCurrentBiweeklyUnifiedReceiptReport = async () => {
    try {
      const result = await getCurrentBiweeklyReportUnifiedReceipt();
      dispatch(loadingCurrentbiweeklyReportUnifiedReceipt(result.data));
    } catch (error) {
      if (error.response.status == 401) {
        handlerLogout();
      } else {
        throw error;
      }
    }
  };

  const getCurrentWeeklyUnifiedReceiptReport = async () => {
    try {
      const result = await getCurrentWeeklyReportUnifiedReceipt();
      dispatch(loadingCurrentWeeklyReportUnifiedReceipt(result.data));
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

    currentMonthlyNightlyReceiptReport,
    currentWeeklyNightlyReceiptReport,
    currentBiweeklyNightlyReceiptReport,
    getCurrentMonthlyNightlyReceiptReport,
    getCurrentMonthlyNightlyReceiptReportSpecific,
    getCurrentBiweeklyNightlyReceiptReport,
    getCurrentWeeklyNightlyReceiptReport,

    currentMonthlyVisitorReceiptReport,
    currentWeeklyVisitorReceiptReport,
    currentBiweeklyVisitorReceiptReport,
    getCurrentMonthlyVisitorReceiptReport,
    getCurrentMonthlyVisitorReceiptReportSpecific,
    getCurrentBiweeklyVisitorReceiptReport,
    getCurrentWeeklyVisitorReceiptReport,

    currentMonthlyUnifiedReceiptReport,
    currentWeeklyUnifiedReceiptReport,
    currentBiweeklyUnifiedReceiptReport,
    getCurrentMonthlyUnifiedReceiptReportSpecific,
    getCurrentBiweeklyUnifiedReceiptReport,
    getCurrentWeeklyUnifiedReceiptReport,
    getCurrentMonthlyUnifiedReceiptReport,
  };
};
