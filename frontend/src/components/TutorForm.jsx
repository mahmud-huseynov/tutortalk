// src/components/TutorForm.js
import React from 'react';
import styles from './TutorForm.module.css';

const TutorForm = ({ newTutorName, setNewTutorName, selectedLanguageIds, setSelectedLanguageIds, languages, addTutor }) => (
    <section className={styles.section}>
        <h2>Add New Tutor</h2>
        <div>
            <input
                type="text"
                value={newTutorName}
                onChange={e => setNewTutorName(e.target.value)}
                placeholder="Tutor Name"
                className={styles.input}
            />
        </div>
        <div className={styles.dropdownContainer}>
            <select
                multiple
                value={selectedLanguageIds}
                onChange={e => setSelectedLanguageIds(Array.from(e.target.selectedOptions, option => parseInt(option.value)))}
                className={styles.dropdown}
            >
                {languages.map(language => (
                    <option key={language.id} value={language.id} className={styles.dropdownOption}>
                        {language.name}
                    </option>
                ))}
            </select>
        </div>
        <div className={styles.buttonContainer}>
            <button onClick={addTutor}>Add</button>
        </div>
    </section>
);

export default TutorForm;
