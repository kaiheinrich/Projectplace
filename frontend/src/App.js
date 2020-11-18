import React from "react";
import { Route, Switch } from "react-router-dom";
import LoginPage from "./loginPage/LoginPage";
import Success from "./loginPage/Success";
import UserContextProvider from "./contexts/UserContextProvider";

function App() {
  return (
      <UserContextProvider>
        <div>
        <Switch>
            <Route path="/login" component={LoginPage}/>
            <Route path="/success" component={Success}/>
        </Switch>
        </div>
      </UserContextProvider>
  );
}

export default App;
