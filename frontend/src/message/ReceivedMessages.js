import React, {useContext} from "react";
import {Link, useParams} from "react-router-dom";
import ProfileContext from "../contexts/ProfileContext";

export default function ReceivedMessages() {

    const {username} = useParams();
    const {profiles} = useContext(ProfileContext);
    const profile = profiles?.find(profile => profile.username === username);

    return(
        <ul>
            {profile?.receivedMessages.map(message =>
            <Link to={`/message/${message.id}`}><li>by {message.sender} at {message.timestamp}</li></Link>)}
        </ul>
    );
}