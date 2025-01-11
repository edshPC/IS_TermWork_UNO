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
        return new Promise(resolve => {
            this.scene.tweens.add({
                targets: this,
                x, y, angle: angle / Math.PI * 180,
                duration: 200,
                onComplete: resolve,
            });
        });
    }
    
    async giveCard(card) {
        await card.move(0, -30)
        this.cards.push(card);
        this.add(card);
        this.rearrangeCards();
        return card;
    }
    
    async putAndRemoveCard(id = -1) {
        const card = id === -1 ? 
            this.cards[Math.floor(Math.random() * this.cards.length)] :
            this.cards.find(card => card.id === id);
        if (!card) return;
        card.move(this.scene.activeCardX, this.scene.activeCardY);
    }
    
    rearrangeCards() {
        const cardCount = this.cards.length;
        const minSpace = 20, maxSpace = 600
        const normLength = 300, maxLength = 1000;
        let space = normLength / cardCount;
        space = Math.min(Math.max(space, minSpace), maxSpace);
        if (space * cardCount > maxLength) space = maxLength / cardCount;
        let x = - (space * cardCount / 2);
        this.cards.forEach(card => {
           card.move(x); x += space;
        });
    }

}