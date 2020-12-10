import React, {useContext} from "react";
import ProjectContext from "../contexts/ProjectContext";
import styled from "styled-components/macro";
import ProjectOverviewItem from "./ProjectOverviewItem";
import MenuAppBar from "../navBar/NavBar";
import AddProjectButton from "../addProjectButton/AddProjectButton";

export default function ProjectOverview() {

    const {projects} = useContext(ProjectContext);

    return(
        <>
            <MenuAppBar pagename="Projects" searchIsActive={false}/>
            <ListStyled>
                {projects?.map(project =>
                    <li key={project.id}>
                        <ProjectOverviewItem project={project}/>
                    </li>
                )}
            </ListStyled>
            <AddProjectButton/>
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

