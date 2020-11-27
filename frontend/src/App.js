import React from "react";
import { Route, Switch} from "react-router-dom";
import LoginPage from "./loginPage/LoginPage";
import Success from "./loginPage/Success";
import UserContextProvider from "./contexts/UserContextProvider";
import ProfileContextProvider from "./contexts/ProfileContextProvider";
import ProfileOverview from "./profiles/ProfileOverview";
import ProtectedRoute from "./routing/ProtectedRoute";
import styled from "styled-components/macro";
import ProfileDetails from "./profiles/ProfileDetails";
import EditProfile from "./profiles/EditProfile";
import ProjectOverview from "./projects/ProjectOverview";
import ProjectContextProvider from "./contexts/ProjectContextProvider";

function App() {
  return (
      <UserContextProvider>
          <ProfileContextProvider>
              <ProjectContextProvider>
                  <AppStyled>
                    <Switch>
                        <Route path="/login" component={LoginPage}/>
                        <ProtectedRoute path="/success" component={Success}/>
                        <ProtectedRoute exact path="/profile" component={ProfileOverview}/>
                        <ProtectedRoute exact path="/profile/:username" component={ProfileDetails}/>
                        <ProtectedRoute path="/profile/:username/edit" component={EditProfile}/>
                        <ProtectedRoute exact path="/project" component={ProjectOverview}/>
                    </Switch>
                  </AppStyled>
              </ProjectContextProvider>
          </ProfileContextProvider>
      </UserContextProvider>
  );
}

export default App;

const AppStyled = styled.div`
  display: grid;
  grid-template-rows: min-content 1fr;
  height: 100vh;
  background-color: var(--greyish-yellow);
  overflow: scroll;
`;
