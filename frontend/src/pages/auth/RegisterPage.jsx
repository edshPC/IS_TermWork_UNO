// import React, { useState } from 'react';
// import { Button } from "@/components/ui/button";
// import { Input } from "@/components/ui/input";
// import { Card, CardContent, CardHeader, CardTitle, CardFooter } from "@/components/ui/card";
// import { Label } from "@/components/ui/label";
// import { ArrowLeft } from 'lucide-react';
//
// const RegisterPage = ({ onRegister, onNavigateLogin }) => {
//     const [username, setUsername] = useState('');
//     const [password, setPassword] = useState('');
//     const [confirmPassword, setConfirmPassword] = useState('');
//     const [error, setError] = useState('');
//
//     const handleSubmit = (e) => {
//         e.preventDefault();
//         if (password.length < 3) {
//             setError('Пароль должен быть не менее 3 символов');
//             return;
//         }
//         if (password !== confirmPassword) {
//             setError('Пароли не совпадают');
//             return;
//         }
//         onRegister(username, password);
//     };
//
//     return (
//         <div className="min-h-screen flex items-center justify-center bg-gray-900 p-4">
//             <Card className="w-full max-w-md">
//                 <CardHeader>
//                     <CardTitle>Регистрация в UNO</CardTitle>
//                 </CardHeader>
//                 <form onSubmit={handleSubmit}>
//                     <CardContent className="space-y-4">
//                         <div className="space-y-2">
//                             <Label htmlFor="username">Имя пользователя</Label>
//                             <Input
//                                 id="username"
//                                 type="text"
//                                 placeholder="Придумайте имя пользователя"
//                                 value={username}
//                                 onChange={(e) => setUsername(e.target.value)}
//                                 required
//                             />
//                         </div>
//                         <div className="space-y-2">
//                             <Label htmlFor="password">Пароль</Label>
//                             <Input
//                                 id="password"
//                                 type="password"
//                                 placeholder="Придумайте пароль"
//                                 value={password}
//                                 onChange={(e) => setPassword(e.target.value)}
//                                 required
//                             />
//                         </div>
//                         <div className="space-y-2">
//                             <Label htmlFor="confirmPassword">Подтвердите пароль</Label>
//                             <Input
//                                 id="confirmPassword"
//                                 type="password"
//                                 placeholder="Введите пароль ещё раз"
//                                 value={confirmPassword}
//                                 onChange={(e) => setConfirmPassword(e.target.value)}
//                                 required
//                             />
//                         </div>
//                         {error && (
//                             <p className="text-red-500 text-sm">{error}</p>
//                         )}
//                     </CardContent>
//                     <CardFooter className="flex flex-col space-y-2">
//                         <Button type="submit" className="w-full">
//                             Зарегистрироваться
//                         </Button>
//                         <Button
//                             type="button"
//                             variant="ghost"
//                             className="w-full"
//                             onClick={onNavigateLogin}
//                         >
//                             <ArrowLeft className="mr-2 h-4 w-4" />
//                             Вернуться ко входу
//                         </Button>
//                     </CardFooter>
//                 </form>
//             </Card>
//         </div>
//     );
// };
//
// export default RegisterPage;


import React, { useState } from 'react';
import Button from '@mui/material/Button';
import TextField from '@mui/material/TextField';
import Card from '@mui/material/Card';
import CardContent from '@mui/material/CardContent';
import CardHeader from '@mui/material/CardHeader';
import CardActions from '@mui/material/CardActions';
import ArrowBackIcon from '@mui/icons-material/ArrowBack';

const RegisterPage = ({ onRegister, onNavigateLogin }) => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [error, setError] = useState('');

    const handleSubmit = (e) => {
        e.preventDefault();
        if (password.length < 3) {
            setError('Пароль должен быть не менее 5 символов');
            return;
        }
        if (password !== confirmPassword) {
            setError('Пароли не совпадают');
            return;
        }
        onRegister(username, password);
    };

    return (
        <div className="min-h-screen flex items-center justify-center bg-gray-900 p-4">
            <Card className="w-full max-w-md">
                <CardHeader
                    title="Регистрация в UNO"
                />
                <form onSubmit={handleSubmit}>
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
                            onClick={onNavigateLogin}
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
