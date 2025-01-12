import React, { useState } from 'react';
import Button from '@mui/material/Button';
import TextField from '@mui/material/TextField';
import Card from '@mui/material/Card';
import CardContent from '@mui/material/CardContent';
import CardHeader from '@mui/material/CardHeader';
import CardActions from '@mui/material/CardActions';
import Typography from '@mui/material/Typography';
import ArrowForwardIcon from '@mui/icons-material/ArrowForward';

const LoginPage = ({ onLogin, onNavigateRegister }) => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');

    const handleSubmit = (e) => {
        e.preventDefault();
        setError('');
        onLogin(username, password, setError);
    };

    return (
        <div className="min-h-screen flex items-center justify-center bg-gray-900 p-4">
            <Card className="w-full max-w-md">
                <CardHeader
                    title="Войти в UNO"
                    subheader="Введите свои данные для входа в игру"
                />
                <form onSubmit={handleSubmit}>
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
                            onClick={onNavigateRegister}
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
