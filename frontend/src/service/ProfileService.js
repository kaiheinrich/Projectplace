import axios from "axios";

const header = (token) => ({
    headers: {
        Authorization: `Bearer ${token}`
    }
});

export const getProfiles = (token) =>
    axios.get("/api/profiles", header(token)).then(response => response.data);

export const updateProfile = (username, name, birthday, location, skills, token) =>
    axios.put("/api/profiles/" + username, {name, birthday, location, skills}, header(token))
        .then(response => response.data);