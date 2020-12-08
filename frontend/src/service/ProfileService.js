import axios from "axios";

const header = (token, options = {}) => ({
    headers: {
        Authorization: `Bearer ${token}`,
        ...options
    }
});

export const getProfiles = (token) =>
    axios.get("/api/profiles", header(token)).then(response => response.data);

export const updateProfile = (username, name, birthday, location, skills, imageName, token) =>
    axios.put("/api/profiles/" + username, {name, birthday, location, skills, imageName}, header(token))
        .then(response => response.data);

export const uploadProfileImage = (file, token) => {
    const formData = new FormData();
    formData.append('image', file);
    return axios.post('/api/profiles/image', formData, header(token, {'Content-Type': 'multipart/form-data'}))
        .then(response => response.data);
}

export const sendMessage = (subject, message, sender, recipient, token) =>
    axios.post("/api/profiles/message/" + recipient, {subject, message, sender, recipient}, header(token))
        .then(response => response.data);