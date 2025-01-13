import { useEffect, useState } from "react";
import { EventBus } from "../EventBus.js";
import {useNavigate} from "react-router-dom";

export default function GameOverPopup({packetHandler}) {
    const [isVisible, setIsVisible] = useState(false);
    const [info, setInfo] = useState({stats: []});

    useEffect(() => {
        const onPacket = (packet) => {
            setIsVisible(true);
            setInfo(packet);
        };
        EventBus.on('packet-GAME_OVER_PACKET', onPacket);
        return () => EventBus.removeListener('packet-GAME_OVER_PACKET', onPacket);
    }, []);
    
    const onClose = () => {
        if (info.gameOver) packetHandler.disconnect();
        else setIsVisible(false);
    }
    
    
    const statisticsComponent = (
        <div className="flex flex-col h-64 w-96 bg-blue-700 p-4 rounded-lg shadow-lg">
            <p><b>Игра окончена!</b></p>
            <p>Победитель игры: <b>{info.winner}</b ></p>
            <p>Очки игроков (меньше - лучше):</p>
            <div className="flex-grow overflow-y-auto">
                {info.stats.map((stat, i) => (
                    <p key={i} className="mb-2">
                        {i+1}. {stat.username} - {stat.totalScore} (+{stat.score})
                    </p>
                ))}
            </div>
            {info.gameOver && (
                <p><b>Достигнуто максимальное количество очков: Игра завершена</b></p>
            )}
            <button
                className="bg-pink-700 text-white rounded px-2 py-1"
                onClick={onClose}
            >
                Закрыть
            </button>
        </div>
    );

    return ( 
        <div className="fixed">
            {isVisible && statisticsComponent}
        </div>
    );
}
