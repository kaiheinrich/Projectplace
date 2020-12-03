import React, {useContext, useEffect, useState} from "react";
import {useParams, useHistory} from "react-router-dom";
import ProjectContext from "../contexts/ProjectContext";
import styled from "styled-components/macro";
import MenuAppBar from "../navBar/NavBar";
import makeStyles from "@material-ui/core/styles/makeStyles";
import Card from "@material-ui/core/Card";
import Box from "@material-ui/core/Box";
import {Button, Typography} from "@material-ui/core";
import UserContext from "../contexts/UserContext";
import AddProjectButton from "../addProjectButton/AddProjectButton";

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
            <MenuAppBar pagename="Project details"/>
            <ProjectDetailsStyled>
                <Card className={classes.card}>
                    <Typography variant="h4">{project.title}</Typography>
                    <p>created by: {project.projectOwner}</p>
                    {buttonEnabled ? <Button onClick={handleClick}>Edit project</Button> : null}
                    <hr/>
                    <p>Description:</p>
                    <Box component="div">{project.description}</Box>
                    <p>Impressions:</p>
                </Card>
            </ProjectDetailsStyled>
            <AddProjectButton/>
        </>
    );

    function handleClick() {
        history.push("/project/" + project.id + "/edit");
    }
}

const ProjectDetailsStyled = styled.section`
  display: grid;
  overflow: scroll;
  grid-template-rows: 1fr min-content;
  padding: var(--size-m);
`

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
        padding: "8px",
        backgroundColor: "#FFF4F4"
    }
});