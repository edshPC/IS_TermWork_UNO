import {useEffect, useMemo, useRef} from 'react';

import {PhaserGame} from './PhaserGame.jsx';
import PacketHandler from "./network/PacketHandler.js";
import {useSelector} from "react-redux";
import {useNavigate} from "react-router-dom";

export default function GameApp() {
    const username = useSelector(state => state.auth.username);
    const token = useSelector(state => state.auth.token);
    const gameUUID = useSelector(state => state.game.gameUUID);
    const privateUUID = useSelector(state => state.game.privateUUID);
    
    const phaserRef = useRef();
    const navigate = useNavigate();
    
    useEffect(() => {
        if (!username || !token || !gameUUID || !privateUUID) {
            navigate('/main');
        }
    }, [username, token, gameUUID, privateUUID, navigate]);
    
    const packetHandler = useMemo(() =>
        new PacketHandler(gameUUID, privateUUID, token), [gameUUID, privateUUID, token]);

    useEffect(() => {
        return () => packetHandler.disconnect();
    }, [packetHandler]);

    const changeScene = () => {
        const scene = phaserRef.current.scene;
        if (scene) {
            scene.changeScene();
        }
    }

    return (
        <div id="app">
            <PhaserGame ref={phaserRef} packetHandler={packetHandler} username={username} name={username}/>
            <div>
                <div>
                </div>
            </div>
        </div>
    )
}
