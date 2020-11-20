import axios from "axios";

const header = (token) => ({
    headers: {
        Authorization: `Bearer ${token}`
    }
});

export const getProfiles = (token) =>
    axios.get("/api/profiles", header(token)).then(response => response.data);