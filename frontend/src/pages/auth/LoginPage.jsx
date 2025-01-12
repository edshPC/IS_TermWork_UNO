import React, { useState } from 'react';
import { useDispatch } from 'react-redux';
import { useNavigate } from 'react-router-dom';
import Button from '@mui/material/Button';
import TextField from '@mui/material/TextField';
import Card from '@mui/material/Card';
import CardContent from '@mui/material/CardContent';
import CardHeader from '@mui/material/CardHeader';
import CardActions from '@mui/material/CardActions';
import Typography from '@mui/material/Typography';
import ArrowForwardIcon from '@mui/icons-material/ArrowForward';
import { loginSuccess } from "../../storage/authSlice";

const LoginPage = () => {
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [error, setError] = useState("");
    const dispatch = useDispatch();
    const navigate = useNavigate();

    const handleLogin = async (e) => {
        e.preventDefault();
        try {
            const response = await fetch("http://localhost:8080/api/auth/login", {
                method: "POST",
                headers: { "Content-Type": "application/json;charset=utf-8" },
                body: JSON.stringify({ username, password }),
            });
            const data = await response.json();
            if (response.ok) {
                dispatch(loginSuccess({ username, token: data.token }));
                navigate("/main");
            } else {
                setError(data.message || "Ошибка входа");
            }
        } catch (err) {
            setError("Произошла ошибка");
        }
    };
    // const [username, setUsername] = useState('');
    // const [password, setPassword] = useState('');
    // const [error, setError] = useState('');
    // const dispatch = useDispatch();
    // const navigate = useNavigate();
    //
    // const handleSubmit = async (e) => {
    //     e.preventDefault();
    //     setError('');
    //     try {
    //         const response = await fetch('http://localhost:8080/api/auth/login', {
    //             method: 'POST',
    //             headers: { 'Content-Type': 'application/json;charset=utf-8' },
    //             body: JSON.stringify({ username, password }),
    //         });
    //         const data = await response.json();
    //         if (response.ok) {
    //             dispatch(setUsername(username));
    //             dispatch(setToken(data.token));
    //             navigate('/main');
    //         } else {
    //             setError(data.message || "Неправильный пароль");
    //         }
    //     } catch (err) {
    //         setError("Произошла ошибка при входе");
    //     }
    // };

    return (
        <div className="min-h-screen flex items-center justify-center bg-gray-900 p-4">
            <Card className="w-full max-w-md">
                <CardHeader title="Войти в UNO" subheader="Введите свои данные для входа в игру" />
                <form onSubmit={handleLogin}>
                    <CardContent className="space-y-4">
                        <div className="space-y-2">
                            <TextField
                                id="username"
                                label="Имя пользователя"
                                type="text"
                                placeholder="Введите имя пользователя"
                                value={username}
                                onChange={(e) => setUsername(e.target.value)}
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
                                required
                                fullWidth
                            />
                        </div>
                        {error && (
                            <Typography variant="body2" color="error">
                                {error}
                            </Typography>
                        )}
                    </CardContent>
                    <CardActions className="flex flex-col space-y-2">
                        <Button type="submit" variant="contained" color="primary" fullWidth>
                            Войти
                        </Button>
                        <Button
                            type="button"
                            variant="outlined"
                            color="primary"
                            fullWidth
                            onClick={() => navigate('/register')}
                            endIcon={<ArrowForwardIcon />}
                        >
                            Зарегистрироваться
                        </Button>
                    </CardActions>
                </form>
            </Card>
        </div>
    );
};

export default LoginPage;
