import {GameObjects} from "phaser";
import {TEXT_STYLE} from "../PhaserGame.jsx";
import {Card} from "./Card.js";

export class Player extends GameObjects.Container {

    ready = false;
    cards = [];

    constructor(scene, username, name) {
        super(scene, scene.cameras.main.centerX, scene.cameras.main.centerY);
        this.username = username;
        this.name = name || username;

        this.nameSprite = scene.add.text(0, -15, this.name, TEXT_STYLE).setOrigin();
        this.readySprite = scene.add.text(0, 15, 'Не готов', TEXT_STYLE).setOrigin();

        this.add([
            this.nameSprite,
            this.readySprite,
        ]);
        scene.add.existing(this);
    }

    onReady() {
        this.ready = !this.ready;
        this.readySprite.setText(this.ready ? 'Готов' : 'Не готов');
    }

    move(x, y, angle = 0) {
        this.scene.tweens.add({
            targets: this,
            x, y, angle: angle / Math.PI * 180,
            duration: 200,
        });
    }
    
    giveCard(id = -1, type = null, color = null, value = null) {
        let card = new Card(this.scene, 0, -30, id, type, color, value);
        card.setScale(.5);
        this.cards.push(card);
        this.add(card);
    }
    
    rearrangeCards() {
        
    }

}