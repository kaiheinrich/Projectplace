import React, {useContext, useEffect, useState} from "react";
import UserContext from "./UserContext";
import {getProjects} from "../service/ProjectService";
import ProjectContext from "./ProjectContext";

export default function ProjectContextProvider({children}) {

    const [projects, setProjects] = useState([]);
    const {token, tokenIsValid} = useContext(UserContext);

    useEffect(() => {
        tokenIsValid() && getProjects(token).then(setProjects).catch(console.log);
    }, [token, tokenIsValid])

    return <ProjectContext.Provider value={{projects}}>{children}</ProjectContext.Provider>
}