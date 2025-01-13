import {EventBus} from '../EventBus';
import {Scene} from 'phaser';
import {TEXT_STYLE} from "../PhaserGame.jsx";
import {Player} from "../sprites/Player.js";
import {MainPlayer} from "../sprites/MainPlayer.js";
import {Card, CARD_COLORS} from "../sprites/Card.js";

export class Lobby extends Scene {
    players = {};
    animationQueue = [];
    animationInProcess = false;
    centerX = 640;
    centerY = 360;
    activeCardX = this.centerX - 60;
    activeCardY = this.centerY + 50;
    activeCard;
    deckCardX = this.centerX + 60;
    deckCardY = this.activeCardY;
    deckCard;
    loopArrows;
    activeColor;
    unoButton;

    constructor() {
        super('Lobby');
    }
    
    

    create() {
        this.waitText = this.add.text(this.centerX, this.centerY, 'Ожидание игроков...', TEXT_STYLE).setDepth(100).setOrigin(0.5);
        this.deckCard = new Card(this, this.deckCardX, this.deckCardY)
            .setDepth(1).setVisible(false);
        this.deckCard.setInteractive();
        this.deckCard.onClick = () => {
            EventBus.emit('take-card');
        }
        this.unoButton = this.add.sprite(this.deckCardX + 140, this.centerY, 'uno_button')
            .setScale(.2).setVisible(false).setInteractive({useHandCursor: true})
            .on('pointerover', () => this.unoButton.setScale(.25))
            .on('pointerout', () => this.unoButton.setScale(.2))
            .on('pointerup', () => {
                EventBus.emit('uno-called');
            });
        this.initEvents();
        EventBus.emit('current-scene-ready', this);
    }
    
    initEvents() {
        EventBus.on('packet-PLAYER_JOIN_PACKET', packet => {
            if (!(packet.username in this.players))
                this.createPlayer(packet.username, packet.inGameName, packet.ready);
        });
        EventBus.on('packet-GAME_STATE_PACKET', packet => {
            this.destroyAll();
            this.activeCard = Card.fromCardDTO(this, this.activeCardX, this.activeCardY, packet.currentCard);
            this.activeCard.setDepth(-1);
            const current_color = packet.currentCard.newColor || packet.currentCard.color;
            this.activeColor = this.add.circle(this.centerX - 160, this.centerY, 45, CARD_COLORS[current_color]);
            this.activeColor.setStrokeStyle(5, 0xffffff);
            this.loopArrows = this.add.sprite(this.centerX - 160, this.centerY, 'loop_arrows');
            this.loopArrows.setScale(.11);
            this.loopArrows.setFlipX(packet.orderReversed);
            this.tweens.add({
                targets: this.loopArrows,
                angle: packet.orderReversed ? -360 : 360,
                duration: 5000,
                repeat: -1,
                ease: 'Linear'
            });
            Object.values(this.players).forEach(player =>
                player.setCurrentTurn(packet.currentPlayer === player.username));
        });
        EventBus.on('packet-TAKE_CARD_PACKET', packet => {
            const card = Card.fromCardDTO(this, this.deckCardX, this.deckCardY, packet.card);
            this.pushAnimation(() => this.mainPlayer.giveCard(card));
        });
        EventBus.on('packet-PUT_CARD_PACKET', packet => {
            this.mainPlayer.putAndRemoveCard(packet.cardId);
        });
        EventBus.on('packet-TEXT_PACKET', packet => {
           if (packet.textType === 'SYSTEM') {
               const txt = this.add.text(this.centerX, -50, packet.text, TEXT_STYLE);
               txt.setColor('MAGENTA');
               txt.setOrigin(.5);
               txt.setDepth(10);
               this.tweens.add({
                   targets: txt,
                   y: this.centerY,
                   ease: 'sine.inout',
                   onComplete: () => {
                       this.tweens.add({
                           targets: txt,
                           y: -50,
                           delay: 1000,
                           ease: 'sine.inout',
                           onComplete: () => {
                                txt.destroy(true);
                           }
                       })
                   }
               })
           } 
        });
        EventBus.on('packet-GAME_OVER_PACKET',  async packet => {
            const promises = [];
            Object.values(this.players).forEach(player => promises.push(player.onGameOver()));
            await Promise.all(promises);
            Object.values(this.players).forEach(player => player.cards.forEach(c => c.destroy(true)));
            this.destroyAll();
            this.waitText.setVisible(true);
            this.deckCard.setVisible(false);
            this.unoButton.setVisible(false);
        });
        EventBus.on('action-GAME_START', () => {
            this.waitText.setVisible(false);
            this.deckCard.setVisible(true);
            this.unoButton.setVisible(true);
            Object.values(this.players).forEach(player => player.onGameStart());
        });
        EventBus.on('action-READY', packet => {
            this.getPlayerFromPacket(packet)?.onReady();
        });
        EventBus.on('action-LEAVE', packet => {
            this.getPlayerFromPacket(packet)?.destroy(true);
            delete this.players[packet.username];
            this.rearrangePlayers();
        });
        EventBus.on('action-TAKE_CARD', packet => {
            const pl = this.getPlayerFromPacket(packet);
            if (pl !== this.mainPlayer) this.pushAnimation(() => pl.giveCard(new Card(this, this.deckCardX, this.deckCardY)));
        });
        EventBus.on('action-PUT_CARD', packet => {
            this.getPlayerFromPacket(packet, true)?.putAndRemoveCard();
        });
        EventBus.on('action-CALL_UNO', packet => {
            this.getPlayerFromPacket(packet)?.callUNO();
        });
    }
    
