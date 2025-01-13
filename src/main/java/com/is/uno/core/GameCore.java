package com.is.uno.core;

import com.is.uno.dto.api.CardDTO;
import com.is.uno.dto.packet.*;
import com.is.uno.model.*;
import com.is.uno.service.DeckService;
import com.is.uno.service.GameRoomService;
import com.is.uno.service.MessageService;
import com.is.uno.service.PlayerService;
import com.is.uno.socket.PacketHandler;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class GameCore {

    @Getter
    private final Long roomId;
    private final SimpMessagingTemplate messagingTemplate;
    private final GameRoomService gameRoomService;
    private final MessageService messageService;
    private final PlayerService playerService;
    private final DeckService deckService;

    @Getter
    private PacketHandler packetHandler;
    private GameRoom room;
    private Game game;
    private GameState state;
    @Getter
    private final UUID uuid = UUID.randomUUID();
    // username -> player
    private final Map<String, GamePlayer> players = new ConcurrentHashMap<>();
    private final CircularList<GamePlayer> playerOrder = new CircularList<>();

    private final static int MIN_PLAYERS = 2;
    private final static int INITIAL_CARDS = 7;

    void init() {
        room = gameRoomService.findById(roomId);
        packetHandler = new PacketHandler(messagingTemplate, this);
    }

    void destroy() {

    }

    public GamePlayer getPlayerByUser(User user) {
        if (players.containsKey(user.getUsername())) {
            return players.get(user.getUsername());
        }
        GamePlayer player = new GamePlayer(playerService.findByRoomAndUserOrCreate(room, user));
        onPlayerPreJoin(player);
        return player;
    }

    public void checkPlayerTurn(GamePlayer player) {
        if (!state.getCurrentPlayer().equals(player))
            throw new IllegalStateException("Сейчас не ваш ход");
    }

    public void onPlayerPreJoin(GamePlayer player) {
        if (state != null) throw new IllegalStateException("Игра уже идёт");
        players.put(player.getUsername(), player);
    }

    public void onPlayerJoin(GamePlayer player) {
        if (player.isLoaded()) return;
        for (var pl : playerOrder) {
            var pkt = new PlayerJoinPacket();
            pkt.setUsername(pl.getUsername());
            pkt.setInGameName(pl.getInGameName());
            pkt.setReady(pl.isReady());
            packetHandler.sendPacketToPlayer(pkt, player);
        }
        playerOrder.add(player);
        var pkt = new PlayerJoinPacket();
        pkt.setUsername(player.getUsername());
        pkt.setInGameName(player.getInGameName());
        packetHandler.sendPacketToAllPlayers(pkt);
        player.setLoaded(true);
    }

    public void onPlayerLeave(GamePlayer player) {
        player.setLoaded(false);
        players.remove(player.getUsername());
        if (players.isEmpty()) {
            return;
        }
        if(state != null && state.getCurrentPlayer().equals(player)) switchPlayer();
        playerOrder.remove(player);
        if (state != null) playerOrder.startFrom(state.getCurrentPlayer());
        packetHandler.sendPacketToAllPlayers(player.getActionPacket(Action.LEAVE));
    }

    public void onPlayerReady(GamePlayer player) {
        if (state != null) throw new IllegalStateException("Игра уже началась");
        player.onReady();
        packetHandler.sendPacketToAllPlayers(player.getActionPacket(Action.READY));
        boolean allReady = players.size() >= MIN_PLAYERS;
        for (var pl : players.values()) {
            allReady &= pl.isReady();
        }
        if (allReady) {
            startGame();
        }
    }

    public void onPlayerPutCard(GamePlayer player, @NonNull CardDTO card, Color requestedColor) {
        checkPlayerTurn(player);
        var currentCard = state.getCurrentCard();
        if (!card.canPlaceOn(currentCard)) throw new IllegalStateException("Вы не можете положить эту карту сейчас");
        if (card.getColor() == Color.BLACK) card.setNewColor(requestedColor);
        state.setCurrentCard(card);
        player.removeCard(card.getId());

        var pkt = new PutCardPacket();
        pkt.setCardId(card.getId());
        packetHandler.sendPacketToPlayer(pkt, player);
        packetHandler.sendPacketToAllPlayers(player.getActionPacket(Action.PUT_CARD));

        switch (card.getType()) {
            case CHANGE_DIRECTION -> {
                state.reverseOrder();
                if (players.size() > 2) switchPlayer();
            }
            case SKIP -> switchPlayer();
            case PLUS_TWO -> {
                switchPlayer();
                giveCardsToPlayer(state.getCurrentPlayer(), 2);
            }
            case PLUS_FOUR -> {
                switchPlayer();
                giveCardsToPlayer(state.getCurrentPlayer(), 4);
            }
        }

        onPlayerTurnEnd();

        if (player.getCardCount() == 1 && !player.isUNOCalled()) {
            giveCardsToPlayer(player, 2);
            packetHandler.sendPacketToPlayer(TextPacket.createSystem("Вы не сказали UNO и получили 2 карты"), player);
        }
        if (player.getCardCount() == 0) gameOver(player);
        player.setUNOCalled(false);
        player.setCardTaken(false);
    }

    public void onPlayerTakeCard(GamePlayer player) {
        checkPlayerTurn(player);
        if (player.isCardTaken()) {
            throw new IllegalStateException("Вы уже взяли карту");
        }
        giveCardsToPlayer(player, 1);
        if (player.canPlaceCardOn(state.getCurrentCard())) {
            player.setCardTaken(true);
        } else {
            onPlayerTurnEnd();
        }
    }

    public void onPlayerCallUNO(GamePlayer player) {
        checkPlayerTurn(player);
        player.callUNO();
        packetHandler.sendPacketToAllPlayers(player.getActionPacket(Action.CALL_UNO));
    }

    public void onPlayerTurnEnd() {
        switchPlayer();
        packetHandler.sendPacketToAllPlayers(state.getGameStatePacket());
    }

    public void saveMessage(GamePlayer player, String message) {
        messageService.saveMessage(roomId, player.getPlayer(), message);
    }

    public int getPlayerCount() {
        return players.size();
    }

    private void startGame() {
        state = new GameState();
        packetHandler.sendPacketToAllPlayers(ActionPacket.create(Action.GAME_START));
        var deck = new CardDeck();
        deck.fillDeck(deckService.getActualDeck());
        playerOrder.startFrom(0);
        state.setCurrentPlayer(playerOrder.next());
        state.setCurrentCard(deck.takeNumberCard());
        state.setDeck(deck);
        packetHandler.sendPacketToAllPlayers(state.getGameStatePacket());

        // раздача карт
        for (int i = 0; i < INITIAL_CARDS * players.size(); i++) {
            giveCardsToPlayer(state.getCurrentPlayer(), 1);
            switchPlayer();
        }
        game = new Game();
        game.setRoom(room);
    }

    private void gameOver(GamePlayer winner) {
        game.setEndTime(LocalDateTime.now());
        game.setWinner(winner.getPlayer());
        List<GameScore> scores = new LinkedList<>();
        for (var player : players.values()) {
            var score = new GameScore();
            score.setGame(game);
            score.setPlayer(player.getPlayer());
            score.setScore(player.getTotalCardScore());
            scores.add(score);
            player.reset();
        }

        var stats = gameRoomService.onSingleGameOver(game, scores);

        var pkt = new GameOverPacket();
        pkt.setWinner(winner.getUsername());
        pkt.setStats(stats);
        packetHandler.sendPacketToAllPlayers(pkt);

        state = null;
        game = null;
    }

    private void giveCardsToPlayer(GamePlayer player, int count) {
        for (int i = 0; i < count; i++) {
            var card = state.getDeck().takeCard();
            player.addCard(card);
            var pkt = new TakeCardPacket();
            pkt.setCard(card);
            packetHandler.sendPacketToPlayer(pkt, player);
            packetHandler.sendPacketToAllPlayers(player.getActionPacket(Action.TAKE_CARD));
        }
    }

    private void switchPlayer() {
        var nextPlayer = state.isOrderReversed() ? playerOrder.previous() : playerOrder.next();
        state.setCurrentPlayer(nextPlayer);
    }


}
