import React from 'react';
import Button from '@mui/material/Button';

const LandingPage = ({ onNavigateLogin, onNavigateRegister }) => {
    return (
        <div className="min-h-screen flex flex-col items-center justify-center bg-blue-500 text-white p-4">
            <h1 className="text-6xl font-bold mb-8">UNO GAME</h1>
            <div className="space-y-4 w-full max-w-md">
                <Button
                    onClick={onNavigateLogin}
                    className="w-full py-3 bg-blue-700 hover:bg-blue-600 text-lg font-semibold rounded"
                >
                    Войти
                </Button>
                <Button
                    onClick={onNavigateRegister}
                    className="w-full py-3 bg-white hover:bg-gray-100 text-blue-700 text-lg font-semibold rounded border border-blue-700"
                >
                    Зарегистрироваться
                </Button>
            </div>
        </div>
    );
};

export default LandingPage;
