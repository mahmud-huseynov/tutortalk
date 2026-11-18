import axios from "axios";
import TokenManager from "./TokenManager";

const UserRoleApi = {
    getUserRole: (userId) => axios.get(`http://localhost:8080/user-roles/${userId}`, {
        headers: { Authorization: `Bearer ${TokenManager.getAccessToken()}` }
    })
    .then(response => response.data)
    .catch(error => {
        console.error("Failed to fetch user role:", error);
        throw error;
    })
};

export default UserRoleApi;
