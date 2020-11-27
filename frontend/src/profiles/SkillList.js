import React from "react";
import styled from "styled-components/macro";
import Chip from '@material-ui/core/Chip';
import {makeStyles} from "@material-ui/core/styles";

const useStyles = makeStyles(() => ({
    skill: {
        margin: "4px",
        backgroundColor: "#d7385e"
    }
}));

export default function SkillList({skills}) {

    const classes = useStyles();

    return(
        <>
            <h5>Skills:</h5>
            <SkillListStyled>
                {skills?.map((skill, index) =>
                    <Chip className={classes.skill} label={skill} key={index}/>
                )}
            </SkillListStyled>
        </>
    );
}

const SkillListStyled = styled.ul`
  list-style: none;
  padding: var(--size-m);
  border: black 1px solid;
`

