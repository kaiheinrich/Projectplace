import axios from "axios";

const header = (token, options = {}) =>({
    headers: {
        Authorization: `Bearer ${token}`,
        ...options
    }
});

export const getProjects = (token) =>
    axios.get("/api/projects", header(token)).then(response => response.data);

export const addProject = (title, description, imageName, token) =>
    axios.post("/api/projects", {title, description, imageName}, header(token))
        .then(response => response.data);

export const updateProject = (id, title, description, imageName, token) =>
    axios.put("/api/projects/" + id, {title, description, imageName}, header(token))
        .then(response => response.data);

export const uploadProjectImage = (file, token) => {
    const formData = new FormData();
    formData.append("image", file);
    return axios.post("/api/projects/image", formData, header(token, {'Content-Type': 'multipart/form-data'}))
        .then(response => response.data);
}