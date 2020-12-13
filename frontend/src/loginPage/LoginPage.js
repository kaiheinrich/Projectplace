import React, {useContext, useState} from "react";
import styled from "styled-components/macro";
import UserContext from "../contexts/UserContext";
import {Link, useHistory} from "react-router-dom";
import {Button, TextField, Typography} from "@material-ui/core";
import {makeStyles} from "@material-ui/core/styles";
import Card from "@material-ui/core/Card";

const initialCredentials = {
    username: "",
    password: ""
}

export default function LoginPage() {

    const classes = useStyles();
    const [credentials, setCredentials] = useState(initialCredentials);
    const {postLogin} = useContext(UserContext);
    const history = useHistory();
    const [error, setError] = useState("");

    return(
        <>
            <HeaderStyled title="Login">Welcome to Projectplace!</HeaderStyled>
            <Card className={classes.card} raised={true}>
                <Form autoComplete="off" onSubmit={handleSubmit}>
                    <TextField
                        name="username"
                        label="Username"
                        value={credentials.username}
                        onChange={handleChange}
                        type="text"
                        variant="outlined"
                        InputProps={{className: classes.input}}/>
                    <TextField
                        name="password"
                        label="Password"
                        value={credentials.password}
                        onChange={handleChange}
                        type="password"
                        variant="outlined"
                        InputProps={{className: classes.input}}/>
                    <Button type="submit">Login</Button>
                {error && <div>{error}</div>}
                <Typography className={classes.link}>Not part of the community?<Link to="/signup" className={classes.link}>Sign up here!</Link></Typography>
                </Form>
            </Card>
        </>
    );

    function handleSubmit(event) {
        event.preventDefault();
        postLogin(credentials).then(() => history.push("/home")).catch(() => setError("Invalid credentials!"));
    }

    function handleChange(event) {
        setCredentials({...credentials, [event.target.name]: event.target.value})
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
        height: "min-content"
    }
}));

const Form = styled.form`
  padding: var(--size-l);
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

const HeaderStyled = styled.header`
  text-align: center;
  color: white;
  font-style: italic;
  padding: var(--size-l);
  background-color: var(--red-dark);
  font-size: 150%;
`;