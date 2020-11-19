import React from "react";
import { Route, Switch } from "react-router-dom";
import LoginPage from "./loginPage/LoginPage";
import Success from "./loginPage/Success";
import UserContextProvider from "./contexts/UserContextProvider";
import ProfileContextProvider from "./contexts/ProfileContextProvider";

function App() {
  return (
      <UserContextProvider>
          <ProfileContextProvider>
            <Switch>
                <Route path="/login" component={LoginPage}/>
                <Route path="/success" component={Success}/>
            </Switch>
          </ProfileContextProvider>
      </UserContextProvider>
  );
}

export default App;
