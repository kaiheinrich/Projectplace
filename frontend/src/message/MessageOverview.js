import React, {useState} from "react";
import MenuAppBar from "../navBar/NavBar";
import styled from "styled-components/macro";
import {Button} from "@material-ui/core";
import ReceivedMessages from "./ReceivedMessages";
import SentMessages from "./SentMessages";

export default function MessageOverview() {

    const [receivedMessages, setReceivedMessages] = useState(true);

    return(
        <>
            <MenuAppBar pagename="Messages" searchIsActive={false}/>
            <div>
                <ButtonSectionStyled>
                    <Button onClick={() => setReceivedMessages(true)}>Received</Button>
                    <Button onClick={() => setReceivedMessages(false)}>Sent</Button>
                </ButtonSectionStyled>
                    {receivedMessages ? <ReceivedMessages/> : <SentMessages/>}
            </div>
        </>
    );
}

const ButtonSectionStyled = styled.div`
  display: grid;
  background-color: lightgrey;
  grid-template-columns: 1fr 1fr;
`