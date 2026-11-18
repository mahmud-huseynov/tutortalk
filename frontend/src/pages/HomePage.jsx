import React, { useState, useEffect } from 'react';
import LanguageApi from '../api/LanguageApi';
import styles from './HomePage.module.css';

export default function HomePage() {
    const [languages, setLanguages] = useState([]);

    useEffect(() => {
        LanguageApi.getLanguages()
            .then(data => {
                setLanguages(data.languages);
            })
            .catch(error => {
                console.error('Error fetching languages:', error);
            });
    }, []);

    return (
        <div className={styles.container}>
            <header className={styles.header}>
                <div className={styles.headerContent}>
                    <div>
                        <h1 className={styles.h1}>Welcome to TutorTalk</h1>
                        <p>Unlocking knowledge, one conversation at a time.</p>
                    </div>
                    <img src="/TutorLanding.webp" alt="Placeholder" className={styles.headerImage} />
                </div>
            </header>
            <section className={styles.section}>
                <p className={styles.exploreLanguages}>Explore our languages!</p>
                <ul className={styles.languageList}>
                    {languages.map((language) => (
                        <li key={language.id} className={styles.languageItem}>
                            {language.name}
                        </li>
                    ))}
                </ul>
            </section>
        </div>
    );
}
