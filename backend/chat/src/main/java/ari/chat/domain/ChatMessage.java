package ari.chat.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ChatMessage implements Comparable<ChatMessage>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private MessageType type;

    //채팅방 ID
    @ManyToOne(fetch = FetchType.LAZY)
    private ChatRoom chatRoom;

    //보내는 사람
    private String sender;
    //내용
    private String content;
    //시간
    private LocalDateTime createTime;
    private LocalDate createDate;

    @Override
    public int compareTo(ChatMessage o) {
        return (int)(this.getCreateTime().getNano() - o.getCreateTime().getNano());
    }

    public enum MessageType {
        CHAT, JOIN, LEAVE
    }
}
