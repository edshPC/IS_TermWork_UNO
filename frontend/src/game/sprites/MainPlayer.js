import {Player} from "./Player.js";
import {EventBus} from "../EventBus.js";

export class MainPlayer extends Player {
    constructor(scene, username, name) {
        super(scene, username, name);
        this.readySprite
            .setInteractive({ useHandCursor: true })
            .on('pointerup', () => {
                EventBus.emit('ready-pressed');
            });
    }
}

