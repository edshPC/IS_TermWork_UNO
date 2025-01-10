import {useEffect, useMemo, useRef, useState} from 'react';

import {PhaserGame} from './PhaserGame.jsx';
import PacketHandler from "./network/PacketHandler.js";

export default function GameApp({gameUUID, privateUUID, token, username}) {
    //  References to the PhaserGame component (game and scene are exposed)
    const phaserRef = useRef();

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
