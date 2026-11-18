import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { useNavigate, useParams } from 'react-router-dom';
import styles from './SignupPage.module.css';
import LanguageApi from '../api/LanguageApi';

function SignupPage() {
    const { userType = 'learner' } = useParams(); // Default userType to 'learner'
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [email, setEmail] = useState('');
    const [number, setNumber] = useState('');
    const [languages, setLanguages] = useState([]);
    const [selectedLanguageIds, setSelectedLanguageIds] = useState([]);
    const [errors, setErrors] = useState({});
    const navigate = useNavigate();

    useEffect(() => {
        const fetchLanguages = async () => {
            console.log(userType);
            try {
                const data = await LanguageApi.getLanguages();
                setLanguages(data.languages);
            } catch (error) {
                console.error('Error fetching languages:', error);
            }
        };

        fetchLanguages();
    }, [userType]);

    const validateInputs = () => {
        const errors = {};
        if (!username.trim()) errors.username = 'Username is required';
        if (!password) errors.password = 'Password is required';
        if (!email) errors.email = 'Email is required';
        else if (!/\S+@\S+\.\S+/.test(email)) errors.email = 'Email is invalid';
        if (!number) errors.number = 'Number is required';
        else if (!/^\d+$/.test(number)) errors.number = 'Number is invalid';
        if (userType === 'tutor' && selectedLanguageIds.length === 0) errors.languages = 'At least one language must be selected';
        if (userType === 'learner' && !selectedLanguageIds[0]) errors.language = 'Language is required';
        return errors;
    };

    const handleSignup = async (e) => {
        e.preventDefault();
        const validationErrors = validateInputs();
        if (Object.keys(validationErrors).length > 0) {
            setErrors(validationErrors);
            return;
        }

        try {
            const endpoint = userType === 'tutor' ? 'http://localhost:8080/signup/tutor' : 'http://localhost:8080/signup/learner';
            const signupData = {
                username,
                password,
                email,
                number,
                ...(userType === 'tutor' && { languageIds: selectedLanguageIds.map(id => parseInt(id)) }),
                ...(userType === 'learner' && { languageId: parseInt(selectedLanguageIds[0]) }) // Assuming single selection for learner
            };
            const response = await axios.post(endpoint, signupData);
            console.log(response.data);
            navigate('/login');
        } catch (error) {
            console.error(error);
        }
    };

    const handleLanguageChange = (e) => {
        const options = e.target.options;
        const selectedOptions = [];
        for (let i = 0; i < options.length; i++) {
            if (options[i].selected) {
                selectedOptions.push(options[i].value);
            }
        }
        setSelectedLanguageIds(selectedOptions);
    };

    return (
        <form className={styles.formContainer} onSubmit={handleSignup}>
            <h1>Sign up as {userType.charAt(0).toUpperCase() + userType.slice(1)}</h1>
            <div className={styles.inputFieldContainer}>
                {errors.username && <p className={styles.errorText}>{errors.username}</p>}
                <input
                    className={styles.inputField}
                    type="text"
                    value={username}
                    onChange={(e) => setUsername(e.target.value)}
                    placeholder="Username"
                />
            </div>
            <div className={styles.inputFieldContainer}>
                {errors.password && <p className={styles.errorText}>{errors.password}</p>}
                <input
                    className={styles.inputField}
                    type="password"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    placeholder="Password"
                />
            </div>
            <div className={styles.inputFieldContainer}>
                {errors.email && <p className={styles.errorText}>{errors.email}</p>}
                <input
                    className={styles.inputField}
                    type="email"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                    placeholder="Email"
                />
            </div>
            <div className={styles.inputFieldContainer}>
                {errors.number && <p className={styles.errorText}>{errors.number}</p>}
                <input
                    className={styles.inputField}
                    type="text"
                    value={number}
                    onChange={(e) => setNumber(e.target.value)}
                    placeholder="Number"
                />
            </div>
            {userType === 'tutor' && (
                <div className={styles.inputFieldContainer}>
                    {errors.languages && <p className={styles.errorText}>{errors.languages}</p>}
                    <select
                        className={styles.dropdown}
                        multiple
                        value={selectedLanguageIds}
                        onChange={handleLanguageChange}
                    >
                        {languages.map(language => (
                            <option key={language.id} value={language.id}>{language.name}</option>
                        ))}
                    </select>
                </div>
            )}
            {userType === 'learner' && (
                <div className={styles.inputFieldContainer}>
                    {errors.language && <p className={styles.errorText}>{errors.language}</p>}
                    <select
                        className={styles.dropdown}
                        value={selectedLanguageIds[0] || ''}
                        onChange={(e) => setSelectedLanguageIds([e.target.value])}
                    >
                        <option value="" disabled>Select Language</option>
                        {languages.map(language => (
                            <option key={language.id} value={language.id}>{language.name}</option>
                        ))}
                    </select>
                </div>
            )}
            <button className={styles.button} type="submit">Sign Up</button>
        </form>
    );
}

export default SignupPage;
