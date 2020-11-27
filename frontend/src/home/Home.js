import React from "react";
import {Button} from "@material-ui/core";
import { useHistory } from "react-router-dom";
import MenuAppBar from "../navBar/NavBar";

export default function Home() {
    const history = useHistory();

    return (
        <>
            <MenuAppBar pagename="Home"/>
            <section>
                <h1>HALLO</h1>
                <Button onClick={() => history.push("/profile")}>See profiles</Button>
                <Button onClick={() => history.push("/project")}>See projects</Button>
            </section>
        </>
    );
}