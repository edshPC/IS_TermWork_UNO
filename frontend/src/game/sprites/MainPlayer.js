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
    
    async giveCard(card) {
        await super.giveCard(card);
        card.setInteractive();
        card.onClick = async () => {
            if (card.color === 'BLACK') await card.requestColor();
            EventBus.emit('put-card', card);
        }
        return card;
    }

}

