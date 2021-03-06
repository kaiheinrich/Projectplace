import React, {useContext, useEffect, useState} from "react";
import {useParams, useHistory} from "react-router-dom";
import ProjectContext from "../contexts/ProjectContext";
import UserContext from "../contexts/UserContext";
import MenuAppBar from "../navBar/NavBar";
import styled from "styled-components/macro";
import {makeStyles} from "@material-ui/core/styles";
import TextField from "@material-ui/core/TextField";
import {Button, Grid} from "@material-ui/core";
import {deleteProject, getProjects, updateProject, uploadProjectImage} from "../service/ProjectService";
import Card from "@material-ui/core/Card";
import {ImageSearchOutlined} from "@material-ui/icons";
import {getProfiles} from "../service/ProfileService";
import ProfileContext from "../contexts/ProfileContext";

export default function EditProject() {

    const classes = useStyles();
    const {id} = useParams();
    const {projects, setProjects} = useContext(ProjectContext);
    const {setProfiles} = useContext(ProfileContext);
    const [projectData, setProjectData] = useState(null);
    const [file, setFile] = useState();

    useEffect(() => {
        const project = projects?.find(project => project.id === id);
        setProjectData(project);
    }, [projects, id])

    const {token} = useContext(UserContext);
    const history = useHistory();

    return(
        !projectData ? null :
        <>
            <MenuAppBar pagename="Edit project" searchIsActive={false}/>
            <Card className={classes.card}>
                <FormStyled onSubmit={handleSubmit}>
                    <TextField
                    name="title"
                    label="Title"
                    autoComplete="off"
                    value={projectData.title}
                    onChange={handleChange}
                    type="text"
                    variant="outlined"
                    InputProps={{className: classes.input}}/>
                    <TextField
                        name="teaser"
                        label="Teaser (50 characters max)"
                        autoComplete="off"
                        value={projectData.teaser}
                        onChange={handleChange}
                        type="text"
                        variant="outlined"
                        InputProps={{className: classes.input}}/>
                    <TextField
                    name="description"
                    label="Description"
                    autoComplete="off"
                    multiline={true}
                    value={projectData.description}
                    onChange={handleChange}
                    type="text"
                    variant="outlined"
                    InputProps={{className: classes.input}}/>
                    {file ? <img width="100%" alt="project" src={file}/> : <img width="100%" alt="project" src={projectData.imageUrl}/>}
                    <Grid>
                        <input
                            hidden
                            id="contained-file"
                            type="file"
                            onChange={handlePictureChange}
                            accept="image/*"/>
                        <label htmlFor="contained-file">
                            <Button
                                className={classes.button}
                                aria-label="upload-picture"
                                startIcon={<ImageSearchOutlined/>}
                                component="span"
                            >Upload picture</Button>
                        </label>
                    </Grid>
                    <Button className={classes.button} type="submit" variant="contained">Save changes</Button>
                </FormStyled>
                <ButtonGroupStyled>
                    <Button className={classes.greyButton} variant="contained" onClick={handleGoBack}>Go Back</Button>
                    <Button className={classes.greyButton} variant="contained" onClick={handleDelete}>Delete project</Button>
                </ButtonGroupStyled>
            </Card>
        </>
    );

    function handleSubmit(event) {
        event.preventDefault();
        updateProject(id, projectData.title, projectData.description, projectData.imageName, projectData.teaser, token)
            .then(() => history.push("/project/" + projectData.id))
            .then(() => getProjects(token).then(setProjects))
            .then(() => getProfiles(token).then(setProfiles))
            .catch(error => console.log(error));
    }

    function handleGoBack() {
        history.goBack();
    }

    function handleDelete() {
        deleteProject(id, token)
            .then(() => getProjects(token).then(setProjects))
            .then(() => getProfiles(token).then(setProfiles))
            .then(() => history.push("/project"));
    }

    function handleChange(event) {
        setProjectData({...projectData, [event.target.name]: event.target.value});
    }

    function handlePictureChange(event) {
        const imageFile = event.target.files[0];
        setFile(URL.createObjectURL(imageFile));
        imageFile ? uploadProjectImage(imageFile, token)
            .then(data => setProjectData({...projectData, imageName: data}))
            .catch(error => console.log(error)) : setProjectData({...projectData, imageName: projectData.imageName})
    }
}

const useStyles = makeStyles(() => ({
    skill: {
        margin: "4px",
        backgroundColor: "#d7385e",
        color: "white"
    },
    input: {
        backgroundColor: "inherit"
    },
    card: {
        padding: "16px",
        height: "100vh",
        overflow: "scroll",
        backgroundColor: "#F3EED9"
    },
    button: {
        backgroundColor: "#d7385e",
        color: "white"
    },
    greyButton: {
        width: "100%",
        backgroundColor: "#e7e7e7",
        fontSize: "0.8em"
    }
}));

const FormStyled = styled.form`
  display: grid;
  gap: var(--size-l)
`

const ButtonGroupStyled = styled.div`
  display: grid;
  grid-template-columns: 1fr 1fr;
  margin-top: 8px;
  gap: 8px
`