import parkingApi from "../apis/parkingApi";

const BASE_URL_QR = "/qrcode";

export const generateQRCodeImage = async (id) => {
  try {
    const response = await parkingApi.get(`${BASE_URL_QR}/${id}`, {
      responseType: "arraybuffer",
    });
    const imageBlob = new Blob([response.data], { type: "image/png" });
    const imageUrl = URL.createObjectURL(imageBlob);
    return imageUrl;
  } catch (error) {
    throw error;
  }
};

export const generateQRCodeNightlyImage = async (id) => {
  try {
    const response = await parkingApi.get(
      `${BASE_URL_QR}/nightly-receipt/${id}`,
      {
        responseType: "arraybuffer",
      }
    );
    const imageBlob = new Blob([response.data], { type: "image/png" });
    const imageUrl = URL.createObjectURL(imageBlob);
    return imageUrl;
  } catch (error) {
    throw error;
  }
};

export const generateQRCodeVisitorImage = async (id) => {
  try {
    const response = await parkingApi.get(`${BASE_URL_QR}/visitor/${id}`, {
      responseType: "arraybuffer",
    });
    const imageBlob = new Blob([response.data], { type: "image/png" });
    const imageUrl = URL.createObjectURL(imageBlob);
    return imageUrl;
  } catch (error) {
    throw error;
  }
};
