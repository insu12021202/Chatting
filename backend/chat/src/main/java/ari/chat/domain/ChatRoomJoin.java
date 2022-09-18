package ari.chat.domain;

import javax.persistence.*;

@Entity
public class ChatRoomJoin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private ChatRoom chatRoom;

    public ChatRoomJoin(User user, ChatRoom chatRoom){
        this.user = user;
        this.chatRoom = chatRoom;
    }

}
