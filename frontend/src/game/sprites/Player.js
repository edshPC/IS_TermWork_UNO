import {GameObjects} from "phaser";
import {TEXT_STYLE} from "../PhaserGame.jsx";

export class Player extends GameObjects.Container {

    ready = false;

    constructor(scene, x, y, username, name) {
        super(scene, x, y);
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

    move(x, y) {
        this.scene.tweens.add({
            targets: this,
            x, y,
            duration: 200,
        });
    }

}