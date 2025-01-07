package com.is.uno.core;

import com.is.uno.dto.api.CardDTO;
import com.is.uno.dto.packet.*;
import com.is.uno.model.Deck;
import com.is.uno.model.GameRoom;
import com.is.uno.model.User;
import com.is.uno.service.GameRoomService;
import com.is.uno.service.MessageService;
import com.is.uno.service.PlayerService;
import com.is.uno.socket.PacketHandler;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class GameCore {

    private final Long roomId;
    private final SimpMessagingTemplate messagingTemplate;
    private final GameRoomService gameRoomService;
    private final MessageService messageService;
    private final PlayerService playerService;

    @Getter
    private PacketHandler packetHandler;
    private GameRoom room;
    private GameState state;
    // username -> player
    private final Map<String, GamePlayer> players = new ConcurrentHashMap<>();
    private final CircularList<GamePlayer> playerOrder = new CircularList<>();

    void init() {
        room = gameRoomService.findById(roomId);
        packetHandler = new PacketHandler(roomId, messagingTemplate, this);
    }

    public GamePlayer getPlayerByUser(User user) {
        if (players.containsKey(user.getUsername())) {
            return players.get(user.getUsername());
        }
        GamePlayer player = new GamePlayer(playerService.findByRoomAndUserOrCreate(room, user));
        onPlayerJoin(player);
        return player;
    }

    public void checkPlayerTurn(GamePlayer player) {
        if (!state.getCurrentPlayer().equals(player))
            throw new IllegalStateException("Сейчас не ваш ход");
    }

    public void onPlayerJoin(GamePlayer player) {
        players.put(player.getUsername(), player);
        playerOrder.add(player);
        var pkt = new PlayerJoinPacket();
        pkt.setUsername(player.getUsername());
        pkt.setInGameName(player.getInGameName());
        packetHandler.sendPacketToAllPlayers(pkt);
    }

    public void onPlayerLeave(GamePlayer player) {
        players.remove(player.getUsername());
        playerOrder.remove(player);
        packetHandler.sendPacketToAllPlayers(player.getActionPacket(Action.LEAVE));
    }

    public void onPlayerReady(GamePlayer player) {
        if (state != null) throw new IllegalStateException("Игра уже началась");
        player.onReady();
        packetHandler.sendPacketToAllPlayers(player.getActionPacket(Action.READY));
        boolean allReady = players.size() >= 2;
        for (var pl : players.values()) {
            allReady &= pl.isReady();
        }
        if (allReady) {
            startGame();
        }
    }

    public void onPlayerPutCard(GamePlayer player, CardDTO card) {
        checkPlayerTurn(player);
        // TODO
    }

    public void onPlayerTakeCard(GamePlayer player) {
        checkPlayerTurn(player);
        var card = state.getDeck().takeCard();
        player.addCard(card);
        var pkt = new TakeCardPacket();
        pkt.setCard(card);
        packetHandler.sendPacketToPlayer(pkt, player);
        packetHandler.sendPacketToAllPlayers(player.getActionPacket(Action.TAKE_CARD));
        onPlayerTurnEnd(player);
    }

    public void onPlayerCallUNO(GamePlayer player) {
        checkPlayerTurn(player);
        player.callUNO();
        packetHandler.sendPacketToAllPlayers(player.getActionPacket(Action.CALL_UNO));
    }

    public void onPlayerTurnEnd(GamePlayer player) {
        checkPlayerTurn(player);
        switchPlayer();

        var pkt = new GameStatePacket();
        pkt.setCurrentPlayer(state.getCurrentPlayer().getUsername());
        pkt.setCurrentCard(state.getCurrentCard());
        pkt.setOrderReversed(state.isOrderReversed());
        packetHandler.sendPacketToAllPlayers(pkt);
    }

    private void startGame() {
        state = new GameState();
        var deck = new CardDeck();
        //deck.fillDeck(); // TODO
        state.setCurrentPlayer(playerOrder.next());
        state.setCurrentCard(deck.takeCard());
        state.setDeck(deck);

        packetHandler.sendPacketToAllPlayers(ActionPacket.create(Action.GAME_START));

    }

    private void switchPlayer() {
        var nextPlayer = state.isOrderReversed() ? playerOrder.previous() : playerOrder.next();
        state.setCurrentPlayer(nextPlayer);
    }

}
