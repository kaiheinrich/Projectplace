import React, {useContext, useEffect, useState} from "react";
import {useParams, useHistory} from "react-router-dom";
import ProfileContext from "../contexts/ProfileContext";
import TextField from "@material-ui/core/TextField";
import {updateProfile} from "../service/ProfileService";
import UserContext from "../contexts/UserContext";
import styled from "styled-components/macro";
import {Button} from "@material-ui/core";
import MenuAppBar from "../navBar/NavBar";
import Chip from "@material-ui/core/Chip";
import {makeStyles} from "@material-ui/core/styles";

const useStyles = makeStyles(() => ({
    skill: {
        margin: "4px",
        backgroundColor: "#d7385e"
    }
}));

export default function EditProfile() {

    const classes = useStyles();

    const {username} = useParams();
    const {profiles} = useContext(ProfileContext);
    const [profileData, setProfileData] = useState(null);
    const [newskill, setNewskill] = useState("");

    useEffect(() => {
        const profile = profiles?.find(profile => profile.username === username);
        setProfileData(profile);
    }, [profiles, username])

    const {token} = useContext(UserContext);
    const history = useHistory();



    return(
        !profileData ? null :
        <>
            <MenuAppBar pagename="Edit profile"/>
            <MainStyled>
                <FormStyled onSubmit={handleSubmit}>
                    <TextField
                        name="name"
                        label="Name"
                        autoComplete="off"
                        value={profileData.name}
                        onChange={handleChange}
                        type="text"
                        variant="outlined"/>
                    <TextField
                        name="birthday"
                        label="Birthday"
                        autoComplete="off"
                        value={profileData.birthday}
                        onChange={handleChange}
                        type="date"
                        variant="outlined"/>
                    <TextField
                        name="location"
                        label="Location"
                        autoComplete="off"
                        value={profileData.location}
                        onChange={handleChange}
                        type="text"
                        variant="outlined"/>
                    <div>
                        <ul>
                            {profileData.skills.map((skill, index) =>
                                <Chip className={classes.skill} label={skill} key={index} onDelete={() => handleDelete(index)}/>
                            )}
                        </ul>
                        <TextField
                        name="skills"
                        label="Add new skill"
                        autoComplete="off"
                        value={newskill}
                        onChange={(event) => setNewskill(event.target.value)}
                        onKeyUp={(event) => event.key === "Enter" && addSkill()}
                        type="text"
                        variant="outlined"/>
                    </div>
                    <Button type="submit" variant="contained">Save changes</Button>
                </FormStyled>

            </MainStyled>
        </>
    );

    function handleSubmit(event) {
        event.preventDefault();
        updateProfile(
            profileData.username,
            profileData.name,
            profileData.birthday,
            profileData.location,
            profileData.skills,
            token)
            .then(() => history.push("/profile/"+ profileData.username+ "/edit")).catch(error => console.log(error));
    }

    function handleChange(event) {
        setProfileData({...profileData, [event.target.name]: event.target.value});
    }

    function addSkill() {
        if(newskill) {
            profileData.skills.push(newskill);
            setNewskill("");
        }

    }

    function handleDelete(indexToRemove) {
        setProfileData({...profileData, skills: profileData.skills.filter((_, index) => index !== indexToRemove)})
    }
}

const MainStyled = styled.main`
  padding: var(--size-l);
`

const FormStyled = styled.form`
  display: grid;
  gap: var(--size-l)
`