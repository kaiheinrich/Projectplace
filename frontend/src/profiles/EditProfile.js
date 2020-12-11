import React, {useContext, useEffect, useState} from "react";
import {useParams, useHistory} from "react-router-dom";
import ProfileContext from "../contexts/ProfileContext";
import TextField from "@material-ui/core/TextField";
import {getProfiles, updateProfile, uploadProfileImage} from "../service/ProfileService";
import UserContext from "../contexts/UserContext";
import styled from "styled-components/macro";
import {Avatar, Button, Grid, Typography} from "@material-ui/core";
import MenuAppBar from "../navBar/NavBar";
import Chip from "@material-ui/core/Chip";
import {makeStyles} from "@material-ui/core/styles";
import Card from "@material-ui/core/Card";
import {ImageSearchOutlined} from "@material-ui/icons";

export default function EditProfile() {

    const classes = useStyles();

    const {username} = useParams();
    const {profiles, setProfiles} = useContext(ProfileContext);
    const [profileData, setProfileData] = useState(null);
    const [newSkill, setNewSkill] = useState("");
    const [file, setFile] = useState();

    useEffect(() => {
        const profile = profiles?.find(profile => profile.username === username);
        setProfileData(profile);
    }, [profiles, username])

    const {token} = useContext(UserContext);
    const history = useHistory();



    return(
        !profileData ? null :
        <>
            <MenuAppBar pagename="Edit profile" searchIsActive={false}/>
            <Card className={classes.card}>
                <FormStyled onSubmit={handleSubmit}>
                    {file ? <Avatar className={classes.avatar} alt="profile" src={file}/> : <Avatar className={classes.avatar} alt="profile" src={profileData.imageUrl}/> }
                    <Grid container item>
                        <input
                            hidden
                            id="contained-button-file"
                            type="file"
                            onChange={handlePictureChange}
                            accept="image/*"
                        />
                        <label htmlFor="contained-button-file">
                            <Button
                                className={classes.button}
                                aria-label="upload-picture"
                                startIcon={<ImageSearchOutlined/>}
                                component="span"
                            >Upload picture</Button>
                        </label>
                    </Grid>
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
                                <Chip variant={"outlined"} className={classes.skill} label={skill} key={index} onDelete={() => handleDelete(index)}/>
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
                        {newSkill && <Typography>Press enter to add new skill</Typography>}
                    </div>
                    <Button className={classes.button} type="submit" variant="contained">Save changes</Button>
                </FormStyled>
                <Button className={classes.greyButton} variant="contained" onClick={handleGoBack}>Go Back</Button>
            </Card>
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
            .then(() => history.push("/profile/"+ profileData.username))
            .then(() => getProfiles(token).then(setProfiles))
            .catch(error => console.log(error));
    }

    function handlePictureChange(event) {
        const imageFile = event.target.files[0];
        setFile(URL.createObjectURL(imageFile));
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

    function handleGoBack() {
        history.goBack();
    }

    function handleDelete(indexToRemove) {
        setProfileData({...profileData, skills: profileData.skills.filter((_, index) => index !== indexToRemove)})
    }
}

const useStyles = makeStyles(() => ({
    skill: {
        margin: "4px",
        backgroundColor: "#F3EED9",
    },
    input: {
        backgroundColor: "#ebebeb"
    },
    card: {
        padding: "16px",
        height: "100vh",
        overflow: "scroll"
    },
    button: {
        backgroundColor: "#d7385e",
        color: "white"
    },
    greyButton: {
        width: "100%",
        backgroundColor: "#e7e7e7",
        fontSize: "0.8em",
        marginTop: "8px"
    },
    avatar: {
        width: "90px",
        height: "90px"
    }
}));

const FormStyled = styled.form`
  display: grid;
  gap: var(--size-l)
`