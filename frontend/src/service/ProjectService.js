import axios from "axios";

const header = (token) =>({
    headers: {
        Authorization: `Bearer ${token}`
    }
});

export const getProjects = (token) =>
    axios.get("/api/projects", header(token)).then(response => response.data);

export const addProject = (title, description, token) =>
    axios.post("/api/projects", {title, description}, header(token))
        .then(response => response.data);