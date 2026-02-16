package com.is.uno.controller;

import com.is.uno.dto.DataResponse;
import com.is.uno.dto.api.CreateGameRoomDTO;
import com.is.uno.dto.api.JoinGameRoomDTO;
import com.is.uno.model.User;
import com.is.uno.service.GameRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/rooms")
public class GameRoomController {
    private final GameRoomService gameRoomService;

    @PostMapping
    public ResponseEntity<?> createGameRoom(@RequestBody CreateGameRoomDTO createGameRoomDTO,
                                            @AuthenticationPrincipal User user) {
        var response = gameRoomService.createGameRoom(createGameRoomDTO, user);
        return DataResponse.success(response);
    }

    @PostMapping("/{id}/join")
    public ResponseEntity<?> joinGameRoom(@PathVariable Long id,
                                          @RequestBody JoinGameRoomDTO joinGameRoomDTO,
                                          @AuthenticationPrincipal User user) {
        joinGameRoomDTO.setRoomId(id);
        var response = gameRoomService.joinGameRoom(joinGameRoomDTO, user);
        return DataResponse.success(response);
    }

    @GetMapping
    public ResponseEntity<?> getAllGameRooms() {
        var rooms = gameRoomService.getAllGameRooms();
        return DataResponse.success(rooms);
    }
}
