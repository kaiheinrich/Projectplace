import React, {useState} from "react";
import UserContext from "./UserContext";
import axios from "axios";

export default function UserContextProvider({children}) {

    const [token, setToken] = useState("");

    const postLogin = (credentials) =>
        axios
            .post("/auth/login", credentials)
            .then(response => setToken(response.data));

    return <UserContext.Provider value={{token, postLogin}}>{children}</UserContext.Provider>;
}
