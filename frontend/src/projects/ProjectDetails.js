import React, {useContext} from "react";
import {useParams} from "react-router-dom";
import ProjectContext from "../contexts/ProjectContext";
import styled from "styled-components/macro";
import MenuAppBar from "../navBar/NavBar";
import makeStyles from "@material-ui/core/styles/makeStyles";
import Card from "@material-ui/core/Card";
import Box from "@material-ui/core/Box";
import {Typography} from "@material-ui/core";

export default function ProjectDetails() {

    const classes = useStyles();
    const {id} = useParams();
    const {projects} = useContext(ProjectContext);
    const project = projects?.find(project => project.id === id);

    return(
        !project ? null :
        <>
            <MenuAppBar pagename="Project details"/>
            <ProjectDetailsStyled>
                <Card className={classes.card}>
                    <Typography variant="h4">{project.title}</Typography>
                    <p>created by: {project.projectOwner}</p>
                    <hr/>
                    <p>Description:</p>
                    <Box component="div">{project.description}</Box>
                    <p>Impressions:</p>
                </Card>
            </ProjectDetailsStyled>
        </>
    );
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