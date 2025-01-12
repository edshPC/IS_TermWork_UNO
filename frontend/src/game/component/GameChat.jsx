import {useState} from "react";

export default function GameChat({packetHandler}) {
    const [isOpen, setIsOpen] = useState(false);
    
    const openChat = () => {
        setIsOpen(!isOpen);
    }
    
    return (<div className="container chat">
        <button className="rounded" onClick={openChat}>{isOpen ? "Закрыть" : "Открыть"} чат</button>
    </div>);
}
