import React, {useContext, useEffect, useState} from "react";
import ProfileContext from "./ProfileContext";
import UserContext from "./UserContext";
import {getProfiles} from "../service/ProfileService";

export default function ProfileContextProvider({children}) {

    const [profiles, setProfiles] = useState([]);
    const {token, tokenIsValid} = useContext(UserContext);

    useEffect(() => {
        tokenIsValid() && getProfiles(token).then(setProfiles).catch(console.log);
    }, [token, tokenIsValid])

    return <ProfileContext.Provider value={{profiles, setProfiles}}>{children}</ProfileContext.Provider>;
}

