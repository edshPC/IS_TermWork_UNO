package com.is.uno.controller;

import com.is.uno.dto.DataResponse;
import com.is.uno.dto.api.GameRoomDTO;
import com.is.uno.dto.api.JoinGameRoomDTO;
import com.is.uno.dto.SimpleResponse;
import com.is.uno.model.User;
import com.is.uno.service.GameRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/room")
public class GameRoomController {
    private final GameRoomService gameRoomService;

    @PostMapping
    public ResponseEntity<?> createGameRoom(@RequestBody GameRoomDTO gameRoomDTO,
                                            @AuthenticationPrincipal User user) {
        gameRoomService.createGameRoom(gameRoomDTO, user);
        return SimpleResponse.success();
    }

    @PostMapping("/join")
    public ResponseEntity<?> joinGameRoom(@RequestBody JoinGameRoomDTO joinGameRoomDTO,
                                          @AuthenticationPrincipal User user) {
        var response = gameRoomService.joinGameRoom(joinGameRoomDTO, user);
        return DataResponse.success(response);
    }

    @GetMapping
    public ResponseEntity<?> getAllGameRooms() {
        var rooms = gameRoomService.getAllGameRooms();
        return DataResponse.success(rooms);
    }
}
