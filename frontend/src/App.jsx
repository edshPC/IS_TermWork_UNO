import GameApp from "./game/GameApp.jsx";
import {useEffect, useState} from "react";

export default function App() {

    const [username, setUsername] = useState("string");
    const [password, setPassword] = useState("string");
    const [token, setToken] = useState(null);
    const [gameUUID, setGameUUID] = useState(null);
    const [privateUUID, setPrivateUUID] = useState(null);

    const authorize = () => {
        fetch('http://localhost:8080/api/auth/login', {
            method: 'POST',
            headers: {'Content-Type': 'application/json;charset=utf-8'},
            body: JSON.stringify({
                username,
                password,
            }),
        }).then(r => r.json()).then(r => {
            setToken(r.token);
        });
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
        });
    };
    useEffect(joinGame, [token]);

    return gameUUID ?
        <GameApp gameUUID={gameUUID} privateUUID={privateUUID} token={token} username={username}/> :
        <div>
            <input onChange={(e) => setUsername(e.target.value)} value={username} placeholder="Username"/>
            <input onChange={(e) => setPassword(e.target.value)} value={password} placeholder="Password"/>
            <button className="button" onClick={authorize}>Authorize</button>
        </div>;
}
