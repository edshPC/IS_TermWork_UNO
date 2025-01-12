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
export const CARD_COLORS = {
    RED: 0xff1111,
    BLUE: 0x1111ff,
    YELLOW: 0xffff11,
    GREEN: 0x11ff11
};

export class Card extends GameObjects.Sprite {
    newColor;
    constructor(scene, x, y, id = -1, type = null, color = null, value = null) {
        super(scene, x, y, 'card_back');

        this.id = id;
        this.type = type;
        this.color = color;
        this.value = value || 0;

        scene.add.existing(this);

        this.setOrigin(0, 1); // Устанавливаем точку привязки в центр
        this.setScale(.3)
        this.updateTexture();
    }
    
    static fromCardDTO(scene, x, y, cardDTO) {
        return new Card(scene, x, y, cardDTO.id, cardDTO.type, cardDTO.color, cardDTO.value);
    }

    updateTexture() {
        if (this.type) {
            let frame = COLOR_OFFSETS[this.color] + TYPE_OFFSETS[this.type];
            if (this.type === 'NUMBER') frame += this.value;
            this.setTexture('cards', frame);
        } else {
            this.setTexture('card_back');
        }
    }

    move(x, y = this.y, angle = 0) {
        return new Promise(resolve => {
            this.scene.tweens.add({
                targets: this,
                x, y, angle: angle / Math.PI * 180,
                duration: 200,
                onComplete: resolve,
            });
        });
    }
    
    setInteractive() {
        super.setInteractive({ useHandCursor: true })
            .on('pointerover', this.onHoverState)
            .on('pointerout', this.onRestState)
            .on('pointerup', () => this.onClick(this));
    }

    onHoverState = () => {
        this.setScale(.4);
        //this.setDepth(1);
    }
    
    onRestState = () => {
        this.setScale(.3);
        //this.setDepth(0);
    }
    
    onClick = card => {
        
    }
    
    async requestColor() {
        return new Promise(resolve => {
            // Создание черного квадратика
            const blackSquare = this.scene.add.rectangle(this.scene.centerX, this.scene.centerY, 200, 200, 0x111111);
            blackSquare.setOrigin(0.5, 0.5);

            const colorPositions = [
                { x: blackSquare.x - 50, y: blackSquare.y - 50 },
                { x: blackSquare.x - 50, y: blackSquare.y + 50 },
                { x: blackSquare.x + 50, y: blackSquare.y - 50 },
                { x: blackSquare.x + 50, y: blackSquare.y + 50 }
            ];
            const squares = [blackSquare];
            Object.keys(CARD_COLORS).forEach((color, index) => {
                const colorSquare = this.scene.add.rectangle(colorPositions[index].x, colorPositions[index].y, 80, 80, CARD_COLORS[color]);
                colorSquare.setOrigin(0.5, 0.5);
                colorSquare.setInteractive({useHandCursor: true});
                colorSquare.on('pointerover', () => colorSquare.setScale(1.1))
                    .on('pointerout', () => colorSquare.setScale(1))
                    .on('pointerup', () => {
                    this.newColor = color; // Установка нового цвета
                    squares.forEach(square => square.destroy(true));
                    resolve();
                });
                squares.push(colorSquare);
            });
        });
    }
    
}
