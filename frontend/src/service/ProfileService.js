import axios from "axios";

const header = (token) => ({
    headers: {
        Authorization: `Bearer ${token}`
    }
});

export const getProfiles = (token) =>
    axios.get("/api/profiles", header(token)).then(response => response.data);

export const updateProfile = (username, name, birthday, location, skills, imageUrl, token) =>
    axios.put("/api/profiles/" + username, {name, birthday, location, skills, imageUrl}, header(token))
        .then(response => response.data);

export const uploadProfileImage = (file, token) => {
    const formData = new FormData();
    formData.append('image', file);
    return axios.post('/api/profiles/image', formData, {
        headers: {
            'Content-Type': 'multipart/form-data',
            Authorization: `Bearer ${token}`
        },
    }).then(response => response.data);
}