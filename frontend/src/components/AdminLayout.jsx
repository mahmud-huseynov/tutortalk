// File path: src/components/AdminLayout.jsx
import { Navigate, Outlet, useNavigate } from 'react-router-dom';
import { useEffect, useState } from 'react';
import TokenManager from '../api/TokenManager';

export default function AdminLayout() {
    const navigate = useNavigate();
    const [isLoading, setIsLoading] = useState(true);
    const [isAdmin, setIsAdmin] = useState(false);

    useEffect(() => {
        const checkAuth = async () => {
            try {
                if (!TokenManager.getAccessToken() || TokenManager.isAccessTokenExpired()) {
                    const refreshed = await TokenManager.getRefreshToken();
                    if (!refreshed) {
                        navigate('/login');
                        return;
                    } else {
                        await TokenManager.refreshAccessToken(refreshed);
                    }
                }
                const isAdminUser = await TokenManager.isUserAdmin();
                setIsAdmin(isAdminUser);
            } catch (error) {
                console.error('Error checking authentication:', error);
                navigate('/login');
                return;
            } finally {
                setIsLoading(false);
            }
        };

        checkAuth();    
    }, [navigate]);

    if (isLoading) {
        return <div>Loading...</div>;
    }

    return isAdmin ? <Outlet /> : <Navigate to="/" replace />;
}
