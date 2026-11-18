import React from 'react';
import styles from './NavBar.module.css'; 
import { NavLink, useLocation } from "react-router-dom";
import TokenManager from '../api/TokenManager';

export default function NavBar() {
    const location = useLocation();

    const isLoggedIn = TokenManager.getAccessToken();

    const handleLogout = () => {
        TokenManager.clear();
        window.location.reload();
    }

    const links = [
        {
            id: 1,
            path: "/find-tutor",
            text: "Find a tutor"
        },
        {
            id: 3,
            path: "/about",
            text: "About"
        },
        {
            id: 4,
            path: "/contact",
            text: "Contact",
            className: styles.right
        }
    ];

    // Check if the user is a tutor or learner
    let isTutor = false;
    let isLearner = false;
    const claims = TokenManager.getClaims();
    if (claims && Array.isArray(claims.roles)) {
        isTutor = claims.roles.includes('TUTOR');
        isLearner = claims.roles.includes('LEARNER');
    }

    // Conditionally add the "Chat" link if the user is a tutor or learner
    if (isTutor || isLearner) {
        links.splice(1, 0, {
            id: 2,
            path: "/chat",
            text: "Chat"
        });
    }

    // Conditionally add the "Become a tutor" link if the user is not logged in or is a learner
    if (!isLoggedIn || isLearner) {
        links.splice(1, 0, {
            id: 2,
            path: "/signup/tutor",
            text: "Become a tutor"
        });
    }

    return (
        <header className={styles.header}>
            <NavLink to="/">
                <img className={styles.logo} src="/TutorTalkIcon.png" alt="logo" />
            </NavLink>
            <nav>
                <ul className={styles.nav__links}>
                    {links.map(link => (
                        <li key={link.id}>
                            <NavLink to={link.path} className={({ isActive }) => isActive ? styles.active : ''}>
                                {link.text}
                            </NavLink>
                        </li>
                    ))}
                </ul>
            </nav>
            {isLoggedIn ? (
                <button onClick={handleLogout} className={styles.button}>Logout</button>
            ) : (
                <NavLink to="/login">
                    <button className={styles.button}>Login</button>
                </NavLink>
            )}
        </header>
    );
}
