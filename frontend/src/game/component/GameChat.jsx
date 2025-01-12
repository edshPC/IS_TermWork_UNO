import {useEffect, useRef, useState} from "react";
import {EventBus} from "../EventBus.js";

export default function GameChat({packetHandler}) {
    const [isOpen, setIsOpen] = useState(false);
    const [messages, setMessages] = useState([]);
    const [message, setMessage] = useState("");
    const [note, setNote] = useState(false);
    const containerRef = useRef(null);

    useEffect(() => {
        const onMessage = (message) => {
            if (message.textType !== 'PLAYER') return;
            setMessages(m => [...m, message]);
            if (!isOpen) setNote(true);
        };
        EventBus.on('packet-TEXT_PACKET', onMessage);
        return () => EventBus.removeListener('packet-TEXT_PACKET', onMessage);
    }, [isOpen]);
    
    useEffect(() => {
        if (containerRef.current) containerRef.current.scrollTop = containerRef.current.scrollHeight;
    }, [messages]);
    
    const sendMessage = () => {
        if (message) packetHandler.sendPacket({
            type: 'TEXT_PACKET',
            text: message,
        });
        setMessage("");
    }

    const handleTyping = (e) => setMessage(e.target.value);
    const onInputKey = (e) => e.key === 'Enter' && sendMessage();
    const openChat = () => {
        setIsOpen(!isOpen);
        if (!isOpen) setNote(false);
    }

    const messagesComponent = (
        <div>
            <ul ref={containerRef}>
                {messages.map((message, i) => (
                    <li key={i}>
                        <b>{message.sender}</b> &gt; {message.text}
                    </li>
                ))}
            </ul>
            <input className="input-select rounded box" type="text" placeholder="Сообщение"
                   value={message} onChange={handleTyping} onKeyDown={onInputKey}/>
            <button className="rounded margin padding" onClick={sendMessage}>Отправить</button>
        </div>
    );

    return (<div className="container chat">
        {isOpen && messagesComponent}
        <button className={"rounded full " + (note ? "error" : "")}
                onClick={openChat}>{isOpen ? "Закрыть" : "Открыть"} чат
        </button>
    </div>);
}
