import React, {useContext, useState} from "react";
import MenuAppBar from "../navBar/NavBar";
import styled from "styled-components/macro";
import {Button} from "@material-ui/core";
import Card from "@material-ui/core/Card";
import ReceivedMessages from "./ReceivedMessages";
import SentMessages from "./SentMessages";
import makeStyles from "@material-ui/core/styles/makeStyles";

export default function MessageOverview() {

    const classes = useStyles();
    const [receivedMessages, setReceivedMessages] = useState(true);

    return(
        <>
            <MenuAppBar pagename="Messages"/>
            <div>
                <ButtonSectionStyled>
                    <Button onClick={() => setReceivedMessages(true)}>Received</Button>
                    <Button onClick={() => setReceivedMessages(false)}>Sent</Button>
                </ButtonSectionStyled>
                <Card className={classes.card}>
                    {receivedMessages ? <ReceivedMessages/> : <SentMessages/>}
                </Card>
            </div>
        </>
    );
}

const ButtonSectionStyled = styled.div`
  display: grid;
  background-color: lightgrey;
  grid-template-columns: 1fr 1fr;
`

const useStyles = makeStyles({
    card: {
        height: "83vh",
        margin: "12px",
        padding: "12px",
        backgroundColor: "#FFF4F4"
    }
});