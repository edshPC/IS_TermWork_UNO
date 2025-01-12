import React from 'react';
import { useNavigate } from 'react-router-dom';
import Button from '@mui/material/Button';

const LandingPage = () => {
    const navigate = useNavigate();

    return (
        <div className="min-h-screen flex flex-col items-center justify-center bg-blue-500 text-white p-4">
            <h1 className="text-6xl font-bold mb-8">UNO GAME</h1>
            <div className="space-y-4 w-full max-w-md">
                <Button
                    onClick={() => navigate('/login')}
                    className="w-full py-3 bg-blue-700 hover:bg-blue-600 text-lg font-semibold rounded"
                    sx={{ color: 'white' }}
                >
                    Войти
                </Button>
                <Button
                    onClick={() => navigate('/register')}
                    className="w-full py-3 bg-white hover:bg-blue-600 text-blue-700 text-lg font-semibold rounded border border-blue-700"
                    sx={{ color: 'white' }}
                >
                    Зарегистрироваться
                </Button>
            </div>
        </div>
    );
};

export default LandingPage;
