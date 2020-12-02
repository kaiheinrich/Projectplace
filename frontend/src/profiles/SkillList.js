import React from "react";
import styled from "styled-components/macro";
import Chip from '@material-ui/core/Chip';
import {makeStyles} from "@material-ui/core/styles";
import {Typography} from "@material-ui/core";

const useStyles = makeStyles(() => ({
    skill: {
        margin: "4px",
        backgroundColor: "#dee7e7"
    }
}));

export default function SkillList({skills}) {

    const classes = useStyles();

    return(
        <>
            <Typography variant="h6">Skills:</Typography>
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
  border-bottom: #672d2d 1px solid;
  border-top: #672d2d 1px solid;
  //border-radius: 10px;
`

