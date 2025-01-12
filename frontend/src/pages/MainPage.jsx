import React from 'react';
import Button from '@mui/material/Button';

const MainPage = ({ username, onLogout, onJoinGame }) => {
    return (
        <div className="min-h-screen flex flex-col items-center justify-center bg-gray-900 p-4">
            <h1 className="text-4xl font-bold text-white mb-4">Добро пожаловать, {username}!</h1>
            <p className="text-xl text-gray-300 mb-8">бла бла</p>
            <Button onClick={onJoinGame} variant="outline" className="mb-4">Зайти в игру</Button>
            <Button onClick={onLogout} variant="outline">Выйти</Button>
        </div>
    );
};

export default MainPage;
