import SockJS from 'sockjs-client/dist/sockjs'
import Stomp from 'stompjs'
import {EventBus} from "../EventBus.js";

const origin = 'http://localhost:8080';

export default class PacketHandler {
    stompClient = null;
    subscribers = {};

    constructor(roomId, token, uuid) {
        this.id = roomId;
        this.token = token;
        this.uuid = uuid;
        this.connect();
    }

    connect() {
        this.disconnect();
        var socket = new SockJS(origin + '/ws?token=' + this.token);
        let stompClient = Stomp.over(socket);
        stompClient.connect({
            Authorization: 'Bearer ' + this.token
        }, () => {
            console.log(this);
            stompClient.subscribe('/topic/room/' + this.id, this.onUpdateRecieved);
            stompClient.subscribe('/topic/private/' + this.uuid, this.onUpdateRecieved);
            this.stompClient = stompClient;
            console.log(this);
        });
    }
    
    disconnect() {
        this.stompClient?.disconnect();
        this.stompClient = null;
    }
    
    sendPacket(packet) {
        this.stompClient.send("/app/room/" + this.id, {}, JSON.stringify(packet));
    }
    
    sendAction(action) {
        this.sendPacket({type: 'ACTION_PACKET', action});
    }

    onUpdateRecieved = (update) => {
        const packet = JSON.parse(update.body);
        EventBus.emit('packet-' + packet.type, packet);
        if (packet.type.includes('ACTION_PACKET'))
            EventBus.emit('action-' + packet.action, packet);
        const cbs = this.subscribers[packet.type] || [];
        cbs.forEach(cb => cb(packet));
    }
    
    subscribeOnPacket(packet_type, callback) {
        const cbs = this.subscribers[packet_type] || [];
        cbs.push(callback);
        this.subscribers[packet_type] = cbs;
    }
    
    subscribeOnAction(action, callback) {
        const cb = (packet) => {
            if (packet.action === action) {
                callback(packet);
            }
        }
        this.subscribeOnPacket('ACTION_PACKET', cb);
        this.subscribeOnPacket('PLAYER_ACTION_PACKET', cb);
    }

}
