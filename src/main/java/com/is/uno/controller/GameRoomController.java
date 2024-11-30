package com.is.uno.controller;

import com.is.uno.dto.GameRoom.GameRoomDTO;
import com.is.uno.dto.GameRoom.JoinGameRoomDTO;
import com.is.uno.model.User;
import com.is.uno.service.GameRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5175")
@RequestMapping("/api/room")
public class GameRoomController {
    private final GameRoomService gameRoomService;

    @PostMapping
    public GameRoomDTO createGameRoom(@RequestBody GameRoomDTO gameRoomDTO,
                                      @AuthenticationPrincipal User user) {
        return gameRoomService.createGameRoom(gameRoomDTO, user);
    }

    @PostMapping("/join")
    public GameRoomDTO joinGameRoom(@RequestBody JoinGameRoomDTO joinGameRoomDTO,
                                    @AuthenticationPrincipal User user) {
        return gameRoomService.joinGameRoom(joinGameRoomDTO, user);
    }

    @GetMapping
    public List<GameRoomDTO> getAllGameRooms() {
        return gameRoomService.getAllGameRooms();
    }
}
