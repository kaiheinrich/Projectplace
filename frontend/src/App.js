import React from "react";
import { Route, Switch } from "react-router-dom";
import LoginPage from "./components/loginPage/LoginPage";

function App() {
  return (
    <Switch>
        <Route path="/login" component={LoginPage}/>
    </Switch>
  );
}

export default App;
