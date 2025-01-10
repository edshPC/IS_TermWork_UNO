import {GameObjects} from "phaser";
import {TEXT_STYLE} from "../PhaserGame.jsx";

export class Player extends GameObjects.Container {

    ready = false;

    constructor(scene, username, name) {
        super(scene, scene.cameras.main.centerX, scene.cameras.main.centerY);
        this.username = username;
        this.name = name || username;

        this.nameSprite = scene.add.text(0, -15, this.name, TEXT_STYLE).setOrigin();
        this.readySprite = scene.add.text(0, 15, 'Не готов', TEXT_STYLE).setOrigin();
        scene.add.text()

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

}