import React, {useContext} from "react";
import ProfileContext from "../contexts/ProfileContext";
import ProfileOverviewItem from "./ProfileOverviewItem";

export default function ProfileOverview() {

    const {profiles} = useContext(ProfileContext);

    return(
        <>
            <header>Profiles</header>
            <ul>
                {profiles?.map(profile =>
                <li key={profile.username}>
                    <ProfileOverviewItem profile={profile}/>
                </li>
                )}
            </ul>
        </>
        );
}