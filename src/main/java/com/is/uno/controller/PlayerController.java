package com.is.uno.controller;

import com.is.uno.dto.DataResponse;
import com.is.uno.dto.api.PlayerDTO;
import com.is.uno.dto.SimpleResponse;
import com.is.uno.service.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/player")
public class PlayerController {
    private final PlayerService playerService;

    @PostMapping
    @Deprecated // создание игрока должно происходить автоматически
    public PlayerDTO createPlayer(@RequestBody PlayerDTO playerDTO) {
        return playerService.createPlayer(playerDTO);
    }

    @PutMapping("/{inGameName}/{roomId}/inGameName")
    public ResponseEntity<?> updatePlayerInGameName(@PathVariable String inGameName, Long roomId, @RequestBody String newInGameName) {
        playerService.updatePlayerInGameName(inGameName, roomId, newInGameName);
        return SimpleResponse.success();
    }

    @GetMapping("/{username}")
    public ResponseEntity<?> getPlayerByUsername(@PathVariable String username) {
        var player = playerService.getPlayerByUsername(username);
        return DataResponse.success(player);
    }
}
