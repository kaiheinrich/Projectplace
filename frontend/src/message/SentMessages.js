import React, {useContext} from "react";
import {Link, useParams} from "react-router-dom";
import ProfileContext from "../contexts/ProfileContext";

export default function SentMessages() {

    const {username} = useParams();
    const {profiles} = useContext(ProfileContext);
    const profile = profiles?.find(profile => profile.username === username);

    return(
        <ul>
            {profile.sentMessages?.map(message =>
                <Link to={`/message/${message.id}`}><li>to {message.recipient} at {message.timestamp}</li></Link>)}
        </ul>
    );
}