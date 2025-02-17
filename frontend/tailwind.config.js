/*
 *  Copyright 2024-2025 Dataport. All rights reserved. Developed as part of the POSSIBLE project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

/** @type {import('tailwindcss').Config} */
module.exports = {
  darkMode: "class",
  content: [
    "./src/**/*.{html,ts}",
  ],
  safelist: [
    {
      pattern: /(bg|text)-(brand)*./, // exclude colors from tailwind tree shaking
    },
  ],
  theme: {
    extend: {
      colors: {
        Black: "#000000",
        White: "#ffffff",
        success: "#10B15C",
        error: "#DC2626",
        brand: {
          50: "#eff1f3",
          100: "#c9cfd9",
          200: "#969faf",
          300: "#788194",
          400: "#616c82",
          500: "#4a5770",
          600: "#34425e",
          700: "#1e2e4d",
          800: "#162341",
          900: "#08152f",
        },
        "brand-edu": {
          50: "#fefce8",
          100: "#fef9c3",
          200: "#fef08a",
          300: "#fde047",
          400: "#facc15",
          500: "#e6a31f",
          600: "#ca8a04",
          700: "#a16207",
          800: "#854d0e",
          900: "#713f12",
        },
        "brand-sme": {
          50: "#e7f0f3",
          100: "#d0e1e7",
          200: "#a2c3cf",
          300: "#73a6b8",
          400: "#4588a0",
          500: "#176b89",
          600: "#005270",
          700: "#0d3f5a",
          800: "#0c2e4f",
          900: "#001f3d",
        },
        "brand-public": {
          50: "#f2f3f9",
          100: "#dadced",
          200: "#ced0e7",
          300: "#b6b9db",
          400: "#9ea2cf",
          500: "#868bc4",
          600: "#6b6f9c",
          700: "#5d6189",
          800: "#505375",
          900: "#434562",
        },
      },
      fontSize: {
        xs: "0.75rem",
        sm: "0.875rem",
        base: "1rem",
        lg: "1.125rem",
        xl: "1.25rem",
        "2xl": "1.5rem",
        "3xl": "1.875rem",
        "4xl": "2.25rem",
        "5xl": "3rem",
        "6xl": "4rem",
      },
      fontFamily: {
        "acumin-pro": "Acumin Pro",
      },
      borderRadius: {
        none: "0",
        xs: "0.25rem",
        sm: "0.3125rem",
        default: "0.5rem",
        lg: "2rem",
        xl: "2.5rem",
        "2xl": "3rem",
        "3xl": "4rem",
        full: "9999px",
      },
    },
    container: {
      center: true,
    },
    screens: {
      xs: "375px",
      sm: "568px",
      md: "768px",
      lg: "1024px",
      xl: "1440px",
      "2xl": "1920px",
      "3xl": "2560px",
      "4xl": "3840px"
    },
  },
  plugins: [],
  future: {
    hoverOnlyWhenSupported: true,
  },
}

