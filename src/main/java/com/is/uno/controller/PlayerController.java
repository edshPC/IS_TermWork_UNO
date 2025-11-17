package com.is.uno.controller;

import com.is.uno.dto.DataResponse;
import com.is.uno.dto.api.GameRoomDTO;
import com.is.uno.dto.api.PlayerDTO;
import com.is.uno.dto.SimpleResponse;
import com.is.uno.model.GameRoom;
import com.is.uno.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/player")
public class PlayerController {
    private final UserService userService;

    @PutMapping("/{username}/inGameName")
    public ResponseEntity<?> updatePlayerInGameName(@PathVariable String username, @RequestBody String newInGameName) {
        userService.updatePlayerInGameName(username, newInGameName);
        return SimpleResponse.success();
    }

    /*@GetMapping("/{username}")
    public ResponseEntity<?> getPlayerByUsername(@PathVariable String username) {
        var player = userService.getPlayerByUsername(username);
        return DataResponse.success(player);
    }*/
}
