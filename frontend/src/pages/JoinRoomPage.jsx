import { useState, useEffect } from 'react';
import {useDispatch, useSelector} from 'react-redux';
import { useNavigate } from 'react-router-dom';
import Button from '@mui/material/Button';
import TextField from '@mui/material/TextField';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Paper from '@mui/material/Paper';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogContentText from '@mui/material/DialogContentText';
import DialogTitle from '@mui/material/DialogTitle';
import {joinGame} from "../storage/gameSlice.jsx";
import {useAuthCheck} from "../storage/authSlice.jsx";

const JoinRoomPage = () => {
    useAuthCheck();
    const [rooms, setRooms] = useState([]);
    const [filteredRooms, setFilteredRooms] = useState([]);
    const [password, setPassword] = useState('');
    const [selectedRoom, setSelectedRoom] = useState(null);
    const [error, setError] = useState('');
    const [open, setOpen] = useState(false);
    const [searchOwner, setSearchOwner] = useState('');
    const token = useSelector((state) => state.auth.token) || localStorage.getItem('token');
    const navigate = useNavigate();
    const dispatch = useDispatch();

    useEffect(() => {
        const fetchRooms = async () => {
            try {
                const response = await fetch('http://localhost:8080/api/room', {
                    method: 'GET',
                    headers: {
                        'Authorization': `Bearer ${token}`,
                    },
                });
                if (response.ok) {
                    const {data} = await response.json();
                    setRooms(data);
                    setFilteredRooms(data); // Отображаем изначально все комнаты
                } else {
                    setError('Ошибка при загрузке комнат');
                }
            } catch (err) {
                setError('Ошибка при загрузке комнат');
            }
        };

        fetchRooms();
    }, [token]);

    const handleSearch = () => {
        const filtered = rooms.filter((room) =>
            room.owner.toLowerCase().includes(searchOwner.toLowerCase())
        );
        setFilteredRooms(filtered);
    };

    const handleJoin = (room) => {
        setSelectedRoom(room);
        if (room.password) {
            setOpen(true);
        } else {
            joinRoom(room.id, null);
        }
    };

    const joinRoom = async (roomId, password) => {
        if (!roomId) {
            setError('Комната не найдена. Попробуйте снова.');
            return;
        }
        try {
            const response = await fetch('http://localhost:8080/api/room/join', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`,
                },
                body: JSON.stringify({
                    roomId,
                    password,
                }),
            });
            if (response.ok) {
                const {data} = await response.json();
                await dispatch(joinGame(data));
                navigate('/game');
            } else {
                const errorData = await response.json();
                setError(errorData.message || 'Ошибка при подключении к комнате');
            }
        } catch (err) {
            setError('Ошибка при подключении к комнате');
        }
    };

    const handleDialogSubmit = () => {
        joinRoom(selectedRoom.id, password);
        setOpen(false);
        setPassword('');
    };

    const handleClose = () => {
        setOpen(false);
        setPassword('');
    };

    return (
        <div className="min-h-screen flex flex-col items-center bg-gray-900 p-4">
            <h1 className="text-3xl font-bold text-white mb-4">Список игровых комнат</h1>
            
            <div className="mb-4 flex justify-center gap-4 w-full max-w-4xl">
                <TextField
                    label="Поиск по владельцу"
                    variant="outlined"
                    fullWidth
                    value={searchOwner}
                    onChange={(e) => setSearchOwner(e.target.value)}
                    placeholder="Введите имя владельца"
                    InputProps={{
                        style: { backgroundColor: 'white' },
                    }}
                />
                <Button variant="contained" color="primary" onClick={handleSearch}>
                    Найти
                </Button>
            </div>

            <TableContainer component={Paper} className="w-full max-w-4xl">
                <Table>
                    <TableHead>
                        <TableRow>
                            <TableCell>Название комнаты</TableCell>
                            <TableCell align="center">Макс. игроков</TableCell>
                            <TableCell align="center">Макс. счет</TableCell>
                            <TableCell align="center">Пароль</TableCell>
                            <TableCell align="center">Владелец</TableCell>
                            <TableCell align="center">Действие</TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {filteredRooms.map((room) => (
                            <TableRow key={room.id}>
                                <TableCell>{room.roomName}</TableCell>
                                <TableCell align="center">{room.maxPlayers}</TableCell>
                                <TableCell align="center">{room.maxScore}</TableCell>
                                <TableCell align="center">{room.password ? 'Да' : 'Нет'}</TableCell>
                                <TableCell align="center">{room.owner}</TableCell>
                                <TableCell align="center">
                                    <Button
                                        variant="contained"
                                        color="primary"
                                        onClick={() => handleJoin(room)}
                                    >
                                        Присоединиться
                                    </Button>
                                </TableCell>
                            </TableRow>
                        ))}
                    </TableBody>
                </Table>
            </TableContainer>

            {error && <p className="text-red-500 mt-4">{error}</p>}

            <Dialog open={open} onClose={handleClose}>
                <DialogTitle>Введите пароль</DialogTitle>
                <DialogContent>
                    <DialogContentText>
                        Для подключения к комнате "{selectedRoom?.roomName}" необходимо ввести пароль.
                    </DialogContentText>
                    <TextField
                        autoFocus
                        margin="dense"
                        id="password"
                        label="Пароль"
                        type="password"
                        fullWidth
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                    />
                </DialogContent>
                <DialogActions>
                    <Button onClick={handleClose} color="secondary">
                        Отмена
                    </Button>
                    <Button onClick={handleDialogSubmit} color="primary">
                        Присоединиться
                    </Button>
                </DialogActions>
            </Dialog>
        </div>
    );
};

export default JoinRoomPage;
