// File: src/App.js
import React from 'react';
import { Route, Routes, BrowserRouter as Router } from "react-router-dom";
import NavBar from './components/NavBar';
import AuthLayout from './components/AuthLayout';
import AdminLayout from './components/AdminLayout';
import HomePage from './pages/HomePage';
import LoginPage from './pages/LoginPage';
import DashboardPage from './pages/DashboardPage';
import Footer from './components/Footer';
import FindTutorPage from './pages/FindTutorPage';
import AboutPage from './pages/AboutPage';
import SignupPage from './pages/SignupPage';
import ChatRoom from './components/ChatRoom';

function App() {
  return (
    <div className="App">
      <Router>
        <NavBar />
        <Routes>
          <Route path="/" element={<HomePage />} />
          <Route path="/login" element={<LoginPage />} />
          <Route path="/about" element={<AboutPage />} />
          <Route path="/signup/:userType" element={<SignupPage />} />
          <Route element={<AuthLayout />}>
            <Route path="/find-tutor" element={<FindTutorPage />} />
          </Route>
          <Route element={<AdminLayout />}>
            <Route path="/dashboard" element={<DashboardPage />} />
          </Route>
          <Route element={<AuthLayout />}>
            <Route path="/chat" element={<ChatRoom />} />
          </Route>
        </Routes>
        <Footer />
      </Router>
    </div>
  );
}

export default App;
