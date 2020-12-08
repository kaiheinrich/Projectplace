import React, {useContext, useState} from "react";
import ProfileContext from "../contexts/ProfileContext";
import ProfileOverviewItem from "./ProfileOverviewItem";
import styled from "styled-components/macro";
import MenuAppBar from "../navBar/NavBar";
import AddProjectButton from "../addProjectButton/AddProjectButton";
import InputBase from "@material-ui/core/InputBase";
import {makeStyles} from "@material-ui/core/styles";

export default function ProfileOverview() {

    const classes = useStyles();
    const {profiles} = useContext(ProfileContext);
    const [searchTerm, setSearchTerm] = useState("");

    return(
        <>
            <div>
            <MenuAppBar pagename="Profiles"/>
            <InputBase
                className={classes.searchField}
                type="text"
                placeholder="Search for skills..."
                value={searchTerm}
                onChange={event => setSearchTerm(event.target.value)}/>
            </div>
            <ListStyled>
                {searchTerm ? profiles?.filter(profile =>
                    profile.skills.some(element => element.includes(searchTerm))).map(profile =>
                    <li key={profile.username}>
                        <ProfileOverviewItem profile={profile}/>
                    </li>)
                    : profiles?.map(profile =>
                <li key={profile.username}>
                    <ProfileOverviewItem profile={profile}/>
                </li>
                )}
            </ListStyled>
            <AddProjectButton/>
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

const useStyles = makeStyles((theme) => ({
    searchField: {
        display: "block",
        backgroundColor: "white",
        padding: "2px 4px"
    }
}));