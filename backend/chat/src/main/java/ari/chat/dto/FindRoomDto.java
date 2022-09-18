package ari.chat.dto;

import ari.chat.domain.ChatMessage;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.*;

@Data
public class FindRoomDto {

    private Long roomId;
    private String roomName;
    private List<MessageGroup> messages;

    private FindRoomDto(){};

    public static FindRoomDto createChatRoomDto(Long roomId, String roomName, Map<LocalDate, List<ChatMessage>> groups){
        FindRoomDto findRoomDto = new FindRoomDto();

        findRoomDto.setRoomId(roomId);
        findRoomDto.setRoomName(roomName);

        List<MessageGroup> chatting = new ArrayList<>();
        Set<LocalDate> keys = groups.keySet();

        for(LocalDate key : keys){
            MessageGroup messageGroup = new MessageGroup(key, groups.get(key));
            chatting.add(messageGroup);
        }

        findRoomDto.setMessages(chatting);

        return findRoomDto;
    }

    @Data
    @AllArgsConstructor
    private static class MessageGroup{
        private LocalDate date;
        private List<ChatMessage> messages;
    }
}
