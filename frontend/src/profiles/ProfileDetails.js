import React, {useContext} from "react";
import {useParams, useHistory} from "react-router-dom";
import ProfileContext from "../contexts/ProfileContext";
import SkillList from "./SkillList";
import {Avatar, Button, CardContent, CardHeader, Typography} from "@material-ui/core";
import styled from "styled-components/macro";
import MenuAppBar from "../navBar/NavBar";
import makeStyles from "@material-ui/core/styles/makeStyles";
import Card from "@material-ui/core/Card";

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
                <Card className={classes.card} variant="elevation">
                    <CardHeader
                        className={classes.cardHeader}
                        disableTypography={true}
                        title={<Typography className={classes.name}>{profile.name}</Typography>}
                        subheader={<div><address>Located in: {profile.location}</address>
                            <time>Birthday: {profile.birthday}</time></div>}
                        avatar={profile.imageUrl ? <Avatar className={classes.avatar} src={profile.imageUrl}/> : <Avatar className={classes.avatar}/>}
                        />
                    <CardContent className={classes.cardContent}>
                        <SkillList skills={profile.skills}/>
                        <Typography>Recent projects:</Typography>
                        <Typography>
                            {profile.projects?.map(project =>
                            <p key={project.id}>
                                <Button
                                    className={classes.goToButton}
                                    onClick={() => history.push(`/project/${project.id}`)}
                                >Go to</Button> {project.title}
                            </p>)}
                        </Typography>
                    </CardContent>
                </Card>
                <Button className={classes.button} variant="contained" onClick={handleClick}>Back to profiles</Button>
            </ProfileDetailsStyled>
        </>
    );
}

const useStyles = makeStyles({
    button: {
        backgroundColor: "#ec5864",
        color: "white",
        borderRadius: "10px",
        fontSize: "0.8em",
        padding: "8px",
        position: "static"
    },
    goToButton: {
        backgroundColor: "#e7e7e7",
        borderRadius: "10px",
        fontSize: "0.8em",
        padding: "8px",
    },
    name: {
        fontSize: "1.5em"
    },
    card: {
        height: "82vh",
        backgroundColor: "#FFFFFF",
        overflow: "scroll"
    },
    cardHeader: {
        justifySelf: "center",
        padding: "12px",
        backgroundColor: "#F3EED9"
    },
    cardContent: {
        padding: "12px"
    },
    avatar: {
        width: "130px",
        height: "130px"
    },
    avatarPosition: {
        alignItems: "end",
        justifyItems: "center"
    }
});

const ProfileDetailsStyled = styled.section`
  display: grid;
  grid-template-rows: 1fr min-content;
  padding: var(--size-m);
`