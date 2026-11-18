import React, { useState, useEffect } from 'react';
import LoginForm from '../components/LoginForm';
import TokenManager from '../api/TokenManager';
import AuthAPI from '../api/AuthApi';
import TutorApi from '../api/TutorApi';
import { useNavigate } from 'react-router-dom'; 

export default function LoginPage() {
  const navigate = useNavigate();
  const [claims, setClaims] = useState(TokenManager.getClaims());
  const [tutorDetails, setTutorDetails] = useState(null);

  const handleLogin = (username, password) => {
    AuthAPI.login(username, password)
      .then(accessToken => {
        TokenManager.setAccessToken(accessToken);
        setClaims(TokenManager.getClaims());
        navigate('/dashboard');
      })      
      .then(getTutorDetails)
      .catch(error => console.error(error));
  }

  const getTutorDetails = () => {
    const claims = TokenManager.getClaims();
    if (claims?.roles?.includes('TUTOR') && claims?.tutorId) {
      TutorApi.getTutor(claims.studentId)
        .then(tutor => setTutorDetails(tutor))
        .catch(error => console.error(error));
    }
  }

  const handleLogout = () => {
    TokenManager.clear();
    setClaims(null);
    setTutorDetails(null);
  }

  useEffect(() => {
    const accessTokenExists = TokenManager.getAccessToken();
    
    if (!accessTokenExists) {
      handleLogout();
      return;
    }
  
    const checkRefreshToken = async () => {
      const refreshTokenExpired = await TokenManager.isRefreshTokenExpired();
      
      if (refreshTokenExpired) {
        const refreshed = await TokenManager.refreshAccessToken();
        if (refreshed) {
          setClaims(TokenManager.getClaims());
          return;
        } else {
          handleLogout(); 
          return;
        }
      }
  
      const currentClaims = TokenManager.getClaims();
      
      if (currentClaims) {
        navigate('/dashboard');
      }
    };
  
    checkRefreshToken(); 
  
  }, [navigate]); 

  return (
    <div>
      {claims ? (
        <div>
          <p>Welcome, {claims.sub}</p>
          <button onClick={handleLogout}>Logout</button>
          <br />
          <a href='/' target='_blank'>Open in new Tab</a>
        </div>
      ) : (
        <LoginForm onLogin={handleLogin} />
      )}
    </div>
  );
}
