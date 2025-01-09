import {useEffect, useMemo, useRef, useState} from 'react';

import {PhaserGame} from './game/PhaserGame';
import PacketHandler from "./game/network/PacketHandler.js";

function App() {
    //  References to the PhaserGame component (game and scene are exposed)
    const phaserRef = useRef();

    const roomId = 1, token = "";
    const packetHandler = useMemo(() => new PacketHandler(roomId, token), []);

    useEffect(() => {
        try {
            packetHandler.connect();
        } catch (e) { console.error(e); }
    }, [packetHandler]);

    const changeScene = () => {
        const scene = phaserRef.current.scene;
        if (scene) {
            scene.changeScene();
        }
    }

    // Event emitted from the PhaserGame component
    const currentScene = (scene) => {

    }

    return (
        <div id="app">
            <PhaserGame ref={phaserRef} currentActiveScene={currentScene}/>
            <div>
                <div>
                    <button className="button" onClick={changeScene}>Change Scene</button>
                </div>
            </div>
        </div>
    )
}

export default App
