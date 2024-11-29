package com.is.uno.controller;

import com.is.uno.dto.GameRoom.GameRoomDTO;
import com.is.uno.dto.GameRoom.JoinGameRoomDTO;
import com.is.uno.service.GameRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5175")
@RequestMapping("/game_room")
public class GameRoomController {
    private final GameRoomService gameRoomService;

    @PostMapping
    public GameRoomDTO createGameRoom(@RequestBody GameRoomDTO gameRoomDTO) {
        return gameRoomService.createGameRoom(gameRoomDTO);
    }

    @PostMapping("/join")
    public GameRoomDTO joinGameRoom(@RequestBody JoinGameRoomDTO joinGameRoomDTO) {
        return gameRoomService.joinGameRoom(joinGameRoomDTO);
    }

    @GetMapping
    public List<GameRoomDTO> getAllGameRooms() {
        return gameRoomService.getAllGameRooms();
    }
}
