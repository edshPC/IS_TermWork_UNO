import {GameObjects} from 'phaser'

const COLOR_OFFSETS = {
    RED: 0,
    YELLOW: 14,
    GREEN: 28,
    BLUE: 42,
    BLACK: 0,
};
const TYPE_OFFSETS = {
    NUMBER: 0,
    SKIP: 10,
    CHANGE_DIRECTION: 11,
    PLUS_TWO: 12,
    CHOOSE_COLOR: 13,
    PLUS_FOUR: 27,
};

export class Card extends GameObjects.Sprite {
    constructor(scene, x, y, id = -1, type = null, color = null, value = null) {
        super(scene, x, y, 'card_back');

        this.id = id;
        this.type = type;
        this.color = color;
        this.value = value || 0;

        scene.add.existing(this);

        this.setOrigin(0.5, 0.5); // Устанавливаем точку привязки в центр
        this.setInteractive(); // Делаем карту интерактивной
        this.update();
    }

    update() {
        if (this.type) {
            let frame = COLOR_OFFSETS[this.color] + TYPE_OFFSETS[this.type] + this.value;
            this.setTexture('cards', frame);
        } else {
            this.setTexture('card_back');
        }
    }
}
