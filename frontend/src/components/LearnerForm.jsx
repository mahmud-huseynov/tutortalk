// src/components/LearnerForm.js
import React from 'react';
import styles from './LearnerForm.module.css';

const LearnerForm = ({ newLearnerName, setNewLearnerName, addLearner }) => (
    <section className={styles.section}>
        <h2>Add New Learner</h2>
        <div>
            <input
                type="text"
                value={newLearnerName}
                onChange={e => setNewLearnerName(e.target.value)}
                placeholder="Learner Name"
                className={styles.input}
            />
        </div>
        <div className={styles.buttonContainer}>
            <button onClick={addLearner}>Add</button>
        </div>
    </section>
);

export default LearnerForm;
