import {GameObjects} from "phaser";
import {TEXT_STYLE} from "../PhaserGame.jsx";

export class Player extends GameObjects.Container {

    ready = false;
    cards = [];

    constructor(scene, username, name) {
        super(scene, scene.centerX, scene.centerY);
        this.username = username;
        this.name = name || username;

        this.nameSprite = scene.add.text(0, 15, this.name, TEXT_STYLE).setOrigin();
        this.readySprite = scene.add.text(0, -15, 'Не готов', TEXT_STYLE).setOrigin();
        this.readySprite.setColor('MAGENTA')

        this.add([
            this.nameSprite,
            this.readySprite,
        ]);
        scene.add.existing(this);
    }

    onReady(ready = !this.ready) {
        this.ready = ready;
        this.readySprite.setText(ready ? 'Готов' : 'Не готов');
        this.readySprite.setColor(ready ? 'LIME' : 'MAGENTA');
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
    
    setCurrentTurn(isActive = true) {
        this.nameSprite.setColor(isActive ? 'LIME' : 'GREY');
    }
    
    async giveCard(card) {
        await card.move(this.x, this.y, 0, this.scale || 1);
        this.cards.push(card);
        this.add(card);
        card.setPosition(0, 0);
        await this.rearrangeCards();
        return card;
    }
    
    async putAndRemoveCard(id = -1) {
        const card = id === -1 ? 
            this.cards[Math.floor(Math.random() * this.cards.length)] :
            this.cards.find(card => card.id === id);
        if (!card) return;
        this.remove(card);
        card.setPosition(this.x, this.y);
        await card.move(this.scene.activeCardX, this.scene.activeCardY);
        this.cards.splice(this.cards.indexOf(card), 1);
        card.destroy(true);
        await this.rearrangeCards();
    }
    
    async rearrangeCards() {
        const cardCount = this.cards.length;
        const minSpace = 20, maxSpace = 600
        const normLength = 300, maxLength = 1000;
        let space = normLength / cardCount;
        space = Math.min(Math.max(space, minSpace), maxSpace);
        if (space * cardCount > maxLength) space = maxLength / cardCount;
        let x = - (space * cardCount / 2);
        const promises = [];
        this.cards.forEach(card => {
            promises.push(card.move(x, card.y, 0, this.scale || 1));
            x += space;
        });
        await Promise.all(promises);
    }
    
    callUNO() {
        const unoButton = this.scene.add.sprite(0, 0, 'uno_button')
            .setScale(.3).setDepth(5);
        this.add(unoButton);
        this.scene.tweens.add({
            targets: unoButton,
            y: -300,
            delay: 500,
            ease: 'sine.inout',
            onComplete: () => {
                unoButton.destroy(true);
            }
        })
    }
    
    onGameStart() {
        this.readySprite.setVisible(false);
    }
    
    async onGameOver() {
        while (this.cards.length > 0) await this.putAndRemoveCard();
        this.nameSprite.setColor('WHITE');
        this.onReady(false);
        this.readySprite.setVisible(true);
    }

}