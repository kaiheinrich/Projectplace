import React, {useContext, useEffect, useState} from "react";
import MenuAppBar from "../navBar/NavBar";
import Card from "@material-ui/core/Card";
import makeStyles from "@material-ui/core/styles/makeStyles";
import {Button, Typography} from "@material-ui/core";
import {useHistory, useParams} from "react-router-dom";
import ProfileContext from "../contexts/ProfileContext";
import Box from "@material-ui/core/Box";
import UserContext from "../contexts/UserContext";

export default function Message() {

    const classes = useStyles();
    const {id} = useParams();
    const {userCredentials} = useContext(UserContext);
    const username = userCredentials.sub;
    const {profiles} = useContext(ProfileContext);
    const profile = profiles?.find(profile => profile.username === username);
    const receivedMessage = profile?.receivedMessages.find(message => message.id === id);
    const sentMessage = profile?.sentMessages.find(message => message.id === id);
    const [message, setMessage] = useState();
    const history = useHistory();
    const [replyRecipient, setReplyRecipient] = useState("");

    useEffect(() => {
        setMessage(receivedMessage ? receivedMessage : sentMessage);
    }, [receivedMessage, sentMessage])

    useEffect(() => {
        setReplyRecipient(receivedMessage ? message?.sender : message?.recipient)
    }, [message])

    return(
        <>
        <MenuAppBar pagename="Message"/>
        <Card className={classes.card}>
            <Typography>from {message?.sender} to {message?.recipient}</Typography>
            <Typography>{message?.subject}</Typography>
            <Box>{message?.message}</Box>
            <Button onClick={() => history.push(`/messageto/${replyRecipient}`)}>Reply</Button>
        </Card>
        </>
    );
}

const useStyles = makeStyles({
    card: {
        height: "83vh",
        margin: "12px",
        padding: "12px",
        backgroundColor: "#FFF4F4"
    }
});