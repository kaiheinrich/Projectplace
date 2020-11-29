import {createGlobalStyle} from "styled-components";

export default createGlobalStyle`

:root {

   --red-dark: #9f0d2f;
   --beige: #FDF1DC;
  
   --blue-dark: #0a7bdd;
   //--blue-medium: #;
   --blue-light: #f2fcff;
   
   --greyish-yellow: #f8efd4;
   --brown: #edc988;
   --red: #d7385e;
   --black: #132743;
  
   --size-xs: 4px;
   --size-s: 8px;
   --size-m: 12px;
   --size-l: 16px;
   --size-xl: 24px;
   --size-xxl: 32px;
  }
  
  * {
    box-sizing: border-box;
  }
  
  html, body {
    margin: 0;
    font-family: "Futura",sans-serif;
  }
  
  body {
    background-color: white;
  }
   
  input, textarea {
    font-size: 1em;
    font-family: inherit;
  }
`;