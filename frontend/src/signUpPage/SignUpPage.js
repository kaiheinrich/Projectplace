import React, {useContext, useState} from "react";
import TextField from "@material-ui/core/TextField";
import styled from "styled-components/macro";
import {Button, Typography} from "@material-ui/core";
import { useHistory } from "react-router-dom";
import UserContext from "../contexts/UserContext";

export default function SignUpPage() {

    const [username, setUsername] = useState("");
    const [password1, setPassword1] = useState("");
    const [password2, setPassword2] = useState("");
    const [errorFrontend, setErrorFrontend] = useState("");
    const [errorBackend, setErrorBackend] = useState("");
    const {postSignUp} = useContext(UserContext);
    const history = useHistory();

    return(
        <>
        <header>Sign up</header>
            <main>
                <Form onSubmit={handleSubmit}>
                    <TextField
                        name="username"
                        label="Choose your username"
                        value={username}
                        onChange={(event) => setUsername(event.target.value)}
                        type="text"
                        variant="outlined"/>
                        <Typography>Your password must contain at least 8 characters including uppercase, lowercase & numbers.</Typography>
                    <TextField
                        name="password"
                        label="Set your password"
                        value={password1}
                        onChange={(event) => setPassword1(event.target.value)}
                        type="password"
                        variant="outlined"/>
                    <TextField
                        name="password"
                        label="Repeat your password"
                        value={password2}
                        onChange={(event) => setPassword2(event.target.value)}
                        type="password"
                        variant="outlined"/>
                    <Button type="submit">Get started</Button>
                    <div>
                        {errorFrontend && <p>{errorFrontend}</p>}
                        {errorBackend && <p>{errorBackend}</p>}
                    </div>
                </Form>
            </main>
        </>
    );

    function handleSubmit(event) {
        event.preventDefault();

        try {
            checkIfPasswordsMatch();
            validatePassword();

            postSignUp(username, password1)
                .then(() => history.push("/login"))
                .catch((error) => setErrorBackend(error.message))
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



const Form = styled.form`
  padding: var(--size-l);
  display: grid;
  grid-auto-rows: min-content;
  gap: var(--size-m);
  
  input {
    display: block;
    width: 100%;
  }
`