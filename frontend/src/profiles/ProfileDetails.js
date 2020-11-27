import React, {useContext} from "react";
import {useParams, useHistory} from "react-router-dom";
import ProfileContext from "../contexts/ProfileContext";
import SkillList from "./SkillList";
import {Button} from "@material-ui/core";
import styled from "styled-components/macro";
import MenuAppBar from "../navBar/NavBar";

export default function ProfileDetails(){

    const {username} = useParams();
    const {profiles} = useContext(ProfileContext);
    const profile = profiles?.find(profile => profile.username === username);
    const history = useHistory();

    const handleClick = () =>
        history.goBack();

    return (
        !profile ? null :
        <>
            <MenuAppBar pagename="Profile details"/>
            <ProfileDetailsStyled>
                <div>
                    <h1>{profile.name}</h1>
                    <h3>{profile.location}</h3>
                    <h4>{profile.birthday}</h4>
                    <SkillList skills={profile.skills}/>
                </div>
                <Button className="button" variant="contained" onClick={handleClick} color="secondary">Back to profiles</Button>
            </ProfileDetailsStyled>
        </>
    );
}

const ProfileDetailsStyled = styled.section`
  display: grid;
  overflow: scroll;
  grid-template-rows: 1fr min-content;
  padding: var(--size-m);
`