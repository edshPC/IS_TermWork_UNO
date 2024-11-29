package com.is.uno.controller;

import com.is.uno.dto.PlayerDTO;
import com.is.uno.service.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5175")
@RequestMapping("/player")
public class PlayerController {
    private final PlayerService playerService;

    @PostMapping
    public PlayerDTO createPlayer(@RequestBody PlayerDTO playerDTO) {
        return playerService.createPlayer(playerDTO);
    }

    @PutMapping("/{username}/inGameName")
    public PlayerDTO updatePlayerInGameName(@PathVariable String username, @RequestBody String newInGameName) {
        return playerService.updatePlayerInGameName(username, newInGameName);
    }

    @GetMapping("/{username}")
    public PlayerDTO getPlayerByUsername(@PathVariable String username) {
        return playerService.getPlayerByUsername(username);
    }
}
