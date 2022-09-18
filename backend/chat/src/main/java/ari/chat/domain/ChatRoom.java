package ari.chat.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor
@Entity
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomId;
    private String roomName;

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL)
    private List<ChatMessage> messages;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<ChatRoomJoin> joinRooms;

    public static ChatRoom create(String name) {
        ChatRoom room = new ChatRoom();
        room.roomName = name;

        return room;
    }

    public void addMessage(ChatMessage message){
        this.messages.add(message);
        message.setChatRoom(this);
    }

    public void addRoom(ChatRoomJoin chatRoom){
        this.joinRooms.add(chatRoom);
    }
}
