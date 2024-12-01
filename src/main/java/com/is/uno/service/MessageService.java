package com.is.uno.service;

import com.is.uno.dao.MessageRepository;
import com.is.uno.dto.MessageDTO;
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
    private final PlayerService playerService;

    public MessageDTO sendMessage(Long roomId, Long senderId, String text) {
        GameRoom gameRoom = gameRoomService.findById(roomId);
        Player player = playerService.findById(senderId);

        Message message = Message.builder()
                .text(text)
                .time(LocalDateTime.now())
                .room(gameRoom)
                .sender(player)
                .build();

        message = messageRepository.save(message);
        return toMessageDTO(message);
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
