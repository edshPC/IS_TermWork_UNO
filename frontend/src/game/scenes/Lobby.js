import {EventBus} from '../EventBus';
import {Scene} from 'phaser';
import {TEXT_STYLE} from "../PhaserGame.jsx";
import {Player} from "../sprites/Player.js";
import {MainPlayer} from "../sprites/MainPlayer.js";
import {Card} from "../sprites/Card.js";

export class Lobby extends Scene {
    players = {};

    constructor() {
        super('Lobby');
    }

    create() {
        new Card(this, 600, 550);
        new Card(this, 620, 550);
        new Card(this, 640, 550);
        new Card(this, 660, 550);
        new Card(this, 680, 550);
        this.add.text(640, 360, 'Main Menu', TEXT_STYLE).setDepth(100).setOrigin(0.5);

        this.initEvents();
        EventBus.emit('current-scene-ready', this);
    }
    
    initEvents() {
        EventBus.on('packet-PLAYER_JOIN_PACKET', packet => {
            if (!(packet.username in this.players))
                this.createPlayer(packet.username, packet.inGameName, packet.ready);
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
            this.getPlayerFromPacket(packet)?.giveCard();
        })
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

        const radius = 240; // Радиус окружности
        const playerCount = Object.values(this.players).length;
        Object.values(this.players).forEach((pl, i) => {
            const angle = (i / playerCount) * (2 * Math.PI) + (Math.PI / 2); // Угол в радианах
            const x = centerX + radius * Math.cos(angle); // Вычисляем X координату
            const y = centerY + radius * Math.sin(angle); // Вычисляем Y координату
            pl.move(x, y, (angle - Math.PI/2));
        });
    }

    changeScene() {
        EventBus.removeListener('packet-PLAYER_JOIN_PACKET');
        EventBus.removeListener('action-READY');

        this.scene.start('Game');
    }
    
    getPlayerFromPacket = (packet) => {
        return packet.username? this.players[packet.username] : this.mainPlayer;
    }
    
}
