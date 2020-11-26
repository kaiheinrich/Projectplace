import React, {useContext} from "react";
import ProjectContext from "../contexts/ProjectContext";
import NavBar from "../navBar/NavBar";
import styled from "styled-components/macro";
import ProjectOverviewItem from "./ProjectOverviewItem";

export default function ProjectOverview() {

    const {projects} = useContext(ProjectContext);

    return(
        <>
            <NavBar/>
            <ListStyled>
                {projects?.map(project =>
                    <li key={project.id}>
                        <ProjectOverviewItem project={project}/>
                    </li>
                )}
            </ListStyled>
        </>
    );
}

const ListStyled = styled.ul`
  display: grid;
  grid-auto-rows: min-content;
  list-style: none;
  margin: var(--size-l);
  gap: var(--size-l);
  padding: 0;
`;