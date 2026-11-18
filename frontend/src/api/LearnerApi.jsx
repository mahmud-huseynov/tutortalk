import axios from "axios";
import TokenManager from "./TokenManager";

const LearnerApi = {
    getLearner: (tutorId) => axios.get(`http://localhost:8080/learners/${learnerId}`, {
        headers: { Authorization: `Bearer ${TokenManager.getAccessToken()}` }
    }).then(response => response.data),
    getLearners: () => axios.get("http://localhost:8080/learners", {
        headers: { Authorization: `Bearer ${TokenManager.getAccessToken()}` }
    }).then((result) => result.data),
    createLearner: (newLearner) => axios.post("http://localhost:8080/learners", newLearner, {
        headers: { Authorization: `Bearer ${TokenManager.getAccessToken()}` }
    }),
    deleteLearner: (learnerId) => axios.delete(`http://localhost:8080/learners/${learnerId}`, {
        headers: { Authorization: `Bearer ${TokenManager.getAccessToken()}` }
    }),
    updateLearner: (id, updatedLearner) => axios.put(`http://localhost:8080/learners/${id}`, updatedLearner, {
        headers: { Authorization: `Bearer ${TokenManager.getAccessToken()}` }
    }),
    getLearnerCount: () => axios.get("http://localhost:8080/learners/count", {
        headers: { Authorization: `Bearer ${TokenManager.getAccessToken()}` }
    }).then(response => response.data)
};

export default LearnerApi;
