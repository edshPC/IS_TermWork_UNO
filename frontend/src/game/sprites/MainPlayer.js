import {Player} from "./Player.js";
import {EventBus} from "../EventBus.js";

export class MainPlayer extends Player {
    constructor(scene, username, name) {
        super(scene, 640, 600, username, name);
        this.readySprite
            .setInteractive({ useHandCursor: true })
            .on('pointerup', () => {
                EventBus.emit('ready-pressed');
            });
    }
}

