// src/components/AboutPage.js
import React from 'react';
import styles from './AboutPage.module.css';

export default function AboutPage() {
  return (
    <div className={styles.container}>
      <header className={styles.header}>
        <h1>We are for learners. It's as simple as that.</h1>
        <p>
          We believe that language learners shouldn't have to sweat it when they struggle 
          picking up a language.
          Providing an unmatched technology-driven
          customer experience is what we're all about.
        </p>
      </header>
      <section className={styles.valuesSection}>
        <h1>Our values</h1>
        <div className={styles.valuesContainer}>
          <div className={styles.valueBox}>
            <h2>Innovation</h2>
            <p>We constantly innovate to provide the best learning experience.</p>
          </div>
          <div className={styles.valueBox}>
            <h2>Commitment</h2>
            <p>We are committed to helping you succeed in your language learning journey.</p>
          </div>
          <div className={styles.valueBox}>
            <h2>Integrity</h2>
            <p>We uphold the highest standards of integrity in all our actions.</p>
          </div>
          <div className={styles.valueBox}>
            <h2>Excellence</h2>
            <p>We strive for excellence in everything we do.</p>
          </div>
        </div>
      </section>
    </div>
  );
}
