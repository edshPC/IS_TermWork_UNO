package com.is.uno.core;

public class GameCore {

    private GameState state = new GameState();

    public boolean onPlayerReady(Long id) {
        GamePlayer player = state.getPlayers().get(id);
        player.onReady();
        boolean allReady = true;
        for (var pl : state.getPlayers().values()) {
            allReady &= pl.isReady();
        }
        return allReady && state.getPlayers().size() >= 2;
    }

}
