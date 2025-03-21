import React, { useEffect, useState, useMemo } from 'react';
import { useSelector } from 'react-redux';
import { useNavigate } from 'react-router-dom';
import Button from '@mui/material/Button';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Paper from '@mui/material/Paper';
import TableSortLabel from '@mui/material/TableSortLabel';
import {useAuthCheck} from "../storage/authSlice.jsx";
import {origin} from "../App.jsx";

const StatisticsPage = () => {
    useAuthCheck();
    const [statistics, setStatistics] = useState([]);
    const [error, setError] = useState('');
    const [sortConfig, setSortConfig] = useState({ key: 'rating', direction: 'desc' });
    const token = useSelector((state) => state.auth.token) || localStorage.getItem('token');
    const navigate = useNavigate();

    useEffect(() => {
        const fetchStatistics = async () => {
            try {
                const response = await fetch(origin + '/api/statistics', {
                    method: 'GET',
                    headers: {
                        'Authorization': `Bearer ${token}`,
                    },
                });
                if (response.ok) {
                    const {data} = await response.json();
                    setStatistics(data);
                } else {
                    const errorData = await response.json();
                    setError(errorData.message || 'Ошибка при загрузке статистики');
                }
            } catch (err) {
                setError('Ошибка при загрузке статистики');
            }
        };

        fetchStatistics();
    }, [token]);

    const handleLogout = () => {
        navigate('/main');
    };

    const handleSortRequest = (key) => {
        let direction = 'asc';
        if (sortConfig.key === key && sortConfig.direction === 'asc') {
            direction = 'desc';
        }
        setSortConfig({ key, direction });
    };

    const sortedStatistics = useMemo(() => {
        let sortableStats = [...statistics];
        if (sortConfig) {
            sortableStats.sort((a, b) => {
                if (a[sortConfig.key] < b[sortConfig.key]) {
                    return sortConfig.direction === 'asc' ? -1 : 1;
                }
                if (a[sortConfig.key] > b[sortConfig.key]) {
                    return sortConfig.direction === 'asc' ? 1 : -1;
                }
                return 0;
            });
        }
        return sortableStats;
    }, [statistics, sortConfig]);

    return (
        <div className="min-h-screen flex flex-col items-center bg-gray-900 p-4">
            <h1 className="text-3xl font-bold text-white mb-4">Статистика игроков</h1>
            <TableContainer component={Paper} className="w-full max-w-4xl">
                <Table>
                    <TableHead>
                        <TableRow>
                            <TableCell>Имя пользователя</TableCell>
                            <TableCell align="center">
                                <TableSortLabel
                                    active={sortConfig.key === 'rating'}
                                    direction={sortConfig.key === 'rating' ? sortConfig.direction : 'asc'}
                                    onClick={() => handleSortRequest('rating')}
                                >
                                    Рейтинг
                                </TableSortLabel>
                            </TableCell>
                            <TableCell align="center">
                                <TableSortLabel
                                    active={sortConfig.key === 'playCount'}
                                    direction={sortConfig.key === 'playCount' ? sortConfig.direction : 'asc'}
                                    onClick={() => handleSortRequest('playCount')}
                                >
                                    Количество игр
                                </TableSortLabel>
                            </TableCell>
                            <TableCell align="center">
                                <TableSortLabel
                                    active={sortConfig.key === 'winCount'}
                                    direction={sortConfig.key === 'winCount' ? sortConfig.direction : 'asc'}
                                    onClick={() => handleSortRequest('winCount')}
                                >
                                    Количество побед
                                </TableSortLabel>
                            </TableCell>
                            <TableCell align="center">Время в игре</TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {sortedStatistics.map((stat) => (
                            <TableRow key={stat.username}>
                                <TableCell>{stat.username}</TableCell>
                                <TableCell align="center">{stat.rating}</TableCell>
                                <TableCell align="center">{stat.playCount}</TableCell>
                                <TableCell align="center">{stat.winCount}</TableCell>
                                <TableCell align="center">{formatDuration(stat.timePlayed)}</TableCell>
                            </TableRow>
                        ))}
                    </TableBody>
                </Table>
            </TableContainer>
            {error && <p className="text-red-500 mt-4">{error}</p>}
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

const formatDuration = (duration) => {
    const hours = Math.floor(duration / 3600);
    const minutes = Math.floor((duration % 3600) / 60);
    const seconds = Math.floor(duration % 60);
    return `${hours}ч ${minutes}м ${seconds}с`;
};

export default StatisticsPage;
