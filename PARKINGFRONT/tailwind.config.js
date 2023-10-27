/** @type {import('tailwindcss').Config} */
export default {
  content: ["./index.html", "./src/**/*.{js,ts,jsx,tsx}"],
  theme: {
    extend: {
      colors: {
        primary: "#14dbff",
        secondary: {
          100: "#1E293C",
          900: "#0F172A",
        },
      },
    },
  },
  plugins: [
    //Configuracion para el selected del tab en hadles
    require("@headlessui/tailwindcss"),
  ],
};
