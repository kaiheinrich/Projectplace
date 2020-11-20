import React from "react";

export default function ProfileOverviewItem({profile}) {

    return (
        <div>
            {profile.username}
            {profile.name}
            {profile.location}
            {profile.birthday}
            {profile.skills}
        </div>
    );
}