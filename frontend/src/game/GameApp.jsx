import React, {useEffect, useMemo, useRef} from 'react';

import {PhaserGame} from './PhaserGame.jsx';
import PacketHandler from "./network/PacketHandler.js";
import {useSelector} from "react-redux";
import {useNavigate} from "react-router-dom";
import GameOverPopup from "./component/GameOverPopup.jsx";

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
        packetHandler.onDisconnect = () => navigate('/main');
        return () => packetHandler.disconnect();
    }, [packetHandler]);
    
    return (
        <div id="app">
            <PhaserGame ref={phaserRef} packetHandler={packetHandler} username={username} name={username}/>
            <div className="fixed top-4 right-4">
                <button
                    className={`bg-blue-700 text-white rounded-lg px-4 py-2 shadow-lg`}
                    onClick={packetHandler.disconnect}
                >
                    Покинуть игру
                </button>
            </div>
            <GameOverPopup packetHandler={packetHandler} />
        </div>
    )
}
