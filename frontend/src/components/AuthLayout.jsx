import { Outlet, useNavigate, useLocation } from 'react-router-dom';
import { useEffect, useState } from 'react';
import TokenManager from '../api/TokenManager';

export default function AuthLayout() {
    const navigate = useNavigate();
    const location = useLocation();
    const [isLoading, setIsLoading] = useState(true);
    const [isAuthenticated, setIsAuthenticated] = useState(false);

    useEffect(() => {
        const checkAuth = async () => {
            try {
                if (!TokenManager.getAccessToken() || TokenManager.isAccessTokenExpired()) {
                    const refreshed = await TokenManager.getRefreshToken();
                    if (!refreshed) {
                        // Check if the current route is "find-tutor"
                        if (location.pathname === '/find-tutor') {
                            navigate('/signup/learner');
                        } else {
                            navigate('/login');
                        }
                        return;
                    } else {
                        await TokenManager.refreshAccessToken(refreshed);
                    }
                }
                setIsAuthenticated(true);
            } catch (error) {
                console.error('Error checking authentication:', error);
                navigate('/login');
                return;
            } finally {
                setIsLoading(false);
            }
        };

        checkAuth();
    }, [navigate, location.pathname]);

    if (isLoading) {
        return <div>Loading...</div>;
    }

    return isAuthenticated ? <Outlet /> : null;
}
