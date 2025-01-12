import {EventBus} from '../EventBus';
import {Scene} from 'phaser';
import {TEXT_STYLE} from "../PhaserGame.jsx";
import {Player} from "../sprites/Player.js";
import {MainPlayer} from "../sprites/MainPlayer.js";
import {Card, CARD_COLORS} from "../sprites/Card.js";

export class Lobby extends Scene {
    players = {};
    centerX = 640;
    centerY = 360;
    activeCardX = this.centerX - 60;
    activeCardY = this.centerY + 50;
    activeCard;
    deckCardX = this.centerX + 60;
    deckCardY = this.centerY + 50;
    deckCard;
    loopArrows;
    activeColor;

    constructor() {
        super('Lobby');
    }

    create() {
        this.waitText = this.add.text(this.centerX, this.centerY, 'Ожидание игроков...', TEXT_STYLE).setDepth(100).setOrigin(0.5);
        this.deckCard = new Card(this, this.deckCardX, this.deckCardY);
        this.deckCard.setInteractive();
        this.deckCard.onClick = () => {
            EventBus.emit('take-card');
        }
        this.deckCard.setVisible(false);
        this.initEvents();
        EventBus.emit('current-scene-ready', this);
    }
    
    initEvents() {
        EventBus.on('packet-PLAYER_JOIN_PACKET', packet => {
            if (!(packet.username in this.players))
                this.createPlayer(packet.username, packet.inGameName, packet.ready);
        });
        EventBus.on('packet-GAME_STATE_PACKET', packet => {
            if (this.activeCard) this.activeCard.destroy(true);
            this.activeCard = Card.fromCardDTO(this, this.activeCardX, this.activeCardY, packet.currentCard);
            this.activeCard.setDepth(-1);
            if (this.activeColor) this.activeColor.destroy(true);
            const current_color = packet.currentCard.newColor || packet.currentCard.color;
            this.activeColor = this.add.circle(this.centerX - 160, this.centerY, 50, CARD_COLORS[current_color]);
            if (this.loopArrows) this.loopArrows.destroy(true);
            this.loopArrows = this.add.sprite(this.centerX - 160, this.centerY, 'loop_arrows');
            this.loopArrows.setScale(.125);
            this.loopArrows.setFlipX(packet.orderReversed);
            this.tweens.add({
                targets: this.loopArrows,
                angle: packet.orderReversed ? -360 : 360,
                duration: 5000, // Время вращения (в миллисекундах)
                repeat: -1, // Бесконечное повторение
                ease: 'Linear' // Линейная анимация
            });
        });
        EventBus.on('packet-TAKE_CARD_PACKET', packet => {
            const card = Card.fromCardDTO(this, this.deckCardX, this.deckCardY, packet.card);
            this.mainPlayer.giveCard(card);
        });
        EventBus.on('packet-PUT_CARD_PACKET', packet => {
            this.mainPlayer.putAndRemoveCard(packet.cardId);
        });
        EventBus.on('action-GAME_START', () => {
            this.waitText.setVisible(false);
            this.deckCard.setVisible(true);
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
            if (pl !== this.mainPlayer) pl.giveCard(new Card(this, this.deckCardX, this.deckCardY));
        });
        EventBus.on('action-PUT_CARD', packet => {
            this.getPlayerFromPacket(packet, true)?.putAndRemoveCard();
        });
    }
    
    createMainPlayer(username, name) {
        this.mainPlayer = new MainPlayer(this, username, name);
        this.players[username] = this.mainPlayer;
        this.rearrangePlayers();
    }
    
    createPlayer(username, name, ready) {
        let player = new Player(this, username, name);
        if (ready) player.onReady();
        this.players[username] = player;
        this.rearrangePlayers();
    }
    
    rearrangePlayers() {
        const centerX = this.cameras.main.centerX;
        const centerY = this.cameras.main.centerY;

        const radius = 250; // Радиус окружности
        const playerCount = Object.values(this.players).length;
        Object.values(this.players).forEach((pl, i) => {
            const angle = (i / playerCount) * (2 * Math.PI) + (Math.PI / 2); // Угол в радианах
            const x = centerX + radius * Math.cos(angle); // Вычисляем X координату
            const y = centerY + radius * Math.sin(angle); // Вычисляем Y координату
            //pl.move(x, y, (angle - Math.PI/2));
            pl.move(x, y);
        });
    }

    changeScene() {
        EventBus.removeListener('packet-PLAYER_JOIN_PACKET');
        EventBus.removeListener('action-READY');

        this.scene.start('Game');
    }
    
    getPlayerFromPacket = (packet, excludeMain) => {
        const pl = packet.username? this.players[packet.username] : this.mainPlayer;
        if (excludeMain && pl === this.mainPlayer) return null;
        return pl;
    }
    
}
