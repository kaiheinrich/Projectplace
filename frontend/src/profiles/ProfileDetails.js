import React, {useContext} from "react";
import {useParams, useHistory} from "react-router-dom";
import ProfileContext from "../contexts/ProfileContext";
import SkillList from "./SkillList";
import {Avatar, Box, Button, Typography} from "@material-ui/core";
import styled from "styled-components/macro";
import MenuAppBar from "../navBar/NavBar";
import makeStyles from "@material-ui/core/styles/makeStyles";
import Card from "@material-ui/core/Card";
import AddProjectButton from "../addProjectButton/AddProjectButton";

export default function ProfileDetails(){

    const classes = useStyles();
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
                <Card className={classes.card}>
                    <DivStyled>
                        <div>
                            <h1>{profile.name}</h1>
                            <AddressStyled>Location: {profile.location}</AddressStyled>
                            <BirthdayStyled>Birthday: {profile.birthday}</BirthdayStyled>
                        </div>
                        <Box component={'div'} className={classes.avatarPosition}>
                            {profile.imageUrl && <Avatar className={classes.avatar} src={profile.imageUrl}/>}
                        </Box>
                    </DivStyled>
                    <SkillList skills={profile.skills}/>
                    <Typography>
                        Recent projects:
                        {profile.projects?.map(project =>
                        <p key={project.id}>{project.title}</p>)}
                    </Typography>
                </Card>
                <Button className={classes.button} variant="contained" onClick={handleClick} >Back to profiles</Button>
            </ProfileDetailsStyled>
            <AddProjectButton/>
        </>
    );
}

const useStyles = makeStyles({
    button: {
        backgroundColor: "#ec5864",
        color: "white",
        borderRadius: "10px",
        fontSize: "0.8em",
        padding: "8px"
    },
    card: {
        height: "82vh",
        padding: "12px",
        backgroundColor: "#FFF4F4"
    },
    avatar: {
        width: "130px",
        height: "130px"
    },
    avatarPosition: {
        alignSelf: "center",
        justifySelf: "end"
    }
});

const ProfileDetailsStyled = styled.section`
  display: grid;
  overflow: scroll;
  grid-template-rows: 1fr min-content;
  padding: var(--size-m);
`

const AddressStyled = styled.address`
  margin-bottom: var(--size-s);
`

const BirthdayStyled = styled.time`
  margin-bottom: var(--size-xl);
`

const DivStyled = styled.div`
  display: grid;
  grid-template-columns: 1fr 1fr;
`