import React, {useContext, useState} from "react";
import MenuAppBar from "../navBar/NavBar";
import styled from "styled-components/macro";
import {makeStyles} from "@material-ui/core/styles";
import TextField from "@material-ui/core/TextField";
import {Button} from "@material-ui/core";
import {addProject} from "../service/ProjectService";
import UserContext from "../contexts/UserContext";
import {useHistory} from "react-router-dom";

export default function AddProject() {

    const classes = useStyles();
    const [title, setTitle] = useState("");
    const [description, setDescription] = useState("");
    const {token} = useContext(UserContext);
    const history = useHistory();

    return(
        <>
            <MenuAppBar pagename="New project"/>
            <MainStyled>
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
                        name="description"
                        label="Description"
                        autoComplete="off"
                        value={description}
                        onChange={event => setDescription(event.target.value)}
                        type="text"
                        variant="outlined"
                        InputProps={{className: classes.input}}/>
                    <Button type="submit" variant="contained">Save project</Button>
                </FormStyled>
            </MainStyled>
        </>
    );

    function handleSubmit() {
        addProject(title, description, token)
            .then(() => history.push("/project"))
            .catch(error => console.log(error));
    }
}

const useStyles = makeStyles(() => ({
    skill: {
        margin: "4px",
        backgroundColor: "#d7385e",
        color: "white"
    },
    input: {
        backgroundColor: "lightgrey"
    }
}));

const MainStyled = styled.main`
  padding: var(--size-l);
`

const FormStyled = styled.form`
  display: grid;
  gap: var(--size-l)
`