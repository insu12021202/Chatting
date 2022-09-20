package ari.chat.controller;

import ari.chat.domain.ChatMessage;
import ari.chat.domain.User;
import ari.chat.dto.MessageDto;
import ari.chat.dto.Result;
import ari.chat.dto.RoomDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ari.chat.service.ChatService;

import java.time.LocalDate;
import java.util.*;

@Controller
@RequiredArgsConstructor
@Slf4j
//@RequestMapping("/chat")
public class ChatRoomController {
    private final ChatService chatService;
    private final SimpMessageSendingOperations sendingOperations;

    @MessageMapping("/chat/addUser")
    public void addUser(@Payload ChatMessage message, SimpMessageHeaderAccessor headerAccessor){

        headerAccessor.getSessionAttributes().put("username", message.getSender());

        chatService.saveUser(new User(message.getSender()));

        MessageDto messageDto = MessageDto.createMessageDto(message.getSender(), message.getCreateTime(),
                message.getContent(), ChatMessage.MessageType.JOIN);

        sendingOperations.convertAndSend("/topic/public", message);
    }

    @MessageMapping("/chat/sendMessage")
    public void enter(@Payload ChatMessage message) {
        if (ChatMessage.MessageType.JOIN.equals(message.getType())) {
            message.setContent(message.getSender()+"님이 입장하였습니다.");
        }

        chatService.saveMessage(message, message.getSender());

        MessageDto messageDto = MessageDto.createMessageDto(message.getSender(), message.getCreateTime(),
                message.getContent(), ChatMessage.MessageType.CHAT);

        sendingOperations.convertAndSend("/topic/public", message);
    }

    // 채팅방 생성
    @GetMapping("/chat/room")
    @ResponseBody
    public Result enterRoom() {
        Map<LocalDate, List<ChatMessage>> chatting = chatService.getMessageGroups();
        List<RoomDto> list = new ArrayList<>();

        int id = 0;
        Set<LocalDate> keys = chatting.keySet();
        for(LocalDate key : keys){
            RoomDto dto = RoomDto.createRoomDto(key, chatting.get(key));
            list.add(dto);
        }

        return new Result(list);
    }

}
