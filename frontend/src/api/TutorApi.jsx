// TutorApi.js
import axios from "axios";
import TokenManager from "./TokenManager";

const TutorApi = {
    getTutor: (tutorId) => axios.get(`http://localhost:8080/tutors/${tutorId}`, {
        headers: { Authorization: `Bearer ${TokenManager.getAccessToken()}` }
    }).then(response => response.data),
    getTutors: () => axios.get("http://localhost:8080/tutors", {
        headers: { Authorization: `Bearer ${TokenManager.getAccessToken()}` }
    }).then((result) => result.data),
    createTutor: (newTutor) => axios.post("http://localhost:8080/tutors", newTutor, {
        headers: { Authorization: `Bearer ${TokenManager.getAccessToken()}` }
    }),
    deleteTutor: (tutorId) => axios.delete(`http://localhost:8080/tutors/${tutorId}`, {
        headers: { Authorization: `Bearer ${TokenManager.getAccessToken()}` }
    }),
    updateTutor: (id, updatedTutor) => axios.put(`http://localhost:8080/tutors/${id}`, updatedTutor, {
        headers: { Authorization: `Bearer ${TokenManager.getAccessToken()}` }
    }),
    getTutorLearners: () => axios.get("http://localhost:8080/tutors/learners", {
        headers: { Authorization: `Bearer ${TokenManager.getAccessToken()}` }
    }).then((result) => result.data),
    getMessages: (username) => {
        return axios.get(`http://localhost:8080/tutors/${username}/messages`, {
            headers: { Authorization: `Bearer ${TokenManager.getAccessToken()}` }
        }).then(response => response.data);
    },
    getUserIdByUsername: (username) => {
        return axios.get(`http://localhost:8080/tutors/users/${username}/id`, {
            headers: { Authorization: `Bearer ${TokenManager.getAccessToken()}` }
        }).then(response => response.data);
    },
};

export default TutorApi;
