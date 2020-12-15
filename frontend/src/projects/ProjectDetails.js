import React, {useContext, useEffect, useState} from "react";
import {useParams, useHistory, Link} from "react-router-dom";
import ProjectContext from "../contexts/ProjectContext";
import styled from "styled-components/macro";
import MenuAppBar from "../navBar/NavBar";
import makeStyles from "@material-ui/core/styles/makeStyles";
import Card from "@material-ui/core/Card";
import {Button, CardContent, CardHeader, Typography} from "@material-ui/core";
import UserContext from "../contexts/UserContext";

export default function ProjectDetails() {

    const classes = useStyles();
    const {id} = useParams();
    const {projects} = useContext(ProjectContext);
    const project = projects?.find(project => project.id === id);
    const [buttonEnabled, setButtonEnabled] = useState(false);
    const {userCredentials} = useContext(UserContext);
    const history = useHistory();

    useEffect(() => {
        project?.projectOwner === userCredentials.sub && setButtonEnabled(true)
    }, [project, userCredentials])

    return(
        !project ? null :
        <>
            <MenuAppBar pagename="Project details" searchIsActive={false}/>
            <ProjectDetailsStyled>
                <Card className={classes.card} variant="elevation">
                    <CardHeader
                        className={classes.cardHeader}
                        title={project.title}
                        subheader={<div>by <Link className={classes.link} to={`/profile/${project.projectOwner}`}>{project.projectOwner}</Link></div>}
                        action={buttonEnabled ? <Button className={classes.editButton} onClick={handleClick}>Edit</Button> : null}/>
                    <CardContent className={classes.cardContent}>
                        <Typography>Description:</Typography>
                        <Typography>{project.description}</Typography>
                    </CardContent>
                    <CardContent className={classes.cardContent}>
                        <img width="100%" alt="project impression" src={project.imageUrl}/>
                    </CardContent>

                </Card>
                <ButtonSectionStyled>
                    <Button className={classes.goBackButton} variant="contained" onClick={handleGoBack}>Go Back</Button>
                    <Button className={classes.button} variant="contained" onClick={() => history.push(`/messageto/${project.projectOwner}`)}>Take part</Button>
                </ButtonSectionStyled>
            </ProjectDetailsStyled>
        </>
    );

    function handleClick() {
        history.push("/project/" + project.id + "/edit");
    }

    function handleGoBack() {
        history.goBack();
    }
}

const ProjectDetailsStyled = styled.section`
  display: grid;
  overflow: scroll;
  grid-template-rows: 1fr min-content;
  padding: var(--size-m);
`
const ButtonSectionStyled = styled.section`
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 8px;
`

const useStyles = makeStyles({
    cardHeader: {
        justifySelf: "center",
        padding: "12px",
        backgroundColor: "#F3EED9"
    },
    cardContent: {
        padding: "12px"
    },
    button: {
        backgroundColor: "#ec5864",
        color: "white",
        borderRadius: "10px",
        fontSize: "0.8em",
        padding: "8px"
    },
    editButton: {
        backgroundColor: "#ec5864",
        color: "white",
        borderRadius: "10px",
        fontSize: "0.8em",
        padding: "8px",
        margin: "8px"
    },
    goBackButton: {
        backgroundColor: "#e7e7e7",
        borderRadius: "10px",
        fontSize: "0.8em",
        padding: "8px"
    },
    card: {
        height: "82vh",
        backgroundColor: "#FFFFFF",
        overflow: "scroll"
    },
    link: {
        color: "#666666"
    }
});