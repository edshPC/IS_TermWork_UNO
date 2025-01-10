import {useEffect, useMemo, useRef, useState} from 'react';

import {PhaserGame} from './PhaserGame.jsx';
import PacketHandler from "./network/PacketHandler.js";

export default function GameApp({roomId, token, uuid, username}) {
    //  References to the PhaserGame component (game and scene are exposed)
    const phaserRef = useRef();

    const packetHandler = useMemo(() =>
        new PacketHandler(roomId, token, uuid), [roomId, token, uuid]);

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
                    <button className="button" onClick={changeScene}>Change Scene</button>
                </div>
            </div>
        </div>
    )
}
