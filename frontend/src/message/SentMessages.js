import React, {useContext} from "react";
import {Link, useParams} from "react-router-dom";
import ProfileContext from "../contexts/ProfileContext";
import Card from "@material-ui/core/Card";
import {CardContent, CardHeader} from "@material-ui/core";
import makeStyles from "@material-ui/core/styles/makeStyles";
import styled from "styled-components/macro";
import Typography from "@material-ui/core/Typography";

export default function SentMessages() {

    const classes = useStyles();
    const {username} = useParams();
    const {profiles} = useContext(ProfileContext);
    const profile = profiles?.find(profile => profile.username === username);

    return(
        <>
            <Card className={classes.root} variant="elevation" elevation={20}>
                <CardHeader
                    className={classes.cardHeader}
                    title="Outbox"
                    titleTypographyProps={{align:"center"}}/>
                <CardContent className={classes.messages}>
                    <MessageList>
                        {profile.sentMessages?.slice(0).reverse().map(message =>
                            <Link key={message.id} to={`/message/${message.id}`}><Typography align="left">To: {message.recipient}, Subject: {message.subject}, Time: {message.timestamp}</Typography></Link>)}
                    </MessageList>
                </CardContent>
            </Card>
        </>
    );
}

const useStyles = makeStyles({
    root: {
        minWidth: 275,
        backgroundColor: "#FFFFFF",
        borderRadius: "5px",
        height: "100%"
    },
    cardHeader: {
        backgroundColor: "#F3EED9",
    },
    messages: {
        display: "grid",
        gridAutoRows: "min-content",
        padding: "16px",
        gap: "12px"
    }
});

const MessageList = styled.ul`
  list-style: none;
  margin: 0;
  padding: 0;
`