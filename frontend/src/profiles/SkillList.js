import React from "react";

export default function SkillList({skills}) {

    return(
        <>
            <h5>Skills:</h5>
            <ul>
                {skills?.map(skill =>
                    <li key={skill}>{skill}</li>
                )}
            </ul>
        </>
    );
}