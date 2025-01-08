package com.is.uno.service;

import com.is.uno.dao.MessageRepository;
import com.is.uno.dto.api.MessageDTO;
import com.is.uno.model.GameRoom;
import com.is.uno.model.Message;
import com.is.uno.model.Player;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;
    private final GameRoomService gameRoomService;

    public void saveMessage(Long roomId, Player sender, String text) {
        GameRoom gameRoom = gameRoomService.findById(roomId);

        Message message = Message.builder()
                .text(text)
                .time(LocalDateTime.now())
                .room(gameRoom)
                .sender(sender)
                .build();

        messageRepository.save(message);
    }

    public List<MessageDTO> getMessagesByRoomId(Long roomId) {
        List<Message> messages = messageRepository.findAllByRoomId(roomId);
        return messages.stream()
                .map(this::toMessageDTO)
                .collect(Collectors.toList());
    }

    private MessageDTO toMessageDTO(Message message) {
        return MessageDTO.builder()
                .senderName(message.getSender().getInGameName())
                .text(message.getText())
                .time(message.getTime())
                .build();
    }
}
