import React, { useEffect, useState } from 'react';
import { useSelector } from 'react-redux';
import { useNavigate } from 'react-router-dom';
import Card from '@mui/material/Card';
import CardContent from '@mui/material/CardContent';
import Typography from '@mui/material/Typography';
import Grid from '@mui/material/Grid';

const AchievementsPage = () => {
    const [achievements, setAchievements] = useState([]);
    const [error, setError] = useState('');
    const token = useSelector((state) => state.auth.token) || localStorage.getItem('token');
    const navigate = useNavigate();
    const username = useSelector((state) => state.auth.username);

    useEffect(() => {
        const fetchAchievements = async () => {
            try {
                const response = await fetch('http://localhost:8080/api/achievement/get-all', {
                    method: 'GET',
                    headers: {
                        'Authorization': `Bearer ${token}`,
                    },
                });
                if (response.ok) {
                    const data = await response.json();
                    setAchievements(data.data);
                } else {
                    const errorData = await response.json();
                    setError(errorData.message || 'Ошибка при загрузке достижений');
                }
            } catch (err) {
                setError('Ошибка при загрузке достижений');
            }
        };

        fetchAchievements();
    }, [token, username]);

    return (
        <div className="min-h-screen flex flex-col items-center bg-gray-900 p-4">
            <h1 className="text-3xl font-bold text-white mb-4">Достижения</h1>
            <Grid container spacing={2} justifyContent="center">
                {achievements.map((achievement) => (
                    <Grid item key={achievement.name} xs={12} sm={6} md={4} lg={3}>
                        <Card className="w-64 h-32 bg-blue-500 text-white font-bold rounded">
                            <CardContent>
                                <Typography variant="h6" component="div">
                                    {achievement.name}
                                </Typography>
                                <Typography color="text.secondary">
                                    {achievement.description}
                                </Typography>
                            </CardContent>
                        </Card>
                    </Grid>
                ))}
            </Grid>
            {error && <p className="text-red-500 mt-4">{error}</p>}
        </div>
    );
};

export default AchievementsPage;
