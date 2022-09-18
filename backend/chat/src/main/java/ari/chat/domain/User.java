package ari.chat.domain;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    public User(String name){
        this.name = name;
    }

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<ChatRoomJoin> joinRooms;

    public void addRoom(ChatRoomJoin chatRoomJoin){
        this.joinRooms.add(chatRoomJoin);
    }
}
