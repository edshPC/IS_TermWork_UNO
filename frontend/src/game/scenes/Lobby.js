import {EventBus} from '../EventBus';
import {Scene} from 'phaser';
import {TEXT_STYLE} from "../PhaserGame.jsx";
import {Player} from "../sprites/Player.js";
import {MainPlayer} from "../sprites/MainPlayer.js";

export class Lobby extends Scene {
    players = {};

    constructor() {
        super('Lobby');
    }

    create() {

        this.add.text(640, 360, 'Main Menu', TEXT_STYLE).setDepth(100).setOrigin(0.5);

        this.initEvents();
        EventBus.emit('current-scene-ready', this);
    }
    
    initEvents() {
        EventBus.on('packet-PLAYER_JOIN_PACKET', packet => {
            this.createPlayer(packet.username, packet.inGameName, packet.ready);
        });
        EventBus.on('action-READY', packet => {
            this.getPlayerFromPacket(packet).onReady();
        });
    }
    
    createMainPlayer(username, name) {
        this.mainPlayer = new MainPlayer(this, username, name);
        this.players[username] = this.mainPlayer;
    }
    
    createPlayer(username, name, ready) {
        let player = new Player(this, 640, 200, username, name);
        if (ready) player.onReady();
        this.players[username] = player;
    }

    changeScene() {
        EventBus.removeListener('packet-PLAYER_JOIN_PACKET');
        EventBus.removeListener('action-READY');

        this.scene.start('Game');
    }
    
    getPlayerFromPacket = (packet) => {
        return packet.username? this.players[packet.username] : this.mainPlayer;
    }

    moveLogo(reactCallback) {
        if (this.logoTween) {
            if (this.logoTween.isPlaying()) {
                this.logoTween.pause();
            } else {
                this.logoTween.play();
            }
        } else {
            this.logoTween = this.tweens.add({
                targets: this.logo,
                x: {value: 750, duration: 3000, ease: 'Back.easeInOut'},
                y: {value: 80, duration: 1500, ease: 'Sine.easeOut'},
                yoyo: true,
                repeat: -1,
                onUpdate: () => {
                    if (reactCallback) {
                        reactCallback({
                            x: Math.floor(this.logo.x),
                            y: Math.floor(this.logo.y)
                        });
                    }
                }
            });
        }
    }
}
