import {Player} from "./Player.js";
import {EventBus} from "../EventBus.js";

export class MainPlayer extends Player {
    clickedCard;
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
        card.onClick = () => {
            EventBus.emit('put-card', card);
            this.clickedCard = card;
        }
        return card;
    }

}

