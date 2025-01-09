import SockJS from 'sockjs-client/dist/sockjs'
import Stomp from 'stompjs'

export default class PacketHandler {
    stompClient = null;

    constructor(roomId, token, uuid) {
        this.id = roomId;
        this.token = token;
        this.uuid = uuid;
    }

    connect() {
        this.stompClient?.disconnect();
        var socket = new SockJS('/ws?token=' + this.token);
        this.stompClient = Stomp.over(socket);
        this.stompClient.connect({
            Authorization: 'Bearer ' + this.token
        }, () => {
            this.stompClient.subscribe('/topic/room/' + this.id, this.onUpdateRecieved);
            this.stompClient.subscribe('/topic/private/' + this.uuid, this.onUpdateRecieved);
        });
    }
    
    sendPacket(packet) {
        this.stompClient.send("/app/room/" + this.id, {}, JSON.stringify(packet));
    }

    onUpdateRecieved = (update) => {
        let packet = JSON.parse(update.body);
    }

}
