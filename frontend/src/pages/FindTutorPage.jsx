// FindTutorPage.js
import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import styles from './FindTutorPage.module.css';
import TutorApi from '../api/TutorApi';
import TokenManager from '../api/TokenManager';

export default function FindTutorPage() {
    const [tutors, setTutors] = useState([]);
    const username = TokenManager.getUsername();
    const navigate = useNavigate();

    useEffect(() => {
        fetchTutors();
    }, []);

    const fetchTutors = () => {
        TutorApi.getTutors()
            .then(data => {
                setTutors(data.tutors);
            })
            .catch(error => {
                console.error('Error fetching tutors:', error);
            });
    };

    const handleChat = (tutor) => {
        navigate('/chat', { state: { username, recipient: { name: tutor.name, role: 'Tutor' } } });
    };

    return (
        <div className={styles.container}>
            <header className={styles.header}>
                <h1>Private tutors that fit your schedule</h1>
                <p>Find your perfect private tutor and arrange a Free Video Meeting.</p>
            </header>
            <section>
                <div>
                    {tutors.map(tutor => (
                        <div key={tutor.id} className={styles.tutorList}>
                            <h2>{tutor.name}</h2>
                            <p>{tutor.languages}</p>
                            <p>{tutor.email}</p>
                            <p>{tutor.number}</p>
                            <button onClick={() => handleChat(tutor)}>Chat</button>
                        </div>
                    ))}
                </div>
            </section>
        </div>
    );
}
