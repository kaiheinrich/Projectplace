import React, {useContext, useEffect, useState} from "react";
import MenuAppBar from "../navBar/NavBar";
import Card from "@material-ui/core/Card";
import makeStyles from "@material-ui/core/styles/makeStyles";
import {Button, CardContent, CardHeader, Typography} from "@material-ui/core";
import {useHistory, useParams} from "react-router-dom";
import ProfileContext from "../contexts/ProfileContext";
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
    }, [message, receivedMessage])

    return(
        <>
        <MenuAppBar pagename="Message"/>
        <Card className={classes.card}>
            <CardHeader
                className={classes.cardHeader}
                title={`Message: ${message.subject}`}
                subheader={`from ${message?.sender} to ${message?.recipient}`}/>
            <CardContent className={classes.cardContent}>
                <Typography>{message?.message}</Typography>
            </CardContent>
            <CardContent className={classes.buttonPosition}>
            <Button className={classes.button} variant="contained" onClick={() => history.push(`/messageto/${replyRecipient}`)}>Reply</Button>
            </CardContent>
        </Card>
        </>
    );
}

const useStyles = makeStyles({
    card: {
        height: "100%",
        backgroundColor: "#FFFFFF",
        overflow: "scroll"
    },
    cardHeader: {
        padding: "12px",
        backgroundColor: "#F3EED9"
    },
    cardContent: {
        padding: "12px"
    },
    button: {
        backgroundColor: "#ec5864",
        color: "white",
        borderRadius: "10px",
        fontSize: "0.8em",
        padding: "8px",
        minWidth: "80px"
    },
    buttonPosition: {
        display: "grid",
        alignItems: "center"
    }
});