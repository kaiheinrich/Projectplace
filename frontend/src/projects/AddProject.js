import React, {useContext, useState} from "react";
import MenuAppBar from "../navBar/NavBar";
import styled from "styled-components/macro";
import {makeStyles} from "@material-ui/core/styles";
import TextField from "@material-ui/core/TextField";
import {Button, Grid} from "@material-ui/core";
import {addProject, getProjects, uploadProjectImage} from "../service/ProjectService";
import UserContext from "../contexts/UserContext";
import {useHistory} from "react-router-dom";
import {ImageSearchOutlined} from "@material-ui/icons";
import Card from "@material-ui/core/Card";
import ProjectContext from "../contexts/ProjectContext";
import {getProfiles} from "../service/ProfileService";
import ProfileContext from "../contexts/ProfileContext";

export default function AddProject() {

    const classes = useStyles();
    const {setProjects} = useContext(ProjectContext);
    const {setProfiles} = useContext(ProfileContext);
    const [title, setTitle] = useState("");
    const [description, setDescription] = useState("");
    const [teaser, setTeaser] = useState("");
    const [imageName, setImageName] = useState("");
    const [file, setFile] = useState();
    const {token} = useContext(UserContext);
    const history = useHistory();

    return(
        <>
            <MenuAppBar pagename="New project" searchIsActive={false}/>
            <Card className={classes.card}>
                <FormStyled onSubmit={handleSubmit}>
                    <TextField
                        name="title"
                        label="Title"
                        autoComplete="off"
                        value={title}
                        onChange={event => setTitle(event.target.value)}
                        type="text"
                        variant="outlined"
                        InputProps={{className: classes.input}}/>
                    <TextField
                        name="teaser"
                        label="Teaser (50 characters max)"
                        autoComplete="off"
                        value={teaser}
                        onChange={event => setTeaser(event.target.value)}
                        type="text"
                        variant="outlined"
                        InputProps={{className: classes.input}}/>
                    <TextField
                        name="description"
                        label="Description"
                        autoComplete="off"
                        multiline={true}
                        value={description}
                        onChange={event => setDescription(event.target.value)}
                        type="text"
                        variant="outlined"
                        InputProps={{className: classes.input}}/>
                    {file && <img width="100%" alt="project" src={file}/>}
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
                    <Button className={classes.button} type="submit" variant="contained">Save project</Button>
                </FormStyled>
                <Button className={classes.goBackButton} variant="contained" onClick={handleGoBack}>Go Back</Button>
            </Card>
        </>
    );

    function handleSubmit() {
        addProject(title, description, imageName, teaser, token)
            .then(() => getProjects(token).then(setProjects))
            .then(() => getProfiles(token).then(setProfiles))
            .catch(error => console.log(error));
        history.push("/project");
    }

    function handlePictureChange(event) {
        const imageFile = event.target.files[0];
        setFile(URL.createObjectURL(imageFile));
        uploadProjectImage(imageFile, token)
            .then(data => setImageName(data))
            .catch(error => console.log(error));
    }

    function handleGoBack() {
        history.goBack();
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
    goBackButton: {
        width: "100%",
        backgroundColor: "#e7e7e7",
        fontSize: "0.8em"
    }
}));

const FormStyled = styled.form`
  display: grid;
  gap: var(--size-l)
`