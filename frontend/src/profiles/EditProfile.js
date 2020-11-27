import React, {useContext, useEffect, useState} from "react";
import {useParams, useHistory} from "react-router-dom";
import NavBar from "../navBar/NavBar";
import ProfileContext from "../contexts/ProfileContext";
import TextField from "@material-ui/core/TextField";
import {updateProfile} from "../service/ProfileService";
import UserContext from "../contexts/UserContext";
import styled from "styled-components/macro";
import {Button} from "@material-ui/core";

export default function EditProfile() {

    const {username} = useParams();
    const {profiles} = useContext(ProfileContext);
    const [profileData, setProfileData] = useState(null);

    useEffect(() => {
        const profile = profiles?.find(profile => profile.username === username);
        setProfileData(profile);
    }, [profiles, username])

    const {token, setToken} = useContext(UserContext);
    const history = useHistory();



    return(
        !profileData ? null :
        <>
            <NavBar/>
            <MainStyled>
                <FormStyled onSubmit={handleSubmit}>
                    <TextField
                        name="name"
                        label="Name"
                        value={profileData.name}
                        onChange={handleChange}
                        type="text"
                        variant="outlined"/>
                    <TextField
                        name="birthday"
                        label="Birthday"
                        value={profileData.birthday}
                        onChange={handleChange}
                        type="date"
                        variant="outlined"/>
                    <TextField
                        name="location"
                        label="Location"
                        value={profileData.location}
                        onChange={handleChange}
                        type="text"
                        variant="outlined"/>
                    <Button type="submit" variant="contained">Save changes</Button>
                </FormStyled>
                <Button variant="contained" color="secondary" onClick={handleLogout}>Logout</Button>
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

    function handleLogout() {
        setToken("");
        history.push("/login");
    }
}

const MainStyled = styled.main`
  padding: var(--size-l);
`

const FormStyled = styled.form`
  display: grid;
  gap: var(--size-l)
`