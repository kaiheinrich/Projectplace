import React, {useContext, useEffect, useState} from "react";
import {useParams, useHistory} from "react-router-dom";
import ProfileContext from "../contexts/ProfileContext";
import TextField from "@material-ui/core/TextField";
import {updateProfile, uploadProfileImage} from "../service/ProfileService";
import UserContext from "../contexts/UserContext";
import styled from "styled-components/macro";
import {Avatar, Button} from "@material-ui/core";
import MenuAppBar from "../navBar/NavBar";
import Chip from "@material-ui/core/Chip";
import {makeStyles} from "@material-ui/core/styles";

const useStyles = makeStyles(() => ({
    skill: {
        margin: "4px",
        backgroundColor: "#d7385e",
        color: "white"
    },
    input: {
        backgroundColor: "lightgrey"
    }
}));

export default function EditProfile() {

    const classes = useStyles();

    const {username} = useParams();
    const {profiles} = useContext(ProfileContext);
    const [profileData, setProfileData] = useState(null);
    const [newSkill, setNewSkill] = useState("");

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
                    {profileData.imageName ? <Avatar alt="profile" src={profileData.imageUrl}/> : <Avatar/>}
                    <input type="file" onChange={handlePictureChange} />
                    <TextField
                        name="name"
                        label="Name"
                        autoComplete="off"
                        value={profileData.name}
                        onChange={handleChange}
                        type="text"
                        variant="outlined"
                        InputProps={{className: classes.input}}/>
                    <TextField
                        name="birthday"
                        label="Birthday"
                        autoComplete="off"
                        value={profileData.birthday}
                        onChange={handleChange}
                        type="date"
                        variant="outlined"
                        InputProps={{className: classes.input}}/>
                    <TextField
                        name="location"
                        label="Location"
                        autoComplete="off"
                        value={profileData.location}
                        onChange={handleChange}
                        type="text"
                        variant="outlined"
                        InputProps={{className: classes.input}}/>
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
                        value={newSkill}
                        onChange={(event) => setNewSkill(event.target.value)}
                        onKeyUp={(event) => event.key === "Enter" && addSkill()}
                        type="text"
                        variant="outlined"
                        InputProps={{className: classes.input}}/>
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
            profileData.imageName,
            token)
            .then(() => history.push("/profile/"+ profileData.username+ "/edit")).catch(error => console.log(error));
    }

    function handlePictureChange(event) {
        const imageFile = event.target.files[0];
        imageFile ? uploadProfileImage(imageFile, token)
            .then(data => setProfileData({...profileData, imageName: data}))
            .catch(error => console.log(error)) : setProfileData({...profileData, imageName: profileData.imageName})
    }

    function handleChange(event) {
        setProfileData({...profileData, [event.target.name]: event.target.value});
    }

    function addSkill() {
        if(newSkill) {
            profileData.skills.push(newSkill);
            setNewSkill("");
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