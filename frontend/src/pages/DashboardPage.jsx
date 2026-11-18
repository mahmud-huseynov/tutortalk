// DashboardPage.js
import React, { useState, useEffect } from 'react';
import LanguageApi from '../api/LanguageApi';
import TutorApi from '../api/TutorApi';
import LearnerApi from '../api/LearnerApi';
import LanguageForm from '../components/LanguageForm';
import TutorForm from '../components/TutorForm';
import LanguageList from '../components/LanguageList';
import styles from './DashBoardPage.module.css';

export default function DashboardPage() {
    const [languages, setLanguages] = useState([]);
    const [newLanguageName, setNewLanguageName] = useState('');
    const [newLanguageCode, setNewLanguageCode] = useState('');
    const [updatingLanguage, setUpdatingLanguage] = useState(null);

    const [tutors, setTutors] = useState([]);
    const [newTutorName, setNewTutorName] = useState('');
    const [selectedLanguageIds, setSelectedLanguageIds] = useState([]);
    const [updatingTutor, setUpdatingTutor] = useState(null);
    const [tutorDetails, setTutorDetails] = useState(null);

    const [learnerCount, setLearnerCount] = useState(0);

    useEffect(() => {
        fetchLanguages();
        fetchTutors();
        fetchLearnerCount();
        return () => {
            setTutorDetails(null);
        };
    }, []);

    const fetchLanguages = () => {
        LanguageApi.getLanguages()
            .then(data => setLanguages(data.languages))
            .catch(error => console.error('Error fetching languages:', error));
    };

    const fetchTutors = () => {
        TutorApi.getTutors()
            .then(data => setTutors(data.tutors))
            .catch(error => console.error('Error fetching tutors:', error));
    };

    const fetchLearnerCount = () => {
        LearnerApi.getLearnerCount()
            .then(data => setLearnerCount(data.count))
            .catch(error => console.error('Error fetching learner count:', error));
    };

    const addLanguage = () => {
        LanguageApi.createLanguage({ name: newLanguageName, code: newLanguageCode })
            .then(() => {
                fetchLanguages();
                setNewLanguageName('');
                setNewLanguageCode('');
            })
            .catch(error => console.error('Error adding language:', error));
    };

    const addTutor = () => {
        const newTutorData = { name: newTutorName, languageIds: selectedLanguageIds };
        TutorApi.createTutor(newTutorData)
            .then(() => {
                fetchTutors();
                setNewTutorName('');
                setSelectedLanguageIds([]);
            })
            .catch(error => console.error('Error adding tutor:', error));
    };

    const deleteLanguage = id => {
        if (window.confirm('Are you sure you want to delete this language?')) {
            LanguageApi.deleteLanguage(id)
                .then(() => fetchLanguages())
                .catch(error => console.error('Error deleting language:', error));
        }
    };

    const deleteTutor = id => {
        TutorApi.deleteTutor(id)
            .then(() => fetchTutors())
            .catch(error => console.error('Error deleting tutor:', error));
    };

    const updateLanguage = (id, newName, newCode) => {
        LanguageApi.updateLanguage(id, { name: newName, code: newCode })
            .then(() => {
                fetchLanguages();
                setUpdatingLanguage(null);
            })
            .catch(error => console.error('Error updating language:', error));
    };

    const updateTutor = (id, newName, newLanguage) => {
        TutorApi.updateTutor(id, { name: newName, language: newLanguage })
            .then(() => {
                fetchTutors();
                setUpdatingTutor(null);
            })
            .catch(error => console.error('Error updating tutor:', error));
    };

    const handleLanguageChange = (id, field, value) => {
        setLanguages(prevLanguages =>
            prevLanguages.map(language =>
                language.id === id ? { ...language, [field]: value } : language
            )
        );
    };

    return (
        <div>
            <div className={styles.container}>
                <header className={styles.header}>
                    <div className={styles.headerContent}>
                        <div>
                            <h1 className={styles.h1}>Dashboard</h1>
                            <p>View, create, update, and delete languages.</p>
                            <p>Number of learners: {learnerCount}</p>
                        </div>
                        <LanguageList
                            languages={languages}
                            updatingLanguage={updatingLanguage}
                            setUpdatingLanguage={setUpdatingLanguage}
                            updateLanguage={updateLanguage}
                            deleteLanguage={deleteLanguage}
                            handleLanguageChange={handleLanguageChange}
                        />
                    </div>
                </header>
                <LanguageForm
                    newLanguageName={newLanguageName}
                    setNewLanguageName={setNewLanguageName}
                    newLanguageCode={newLanguageCode}
                    setNewLanguageCode={setNewLanguageCode}
                    addLanguage={addLanguage}
                />
                <TutorForm
                    newTutorName={newTutorName}
                    setNewTutorName={setNewTutorName}
                    selectedLanguageIds={selectedLanguageIds}
                    setSelectedLanguageIds={setSelectedLanguageIds}
                    languages={languages}
                    addTutor={addTutor}
                />
            </div>
        </div>
    );
}
