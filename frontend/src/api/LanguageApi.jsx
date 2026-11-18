import axios from "axios";
import TokenManager from "./TokenManager";

const LanguageApi = {
    getLanguages: () => axios.get("http://localhost:8080/languages")
        .then((result) => result.data),
    createLanguage: (newLanguage) => axios.post("http://localhost:8080/languages", newLanguage, {
        headers: { Authorization: `Bearer ${TokenManager.getAccessToken()}` }
    }),
    deleteLanguage: (languageId) => axios.delete(`http://localhost:8080/languages/${languageId}`, {
        headers: { Authorization: `Bearer ${TokenManager.getAccessToken()}` }
    }),
    updateLanguage: (id, updatedLanguage) => axios.put(`http://localhost:8080/languages/${id}`, updatedLanguage, {
        headers: { Authorization: `Bearer ${TokenManager.getAccessToken()}` }
    })
};

export default LanguageApi;
