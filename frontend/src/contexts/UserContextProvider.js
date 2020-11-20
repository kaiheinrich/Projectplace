import React, {useEffect, useState} from "react";
import UserContext from "./UserContext";
import axios from "axios";
import {
    loadTokenFromLocalStorage,
    loadUserCredentialsFromLocalStorage,
    saveTokenToLocalStorage, saveUserCredentialsToLocalStorage
} from "../service/LocalStorageService";
import jwtDecode from "jwt-decode";

export default function UserContextProvider({children}) {

    const [token, setToken] = useState(loadTokenFromLocalStorage());
    const [userCredentials, setUserCredentials] = useState(loadUserCredentialsFromLocalStorage());

    useEffect(() => {
        if (token) {
            try {
                const decoded = jwtDecode(token);
                if (decoded.exp > new Date().getTime() / 1000) {
                    setUserCredentials(decoded);
                    saveTokenToLocalStorage(token);
                    saveUserCredentialsToLocalStorage(decoded);
                }
            } catch (exception) {
                console.log(exception);
            }
        }
    }, [token])

    const tokenIsValid = () =>
        token && userCredentials?.exp > new Date().getTime() / 1000;

    const postLogin = (credentials) =>
        axios
            .post("/auth/login", credentials)
            .then(response => setToken(response.data));

    return <UserContext.Provider value={{token, tokenIsValid, postLogin, userCredentials}}>{children}</UserContext.Provider>;
}
