import React, {useContext, useEffect, useState} from "react";
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
    const [dayOfBirth, setDayOfBirth] = useState(0);
    const [monthOfBirth, setMonthOfBirth] = useState(0);
    const [yearOfBirth, setYearOfBirth] = useState(0);
    const history = useHistory();

    useEffect(() => {
        const date = new Date(profile?.birthday);
        setDayOfBirth(date.getDate());
        setMonthOfBirth(date.getMonth() + 1);
        setYearOfBirth(date.getFullYear());
    }, [profile])

    const handleClick = () =>
        history.goBack();

    return (
        !profile ? null :
        <>
            <MenuAppBar pagename="Profile details" searchIsActive={false}/>
            <ProfileDetailsStyled>
                <Card className={classes.card} variant="elevation">
                    <CardHeader
                        className={classes.cardHeader}
                        disableTypography={true}
                        title={<Typography className={classes.name}>{profile.name}</Typography>}
                        subheader={<div><address>Located in: {profile.location}</address>
                            <time>Birthday: {`${dayOfBirth}.${monthOfBirth}.${yearOfBirth}`}</time></div>}
                        avatar={<Avatar className={classes.avatar} src={profile.imageUrl}/>}
                        />
                    <CardContent className={classes.cardContent}>
                        <SkillList skills={profile.skills}/>
                        <Typography>Recent projects:</Typography>
                        <Typography>
                            {profile.projects?.slice(0).reverse().map(project =>
                            <p key={project.id}>
                                <Button
                                    className={classes.goToButton}
                                    onClick={() => history.push(`/project/${project.id}`)}
                                >Go to</Button> {project.title}
                            </p>)}
                        </Typography>
                    </CardContent>
                </Card>
                <ButtonSectionStyled>
                <Button className={classes.goBackButton} variant="contained" onClick={handleClick}>Go back</Button>
                <Button className={classes.button} variant="contained" onClick={() => history.push(`/messageto/${profile.username}`)}>Send message</Button>
                </ButtonSectionStyled>
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
        padding: "8px"
    },
    goToButton: {
        backgroundColor: "#e7e7e7",
        borderRadius: "10px",
        fontSize: "0.8em",
        padding: "8px",
    },
    goBackButton: {
        backgroundColor: "#e7e7e7",
        borderRadius: "10px",
        fontSize: "0.8em",
        padding: "8px"
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
    }
});

const ProfileDetailsStyled = styled.section`
  display: grid;
  grid-template-rows: 1fr min-content;
  padding: var(--size-m);
`

const ButtonSectionStyled = styled.section`
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 8px;
`