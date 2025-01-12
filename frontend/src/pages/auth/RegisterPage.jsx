import React, { useState } from 'react';
import { useDispatch } from 'react-redux';
import { useNavigate } from 'react-router-dom';
import Button from '@mui/material/Button';
import TextField from '@mui/material/TextField';
import Card from '@mui/material/Card';
import CardContent from '@mui/material/CardContent';
import CardHeader from '@mui/material/CardHeader';
import CardActions from '@mui/material/CardActions';
import ArrowBackIcon from '@mui/icons-material/ArrowBack';
import { loginSuccess } from "../../storage/authSlice";

const RegisterPage = () => {
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");
    const [error, setError] = useState("");

    const dispatch = useDispatch();
    const navigate = useNavigate();

    const handleRegister = async (e) => {
        e.preventDefault();
        setError("");

        if (password.length < 5) {
            setError("Пароль должен быть не менее 5 символов");
            return;
        }

        if (password !== confirmPassword) {
            setError("Пароли не совпадают");
            return;
        }

        try {
            const response = await fetch("http://localhost:8080/api/auth/register", {
                method: "POST",
                headers: { "Content-Type": "application/json;charset=utf-8" },
                body: JSON.stringify({ username, password }),
            });
            const data = await response.json();

            if (response.ok) {
                dispatch(loginSuccess({ username, token: data.token }));
                navigate("/main");
            } else {
                setError(data.message || "Ошибка регистрации");
            }
        } catch (err) {
            setError("Произошла ошибка");
        }
    };

    return (
        <div className="min-h-screen flex items-center justify-center bg-gray-900 p-4">
            <Card className="w-full max-w-md">
                <CardHeader title="Регистрация в UNO" />
                <form onSubmit={handleRegister}>
                    <CardContent className="space-y-4">
                        <div className="space-y-2">
                            <TextField
                                id="username"
                                label="Имя пользователя"
                                type="text"
                                placeholder="Придумайте имя пользователя"
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
                                placeholder="Придумайте пароль"
                                value={password}
                                onChange={(e) => setPassword(e.target.value)}
                                required
                                fullWidth
                            />
                        </div>
                        <div className="space-y-2">
                            <TextField
                                id="confirmPassword"
                                label="Подтвердите пароль"
                                type="password"
                                placeholder="Введите пароль ещё раз"
                                value={confirmPassword}
                                onChange={(e) => setConfirmPassword(e.target.value)}
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
                            Зарегистрироваться
                        </Button>
                        <Button
                            type="button"
                            variant="outlined"
                            color="primary"
                            fullWidth
                            onClick={() => navigate('/login')}
                            startIcon={<ArrowBackIcon />}
                        >
                            Вернуться ко входу
                        </Button>
                    </CardActions>
                </form>
            </Card>
        </div>
    );
};

export default RegisterPage;
