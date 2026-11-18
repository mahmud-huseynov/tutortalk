import axios from "axios";
import { jwtDecode } from "jwt-decode";

let claims = null;
let accessToken = null;

const TokenManager = {
    getAccessToken: () => localStorage.getItem('accessToken'),
    getRefreshToken: () => localStorage.getItem('refreshToken'),
    getClaims: () => {
        const accessToken = TokenManager.getAccessToken();
        if (accessToken) {
            return jwtDecode(accessToken);
        }
        return null;
    },
    setTokens: (access, refresh) => {
        localStorage.setItem('accessToken', access);
        localStorage.setItem('refreshToken', refresh);
    },
    setAccessToken: (token) => {
        localStorage.setItem('accessToken', token);
        accessToken = token;
        claims = jwtDecode(token);
    },
    decodeClaims: () => {
        const accessToken = TokenManager.getAccessToken();
        if (accessToken) {
            return jwtDecode(accessToken);
        }
        return null;
    },
    isAccessTokenExpired: () => {
        const accessToken = TokenManager.getAccessToken();
        if (!accessToken) {
            return true;
        }
        const { exp } = jwtDecode(accessToken);
        return Date.now() >= exp * 1000;
    },
    isRefreshTokenExpired: async () => {
        const refreshToken = TokenManager.getRefreshToken();
        if (!refreshToken) {
            return true;
        }
        const isValid = await checkRefreshTokenValidity(refreshToken);
        return !isValid;    
    },
    refreshAccessToken: async (refreshToken) => {
        console.log(refreshToken)
        try {
            const response = await axios.post(
                'http://localhost:8080/refresh_tokens/refresh',
                null,
                { params: { refreshToken: refreshToken } }
            );
            console.log(response.data.isValid)
            const newAccessToken = response.data.accessToken;
            
            TokenManager.setAccessToken(newAccessToken);
            return true;
        } catch (error) {
            console.error('Error refreshing access token:', error);
            return false;
        }
    },
    isUserAdmin: () => {
        const claims = TokenManager.getClaims();
        return claims && claims.roles && claims.roles.includes('ADMIN');
    },
    isUserTutor: () => {
        const claims = TokenManager.getClaims();
        return claims && claims.roles && claims.roles.includes('TUTOR');
    },
    isUserLearner: () => {
        const claims = TokenManager.getClaims();
        return claims && claims.roles && claims.roles.includes('LEARNER');
    },
    getUsername: () => {
        const claims = TokenManager.getClaims();
        return claims ? claims.sub : null;
    },
    clear: () => {
        localStorage.removeItem('accessToken');
        localStorage.removeItem('refreshToken');
    }
};

const checkRefreshTokenValidity = async (refreshToken) => {
    try {
        const accessToken = TokenManager.getAccessToken();
        if (!accessToken) {
            console.log('Access token not found.');
        }
        const response = await axios.post(
            'http://localhost:8080/refresh_tokens/validate',
            null, 
            { params: { refreshToken: refreshToken } }  
        );
        return response.data.isValid;
    } catch (error) {
        console.error('Error checking refresh token validity:', error);
        return false; 
    }
};

export default TokenManager;
