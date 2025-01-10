import GameApp from "./game/GameApp.jsx";
import {useState} from "react";

export default function App() {
    
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [token, setToken] = useState(null);
    
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
    
    return token ?
        <GameApp roomId={1} token={token} username={username} uuid={''}/> :
        <div>
            <input onChange={(e) => setUsername(e.target.value)} value={username} placeholder="Username" />
            <input onChange={(e) => setPassword(e.target.value)} value={password} placeholder="Password" />
            <button className="button" onClick={authorize}>Authorize</button>
        </div>;
}
