// src/components/LanguageForm.js
import React from 'react';
import styles from './LanguageForm.module.css';

const LanguageForm = ({ newLanguageName, setNewLanguageName, newLanguageCode, setNewLanguageCode, addLanguage }) => (
    <section className={styles.section}>
        <h2>Add New Language</h2>
        <div>
            <input
                type="text"
                value={newLanguageName}
                onChange={e => setNewLanguageName(e.target.value)}
                placeholder="Language Name"
                className={styles.input}
            />
        </div>
        <div>
            <input
                type="text"
                value={newLanguageCode}
                onChange={e => setNewLanguageCode(e.target.value)}
                placeholder="Language Code"
                className={styles.input}
            />
        </div>
        <div className={styles.buttonContainer}>
            <button onClick={addLanguage}>Add</button>
        </div>
    </section>
);

export default LanguageForm;
