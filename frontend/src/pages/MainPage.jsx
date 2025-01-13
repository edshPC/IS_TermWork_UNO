import React from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { useNavigate } from 'react-router-dom';
import Button from '@mui/material/Button';
import {logout, useAuthCheck} from '../storage/authSlice';

const MainPage = () => {
    useAuthCheck();
    const username = useSelector((state) => state.auth.username);
    const dispatch = useDispatch();
    const navigate = useNavigate();

    const handleLogout = () => {
        dispatch(logout());
        navigate('/');
    };

    const handleCreateRoom = () => {
        navigate('/create-room');
    };

    const handleJoinRoom = () => {
        navigate('/join-room');
    };

    const handleViewStatistics = () => {
        navigate('/statistics');
    };

    const handleViewAchievements = () => {
        navigate('/achievements');
    };

    return (
        <div className="min-h-screen flex flex-col bg-gray-900 p-4">
            <h1 className="text-4xl font-bold text-white text-center mb-4">Добро пожаловать, {username}!</h1>
            <div className="flex-grow flex justify-center items-center">
                <div className="grid grid-cols-2 gap-8">
                    <Button
                        variant="contained"
                        className="w-64 h-32 bg-blue-500 hover:bg-blue-700 text-white font-bold rounded"
                        onClick={handleCreateRoom}                    
                    >
                        Создать игровую комнату
                    </Button>
                    <Button
                        variant="contained"
                        className="w-64 h-32 bg-blue-500 hover:bg-blue-700 text-white font-bold rounded"
                        onClick={handleJoinRoom}
                    >
                        Присоединиться к игровой комнате
                    </Button>
                    <Button
                        variant="contained"
                        className="w-64 h-32 bg-blue-500 hover:bg-blue-700 text-white font-bold rounded"
                        onClick={handleViewStatistics}
                    >
                        Посмотреть статистику
                    </Button>
                    <Button
                        variant="contained"
                        className="w-64 h-32 bg-blue-500 hover:bg-blue-700 text-white font-bold rounded"
                        onClick={handleViewAchievements}
                    >
                        Посмотреть достижения
                    </Button>
                </div>
            </div>
            <div className="flex-grow flex justify-center items-center">
                <Button
                    onClick={handleLogout}
                    variant="outline"
                    className="w-40 h-12 bg-transparent hover:bg-gray-700 text-white font-bold py-2 px-4 rounded text-center mt-auto"
                >
                    Выйти
                </Button>
            </div>
        </div>
    );
};

export default MainPage;
