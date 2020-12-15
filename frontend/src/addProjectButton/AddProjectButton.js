import React from "react";
import {Fab} from "@material-ui/core";
import AddIcon from "@material-ui/icons/Add";
import styled from "styled-components/macro";
import {useHistory} from "react-router-dom";
import {makeStyles} from "@material-ui/core/styles";

export default function AddProjectButton() {

    const classes = useStyles();
    const history = useHistory();

    return(
        <FabContainerStyled>
            <Fab className={classes.addButton} color="primary" onClick={() => history.push("/project/add")}>
                <AddIcon/>
            </Fab>
        </FabContainerStyled>
    );
}

const useStyles = makeStyles(() => ({
    addButton: {
        backgroundColor: "#9f0d2f"
    }
}))

const FabContainerStyled = styled.div`
  position: fixed;
  right: var(--size-l);
  bottom: var(--size-l);
  z-index: 200;
`