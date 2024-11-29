package com.is.uno.controller;

import com.is.uno.dto.MessageDTO;
import com.is.uno.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5175")
@RequestMapping("/message")
public class MessageController {
    private final MessageService messageService;

    @PostMapping("/{roomId}/{senderId}")
    public MessageDTO sendMessage(@PathVariable Long roomId, @PathVariable Long senderId, @RequestBody String text) {
        return messageService.sendMessage(roomId, senderId, text);
    }

    @GetMapping("/{roomId}")
    public List<MessageDTO> getMessagesByRoomId(@PathVariable Long roomId) {
        return messageService.getMessagesByRoomId(roomId);
    }
}
