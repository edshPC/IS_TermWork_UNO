import React, {forwardRef, useEffect, useLayoutEffect, useRef} from 'react';
import StartGame from './main';
import {EventBus} from './EventBus';
import GameChat from "./component/GameChat.jsx";

export const TEXT_STYLE = {
    fontFamily: 'Arial Black', fontSize: 20, color: '#ffffff',
    stroke: '#000000', strokeThickness: 6,
    align: 'center'
};

function PhaserGameFunc({packetHandler, username, name}, ref) {
    
    const game = useRef();

    // Create the game inside a useLayoutEffect hook to avoid the game being created outside the DOM
    useLayoutEffect(() => {
        if (game.current === undefined) {
            game.current = StartGame("game-container");
            if (ref !== null) {
                ref.current = {game: game.current, scene: null};
            }
        }

        return () => {
            if (game.current) {
                game.current.destroy(true);
                game.current = undefined;
            }
        }
    }, [ref]);

    useEffect(() => {

        EventBus.on('current-scene-ready', (currentScene) => {
            ref.current.scene = currentScene;
            currentScene.createMainPlayer(username, name);
        });
        EventBus.on('ready-pressed', () => packetHandler.sendAction('READY'));
        EventBus.on('put-card', (card) => packetHandler.sendPacket({
            type: 'PUT_CARD_PACKET',
            cardId: card.id,
            newColor: card.newColor,
        }));
        EventBus.on('take-card', () => packetHandler.sendAction('TAKE_CARD'));
        EventBus.on('uno-called', () => packetHandler.sendAction('CALL_UNO'));

        return () => {
            EventBus.removeListener('current-scene-ready');
            EventBus.removeListener('ready-pressed');
        }
        
    }, [packetHandler, ref])

    return (
        <div id="game-container">
            <GameChat packetHandler={packetHandler} />
        </div>
    );

}

export const PhaserGame = forwardRef(PhaserGameFunc);
