import SockJS from 'sockjs-client/dist/sockjs'
import Stomp from 'stompjs'

export default class PacketHandler {
    stompClient = null;

    constructor(roomId, token) {
        this.id = roomId;
        this.token = token;
    }

    connect() {
        this.stompClient?.disconnect();
        var socket = new SockJS('/ws?token=' + this.token);
        this.stompClient = Stomp.over(socket);
        this.stompClient.connect({
            Authorization: 'Bearer ' + this.token
        }, () => {
            this.stompClient.subscribe('/topic/room/' + this.id, this.onUpdateRecieved);
            this.stompClient.subscribe('/topic/private/' + this.token, this.onUpdateRecieved);
        });
    }

    onUpdateRecieved = (update) => {
        let packet = JSON.parse(update.body);
    }

}
