import React from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { useNavigate } from 'react-router-dom';
import Button from '@mui/material/Button';
import { logout } from '../storage/authSlice';

const MainPage = () => {
    const username = useSelector((state) => state.auth.username);
    const dispatch = useDispatch();
    const navigate = useNavigate();

    const handleLogout = () => {
        dispatch(logout());
        navigate('/');
    };

    const handleJoinGame = () => {
        navigate('/game');
    };

    return (
        <div className="min-h-screen flex flex-col items-center justify-center bg-gray-900 p-4">
            <h1 className="text-4xl font-bold text-white mb-4">Добро пожаловать, {username}!</h1>
            <p className="text-xl text-gray-300 mb-8">бла бла</p>
            <Button onClick={handleJoinGame} variant="outline" className="mb-4">Зайти в игру</Button>
            <Button onClick={handleLogout} variant="outline">Выйти</Button>
        </div>
    );
};

export default MainPage;
