package ari.chat.controller;

import ari.chat.domain.ChatMessage;
import ari.chat.domain.User;
import ari.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MessageController {

    private final ChatService chatService;
    private final SimpMessageSendingOperations sendingOperations;

    // 메세지의 destination이 /chat/message이면 해당 sendMessage() method가 불리도록 해줌
    @MessageMapping("/chat/sendMessage") // 클라이언트에서 /app/chat/sendMessage로 요청되는 메시지
    //@SendTo("/topic/public")
    public void enter(@Payload ChatMessage message, @RequestParam(required = true) Long roomId, @RequestParam(required = true) String sender) {
        if (ChatMessage.MessageType.JOIN.equals(message.getType())) {
            message.setContent(message.getSender()+"님이 입장하였습니다.");
       }

        chatService.saveMessage(message, roomId, sender);

        sendingOperations.convertAndSend("/queue/room/"+roomId, message);
    }

    @MessageMapping("/chat/addUser")
    public void addUser(@Payload ChatMessage message, SimpMessageHeaderAccessor headerAccessor, @RequestParam Long roomId){

        headerAccessor.getSessionAttributes().put("username", message.getSender());

        String username = headerAccessor.getSessionAttributes().get("username").toString();
        User user = new User(username);
        chatService.saveUser(user);

        sendingOperations.convertAndSend("/queue/room/"+roomId, message);
    }
}