    createMainPlayer(username, name) {
        this.mainPlayer = new MainPlayer(this, username, name);
        this.players[username] = this.mainPlayer;
        this.rearrangePlayers();
    }
    
    createPlayer(username, name, ready) {
        const player = new Player(this, username, name);
        player.onReady(ready);
        this.players[username] = player;
        this.rearrangePlayers();
    }
    
    rearrangePlayers() {
        const centerX = this.centerX;
        const centerY = this.centerY + 50;

        const radius = 220; // Радиус окружности
        const playerCount = Object.values(this.players).length;
        Object.values(this.players).forEach((pl, i) => {
            const angle = (i / playerCount) * (2 * Math.PI) + (Math.PI / 2); // Угол в радианах
            const x = centerX + radius * Math.cos(angle); // Вычисляем X координату
            const y = centerY + radius * Math.sin(angle); // Вычисляем Y координату
            //pl.move(x, y, (angle - Math.PI/2));
            pl.move(x, y);
            if (i > 0 && playerCount > 2) pl.setScale(1 / (1.1 ** playerCount));
        });
    }
    
    pushAnimation(func = async () => {}, ...args) {
        this.animationQueue.push({func, args});
        this.playAnimations();
    }
    
    async playAnimations() {
        if (this.animationInProcess) return;
        this.animationInProcess = true;
        while (this.animationQueue.length > 0) {
            const {func, args} = this.animationQueue[0];
            await func(...args);
            this.animationQueue.shift();
        }
        this.animationInProcess = false;
    }

    close() {
        EventBus.removeListener('packet-PLAYER_JOIN_PACKET');
        EventBus.removeListener('packet-GAME_STATE_PACKET');
        EventBus.removeListener('packet-TAKE_CARD_PACKET');
        EventBus.removeListener('packet-PUT_CARD_PACKET');
        EventBus.removeListener('packet-TEXT_PACKET')
        EventBus.removeListener('packet-GAME_OVER_PACKET');
        EventBus.removeListener('action-GAME_START');
        EventBus.removeListener('action-READY');
        EventBus.removeListener('action-LEAVE');
        EventBus.removeListener('action-TAKE_CARD');
        EventBus.removeListener('action-PUT_CARD');
        EventBus.removeListener('action-CALL_UNO');        
    }
    
    getPlayerFromPacket = (packet, excludeMain) => {
        const pl = packet.username? this.players[packet.username] : this.mainPlayer;
        if (excludeMain && pl === this.mainPlayer) return null;
        return pl;
    }
    
    destroyAll() {
        this.activeCard?.destroy(true);
        this.activeCard = null;
        this.activeColor?.destroy(true);
        this.activeColor = null;
        this.loopArrows?.destroy(true);
        this.loopArrows = null;        
    }
    
}
