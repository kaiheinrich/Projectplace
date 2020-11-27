import React, {useContext} from "react";
import ProfileContext from "../contexts/ProfileContext";
import ProfileOverviewItem from "./ProfileOverviewItem";
import styled from "styled-components/macro";
import MenuAppBar from "../navBar/NavBar";

export default function ProfileOverview() {

    const {profiles} = useContext(ProfileContext);

    return(
        <>
            <MenuAppBar pagename="Profiles"/>
            <ListStyled>
                {profiles?.map(profile =>
                <li key={profile.username}>
                    <ProfileOverviewItem profile={profile}/>
                </li>
                )}
            </ListStyled>
        </>
        );
}

const ListStyled = styled.ul`
  display: grid;
  grid-auto-rows: min-content;
  list-style: none;
  margin: var(--size-l);
  gap: var(--size-l);
  padding: 0;
`;