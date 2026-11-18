import React, { useState } from 'react';
import styles from './LoginForm.module.css'; 

function LoginForm(props) {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [errors, setErrors] = useState({});

    const handleSubmit = (event) => {
        event.preventDefault();
        const validationErrors = validateInputs();
        if (Object.keys(validationErrors).length === 0) {
            props.onLogin(username, password);
        } else {
            setErrors(validationErrors);
        }
    }

    const validateInputs = () => {
        const errors = {};
        if (!username.trim()) errors.username = 'Username is required';
        if (!password) errors.password = 'Password is required';
        return errors;
    };

    return (
        <form className={styles.formContainer} onSubmit={handleSubmit}>
            <h1>Log in</h1>
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
            <button className={styles.button} type="submit">Login</button>
        </form>
    );
}

export default LoginForm;
