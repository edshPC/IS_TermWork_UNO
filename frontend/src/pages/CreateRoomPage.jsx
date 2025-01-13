import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import {useDispatch, useSelector} from 'react-redux';
import Button from '@mui/material/Button';
import TextField from '@mui/material/TextField';
import Card from '@mui/material/Card';
import CardContent from '@mui/material/CardContent';
import CardHeader from '@mui/material/CardHeader';
import CardActions from '@mui/material/CardActions';
import {joinGame} from "../storage/gameSlice.jsx";
import {useAuthCheck} from "../storage/authSlice.jsx";

const CreateRoomPage = () => {
    useAuthCheck();
    const [roomName, setRoomName] = useState('');
    const [password, setPassword] = useState('');
    const [maxPlayers, setMaxPlayers] = useState('');
    const [maxScore, setMaxScore] = useState('');
    const [error, setError] = useState('');
    const navigate = useNavigate();
    const token = useSelector((state) => state.auth.token) || localStorage.getItem('token');
    const dispatch = useDispatch();
    
    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');
        try {
            const response = await fetch('http://localhost:8080/api/room', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json;charset=utf-8',
                    'Authorization': `Bearer ${token}`,
                },
                body: JSON.stringify({
                    roomName,
                    password,
                    maxPlayers: parseInt(maxPlayers),
                    maxScore: parseInt(maxScore),
                }),
            });
            if (response.ok) {
                const {data} = await response.json();
                await dispatch(joinGame(data));
                navigate('/game');
            } else {
                const data = await response.json();
                setError(data.message || "Ошибка при создании комнаты");
            }
        } catch (err) {
            setError("Произошла ошибка при создании комнаты");
        }
    };

    return (
        <div className="min-h-screen flex items-center justify-center bg-gray-900 p-4">
            <Card className="w-full max-w-md">
                <CardHeader title="Создать игровую комнату" />
                <form onSubmit={handleSubmit}>
                    <CardContent className="space-y-4">
                        <div className="space-y-2">
                            <TextField
                                id="roomName"
                                label="Имя комнаты"
                                type="text"
                                placeholder="Введите имя комнаты"
                                value={roomName}
                                onChange={(e) => setRoomName(e.target.value)}
                                required
                                fullWidth
                            />
                        </div>
                        <div className="space-y-2">
                            <TextField
                                id="password"
                                label="Пароль"
                                type="password"
                                placeholder="Введите пароль"
                                value={password}
                                onChange={(e) => setPassword(e.target.value)}
                                fullWidth
                            />
                        </div>
                        <div className="space-y-2">
                            <TextField
                                id="maxPlayers"
                                label="Максимальное количество игроков"
                                type="number"
                                placeholder="Введите количество от 2 до 8"
                                value={maxPlayers}
                                onChange={(e) => setMaxPlayers(e.target.value)}
                                required
                                fullWidth
                            />
                        </div>
                        <div className="space-y-2">
                            <TextField
                                id="maxScore"
                                label="Максимальный счет"
                                type="number"
                                placeholder="Введите максимальный счет"
                                value={maxScore}
                                onChange={(e) => setMaxScore(e.target.value)}
                                required
                                fullWidth
                            />
                        </div>
                        {error && (
                            <p className="text-red-500 text-sm">{error}</p>
                        )}
                    </CardContent>
                    <CardActions className="flex flex-col space-y-2">
                        <Button type="submit" variant="contained" color="primary" fullWidth>
                            Создать комнату
                        </Button>
                    </CardActions>
                </form>
            </Card>
        </div>
    );
};

export default CreateRoomPage;
