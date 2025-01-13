import {Boot} from './scenes/Boot';
import {Lobby} from './scenes/Lobby.js';
import Phaser from 'phaser';
import {Preloader} from './scenes/Preloader';

// Find out more information about the Game Config at:
// https://newdocs.phaser.io/docs/3.70.0/Phaser.Types.Core.GameConfig
const config = {
    type: Phaser.AUTO,
    width: 1280,
    height: 720,
    parent: 'game-container',
    backgroundColor: '#028af8',
    scene: [
        Boot,
        Preloader,
        Lobby
    ]
};

const StartGame = (parent) => {
    return new Phaser.Game({...config, parent});
}

export default StartGame;
