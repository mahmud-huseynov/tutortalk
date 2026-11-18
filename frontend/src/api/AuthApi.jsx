import axios from "axios";
import TokenManager from "./TokenManager";

const AuthAPI = {
    login: (username, password) => axios.post('http://localhost:8080/tokens', { username, password })
        .then(response => {
            const { accessToken, refreshToken } = response.data;
            TokenManager.setTokens(accessToken, refreshToken);
            return accessToken;
        })
        .catch(error => {
            console.error("Login failed:", error);
            throw error;
        })
}



export default AuthAPI;
