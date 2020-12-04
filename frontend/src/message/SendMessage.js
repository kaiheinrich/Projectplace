import React, {useContext, useState} from "react";
import {useParams, useHistory} from "react-router-dom";
import UserContext from "../contexts/UserContext";
import styled from "styled-components/macro";
import {makeStyles} from "@material-ui/core/styles";
import TextField from "@material-ui/core/TextField";
import {Button} from "@material-ui/core";
import {sendMessage} from "../service/ProfileService";
import MenuAppBar from "../navBar/NavBar";

export default function SendMessage() {

    const classes = useStyles();
    const {username} = useParams();
    const history = useHistory();
    const {userCredentials, token} = useContext(UserContext);
    const [subject, setSubject] = useState("");
    const [message, setMessage] = useState("");

    return(
        <>
            <MenuAppBar pagename={"Message to " + username}/>
            <MainStyled>
                <FormStyled onSubmit={handleSubmit}>
                    <TextField
                        name="subject"
                        label="Subject"
                        autoComplete="off"
                        value={subject}
                        onChange={event => setSubject(event.target.value)}
                        type="text"
                        variant="outlined"
                        InputProps={{className: classes.input}}/>
                    <TextField
                        name="message"
                        label="Message"
                        autoComplete="off"
                        value={message}
                        onChange={event => setMessage(event.target.value)}
                        type="text"
                        variant="outlined"
                        InputProps={{className: classes.input}}/>
                    <Button type="submit" variant="contained">Send</Button>
                </FormStyled>
            </MainStyled>
        </>
    );

    function handleSubmit(event) {
        event.preventDefault();
        sendMessage(subject, message, userCredentials.sub, username, token)
            .then(() => history.push("/"));
    }
}

const MainStyled = styled.main`
  padding: var(--size-l);
`

const FormStyled = styled.form`
  display: grid;
  gap: var(--size-l)
`

const useStyles = makeStyles(() => ({
    input: {
        backgroundColor: "lightgrey"
    }
}));