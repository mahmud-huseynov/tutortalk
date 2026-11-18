// LanguageList.js
import React from 'react';
import styles from './LanguageList.module.css';

const LanguageList = ({ languages, updatingLanguage, setUpdatingLanguage, updateLanguage, deleteLanguage, handleLanguageChange }) => (
    <ul className={styles.languageList}>
        {languages.map(language => (
            <li key={language.id} className={styles.languageItem}>
                {updatingLanguage === language.id ? (
                    <div>
                        <input
                            type="text"
                            value={language.name}
                            onChange={e => handleLanguageChange(language.id, 'name', e.target.value)}
                        />
                        <input
                            type="text"
                            value={language.code}
                            onChange={e => handleLanguageChange(language.id, 'code', e.target.value)}
                        />
                        <button
                            onClick={() => updateLanguage(language.id, language.name, language.code)}
                        >
                            Save
                        </button>
                        <button
                            onClick={() => setUpdatingLanguage(null)}
                        >
                            Cancel
                        </button>
                    </div>
                ) : (
                    <div>
                        {language.name} ({language.code})
                        <button className={styles.button} onClick={() => setUpdatingLanguage(language.id)}>
                            Edit
                        </button>
                        <button className={styles.button} onClick={() => deleteLanguage(language.id)}>
                            Delete
                        </button>
                    </div>
                )}
            </li>
        ))}
    </ul>
);

export default LanguageList;
