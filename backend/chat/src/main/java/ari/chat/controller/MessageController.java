package ari.chat.controller;

import ari.chat.domain.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MessageController {

    private final SimpMessageSendingOperations sendingOperations;

    // 메세지의 destination이 /chat/message이면 해당 sendMessage() method가 불리도록 해줌
    @MessageMapping("/chat/sendMessage")
    //@SendTo("/topic/public")
    public void enter(@Payload ChatMessage message) {
       // if (ChatMessage.MessageType.JOIN.equals(message.getType())) {
       //     message.setContent(message.getSender()+"님이 입장하였습니다.");
       // }

        sendingOperations.convertAndSend("/topic/public",message);
    }

    @MessageMapping("/chat/addUser")
    public void addUser(@Payload ChatMessage message, SimpMessageHeaderAccessor headerAccessor){
        headerAccessor.getSessionAttributes().put("username", message.getSender());

        sendingOperations.convertAndSend("/topic/public",message);
    }
}
