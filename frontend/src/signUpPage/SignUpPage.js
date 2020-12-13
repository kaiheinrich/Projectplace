import React, {useContext, useState} from "react";
import TextField from "@material-ui/core/TextField";
import styled from "styled-components/macro";
import {Button, Typography} from "@material-ui/core";
import { useHistory } from "react-router-dom";
import UserContext from "../contexts/UserContext";
import {makeStyles} from "@material-ui/core/styles";
import Card from "@material-ui/core/Card";

export default function SignUpPage() {

    const classes = useStyles();
    const [username, setUsername] = useState("");
    const [password1, setPassword1] = useState("");
    const [password2, setPassword2] = useState("");
    const [errorFrontend, setErrorFrontend] = useState("");
    const [errorBackend, setErrorBackend] = useState("");
    const {postSignUp} = useContext(UserContext);
    const history = useHistory();

    return(
        <>
        <HeaderStyled>Sign up to Projectplace</HeaderStyled>
            <Card className={classes.card} elevation={"10"} raised={true}>
                <Form onSubmit={handleSubmit}>
                    <TextField
                        name="username"
                        label="Choose your username"
                        value={username}
                        onChange={(event) => setUsername(event.target.value)}
                        type="text"
                        required={true}
                        variant="outlined"/>
                        <Typography>Your password must contain at least 8 characters including uppercase, lowercase & numbers.</Typography>
                    <TextField
                        name="password"
                        label="Set your password"
                        value={password1}
                        onChange={(event) => setPassword1(event.target.value)}
                        type="password"
                        required={true}
                        variant="outlined"
                        InputProps={{className: classes.input}}/>
                    <TextField
                        name="password"
                        label="Repeat your password"
                        value={password2}
                        onChange={(event) => setPassword2(event.target.value)}
                        type="password"
                        required={true}
                        variant="outlined"
                        InputProps={{className: classes.input}}/>
                    <Button type="submit" variant={"contained"}>Get started</Button>
                    <div>
                        {errorFrontend && <p>{errorFrontend}</p>}
                        {errorBackend && <p>{errorBackend}</p>}
                    </div>
                </Form>
                <Button className={classes.goBackButton} onClick={() => history.push("/login")}>Back to login</Button>
            </Card>
        </>
    );

    function handleSubmit(event) {
        event.preventDefault();

        try {
            checkIfPasswordsMatch();
            validatePassword();

            postSignUp(username, password1)
                .then(() => history.push("/login"))
                .catch((error) => {
                    console.log(error.response.data.message);
                    setErrorBackend(error.response.data.message);
                })
        } catch (error) {
            setErrorFrontend(error.message)
        }
    }

    function checkIfPasswordsMatch() {
        if (password1 !== password2) {
            throw new Error("Passwords are not matching!");
        }
    }

    function validatePassword() {
        checkPasswordLength();
        checkIfPasswordContainsNumbers();
        checkIfPasswordContainsSmallLetters();
        checkIfPasswordContainsUppercaseLetters();
    }

    function checkPasswordLength() {
        if (password1.length < 8) {
            throw new Error("Password must contain at least 8 characters!");
        }
    }
    function checkIfPasswordContainsNumbers() {
        if (!/\d/.test(password1)) {
            throw new Error("Password must contain at least one number!");
        }
    }
    function checkIfPasswordContainsSmallLetters() {
        if (!/[a-z]/.test(password1)) {
            throw new Error("Password must contain at least one lowercase letter!");
        }
    }
    function checkIfPasswordContainsUppercaseLetters() {
        if (!/[A-Z]/.test(password1)) {
            throw new Error("Password must contain at least one uppercase letter!");
        }
    }
}

const useStyles = makeStyles(() => ({
    input: {
        backgroundColor: "lightgrey"
    },
    link: {
        backgroundColor: "white",
        padding: "8px",
        color: "black"
    },
    card: {
        height: "min-content",
        padding: "16px"
    },
    goBackButton: {
        backgroundColor: "#e7e7e7",
        borderRadius: "10px",
        fontSize: "0.8em",
        padding: "8px",
        width: "100%"
    }
}));

const HeaderStyled = styled.header`
  text-align: center;
  color: white;
  font-style: italic;
  padding: var(--size-l);
  background-color: var(--red-dark);
  font-size: 150%;
`;

const Form = styled.form`
  display: grid;
  grid-auto-rows: min-content;
  gap: var(--size-m);
  
  input {
    display: block;
    width: 100%;
  }
  
  button {
    padding: var(--size-m);
    border: none;
    background: var(--red-dark);
    color: white;
    border-radius: var(--size-s);
    font-size: 1em;
  }
`