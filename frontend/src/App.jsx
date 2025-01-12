// import GameApp from "./game/GameApp.jsx";
// import React, { useEffect, useState } from "react";
//
// export default function App() {
//
//     const [username, setUsername] = useState("string");
//     const [password, setPassword] = useState("string");
//     const [token, setToken] = useState(null);
//     const [gameUUID, setGameUUID] = useState(null);
//     const [privateUUID, setPrivateUUID] = useState(null);
//
//     const authorize = () => {
//         fetch('http://localhost:8080/api/auth/login', {
//             method: 'POST',
//             headers: {'Content-Type': 'application/json;charset=utf-8'},
//             body: JSON.stringify({
//                 username,
//                 password,
//             }),
//         }).then(r => r.json()).then(r => {
//             setToken(r.token);
//         });
//     };
//     const joinGame = () => {
//         if (!token) return;
//         fetch('http://localhost:8080/api/room/join', {
//             method: 'POST',
//             headers: {
//                 'Content-Type': 'application/json;charset=utf-8',
//                 'Authorization': `Bearer ${token}`,
//             },
//             body: JSON.stringify({
//                 roomId: 1
//             }),
//         }).then(r => r.json()).then(r => {
//             setGameUUID(r.data.gameUUID);
//             setPrivateUUID(r.data.privateUUID);
//         });
//     };
//     useEffect(joinGame, [token]);
//
//     return gameUUID ?
//         <GameApp gameUUID={gameUUID} privateUUID={privateUUID} token={token} username={username}/> :
//         <div>
//             <input onChange={(e) => setUsername(e.target.value)} value={username} placeholder="Username"/>
//             <input onChange={(e) => setPassword(e.target.value)} value={password} placeholder="Password"/>
//             <button className="button" onClick={authorize}>Authorize</button>
//         </div>;
// }

import React, { useEffect, useState } from "react";
import LandingPage from "./pages/LandingPage";
import LoginPage from "./pages/auth/LoginPage";
import RegisterPage from "./pages/auth/RegisterPage";
import MainPage from "./pages/MainPage";
import GameApp from "./game/GameApp";

export default function App() {
    const [currentPage, setCurrentPage] = useState('landing');
    const [username, setUsername] = useState("");
    const [token, setToken] = useState(null);
    const [gameUUID, setGameUUID] = useState(null);
    const [privateUUID, setPrivateUUID] = useState(null);

    const handleLogin = async (username, password, onError) => {
        try {
            const response = await fetch('http://localhost:8080/api/auth/login', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json;charset=utf-8' },
                body: JSON.stringify({ username, password }),
            });
            const data = await response.json();
            if (response.ok) {
                setUsername(username);
                setToken(data.token);
                setCurrentPage('main');
            } else {
                if (data.message === "User not found") {
                    onError("Пользователь не найден. Хотите зарегистрироваться?");
                } else {
                    onError(data.message || "Неправильный пароль");
                }
            }
        } catch (err) {
            onError("Произошла ошибка при входе");
        }
    };

    const handleRegister = async (username, password) => {
        try {
            const response = await fetch('http://localhost:8080/api/auth/register', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json;charset=utf-8' },
                body: JSON.stringify({ username, password }),
            });
            const data = await response.json();
            if (response.ok) {
                setUsername(username);
                setToken(data.token);
                setCurrentPage('main');
            } else {
                throw new Error(data.message || "Ошибка регистрации");
            }
        } catch (err) {
            throw err;
        }
    };

    const handleLogout = () => {
        setUsername("");
        setToken(null);
        setCurrentPage('landing');
    };

    const joinGame = () => {
        if (!token) return;
        fetch('http://localhost:8080/api/room/join', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json;charset=utf-8',
                'Authorization': `Bearer ${token}`,
            },
            body: JSON.stringify({
                roomId: 1
            }),
        }).then(r => r.json()).then(r => {
            setGameUUID(r.data.gameUUID);
            setPrivateUUID(r.data.privateUUID);
            setCurrentPage('game');
        });
    };

    useEffect(() => {
        if (token) {
            joinGame();
        }
    }, [token]);

    switch (currentPage) {
        case 'landing':
            return <LandingPage
                onNavigateLogin={() => setCurrentPage('login')}
                onNavigateRegister={() => setCurrentPage('register')}
            />;
        case 'login':
            return <LoginPage
                onLogin={handleLogin}
                onNavigateRegister={() => setCurrentPage('register')}
            />;
        case 'register':
            return <RegisterPage
                onRegister={handleRegister}
                onNavigateLogin={() => setCurrentPage('login')}
            />;
        case 'main':
            return <MainPage
                username={username}
                onLogout={handleLogout}
                onJoinGame={joinGame}
            />;
        case 'game':
            return <GameApp
                gameUUID={gameUUID}
                privateUUID={privateUUID}
                token={token}
                username={username}
            />;
        default:
            return null;
    }
}
