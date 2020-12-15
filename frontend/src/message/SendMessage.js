import React, {useContext, useState} from "react";
import {useParams, useHistory} from "react-router-dom";
import UserContext from "../contexts/UserContext";
import styled from "styled-components/macro";
import {makeStyles} from "@material-ui/core/styles";
import TextField from "@material-ui/core/TextField";
import {getProfiles, sendMessage} from "../service/ProfileService";
import MenuAppBar from "../navBar/NavBar";
import Card from "@material-ui/core/Card";
import Button from "@material-ui/core/Button";
import ProfileContext from "../contexts/ProfileContext";

export default function SendMessage() {

    const classes = useStyles();
    const {username} = useParams();
    const history = useHistory();
    const {userCredentials, token} = useContext(UserContext);
    const {setProfiles} = useContext(ProfileContext);
    const [subject, setSubject] = useState("");
    const [message, setMessage] = useState("");

    return(
        <>
            <MenuAppBar pagename={"Message to " + username} searchIsActive={false}/>
            <Card className={classes.card} elevation={"10"} raised={true}>
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
                        multiline={true}
                        onChange={event => setMessage(event.target.value)}
                        type="text"
                        variant="outlined"
                        InputProps={{className: classes.input}}/>
                    <Button type="submit" variant="contained">Send</Button>
                </FormStyled>
            </Card>
        </>
    );

    function handleSubmit(event) {
        event.preventDefault();
        sendMessage(subject, message, userCredentials.sub, username, token)
            .then(() => getProfiles(token).then(setProfiles))
            .then(() => history.push(`/profile/${userCredentials.sub}/messages`));
    }
}

const FormStyled = styled.form`
  display: grid;
  gap: var(--size-l);
  
  button {
    padding: var(--size-m);
    border: none;
    background: var(--red-dark);
    color: white;
    border-radius: var(--size-s);
    font-size: 1em;
  }
`

const useStyles = makeStyles(() => ({
    input: {
        backgroundColor: "inherit"
    },
    card: {
        height: "min-content",
        padding: "12px",
        backgroundColor: "#F3EED9"
    }
}));