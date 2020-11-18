import React, {useContext, useState} from "react";
import styled from "styled-components/macro";
import UserContext from "../contexts/UserContext";
import { useHistory } from "react-router-dom";

const initialCredentials = {
    username: "",
    password: ""
}

export default function LoginPage() {

    const [credentials, setCredentials] = useState(initialCredentials);
    const {postLogin} = useContext(UserContext);
    const history = useHistory();
    const [error, setError] = useState("");

    return(
        <>
            <header title="Login"/>
            <main>
                <Form onSubmit={handleSubmit}>
                    <label>
                        Username
                        <input
                            name="username"
                            value={credentials.username}
                            onChange={handleChange}
                            type="text"
                        />
                    </label>
                    <label>
                        Password
                        <input
                            name="password"
                            value={credentials.password}
                            onChange={handleChange}
                            type="password"
                        />
                    </label>
                    <button>Login</button>
                </Form>
                {error && <div>{error}</div>}
            </main>
        </>
    );

    function handleSubmit(event) {
        event.preventDefault();
        postLogin(credentials).then(() => history.push("/success")).catch(() => setError("Invalid credentials!"));
    }

    function handleChange(event) {
        setCredentials({...credentials, [event.target.name]: event.target.value})
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
  
  button {
    padding: var(--size-m);
    border: none;
    background: var(--red);
    color: white;
    border-radius: var(--size-s);
    font-size: 1em;
  }
`