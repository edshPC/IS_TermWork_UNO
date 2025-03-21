import SockJS from 'sockjs-client/dist/sockjs'
import Stomp from 'stompjs'
import {EventBus} from "../EventBus.js";
import {origin} from "../../App.jsx";

export default class PacketHandler {
    stompClient = null;
    subscribers = {};
    packetQueue = [];

    constructor(gameUUID, privateUUID, token) {
        this.gameUUID = gameUUID;
        this.privateUUID = privateUUID;
        this.token = token;
        this.connect();
    }

    connect() {
        this.disconnect();
        var socket = new SockJS(origin + '/ws?token=' + this.token);
        let stompClient = Stomp.over(socket);
        stompClient.connect({
            Authorization: 'Bearer ' + this.token
        }, () => {
            stompClient.subscribe('/topic/game/' + this.gameUUID, this.onUpdateRecieved);
            stompClient.subscribe('/topic/private/' + this.privateUUID, this.onUpdateRecieved);
            this.stompClient = stompClient;
            window.addEventListener('beforeunload', this.disconnect);
            this.packetQueue.forEach(this.sendPacket, this);
            this.packetQueue = [];
        });
    }
    
    disconnect = () => {
        if (!this.stompClient) return;
        this.sendAction('LEAVE');
        this.stompClient.disconnect();
        this.stompClient = null;
        this.onDisconnect();
        return true;
    }
    
    onDisconnect = () => {}
    
    sendPacket(packet) {
        if (this.stompClient)
            this.stompClient.send("/app/game/" + this.gameUUID, {}, JSON.stringify(packet));
        else this.packetQueue.push(packet);
    }
    
    sendAction(action) {
        this.sendPacket({type: 'ACTION_PACKET', action});
    }

    onUpdateRecieved = (update) => {
        const packet = JSON.parse(update.body);
        if (packet.error) return this.disconnect();
        EventBus.emit('packet-' + packet.type, packet);
        if (packet.type.includes('ACTION_PACKET'))
            EventBus.emit('action-' + packet.action, packet);
    }
    
}
