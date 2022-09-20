package ari.chat.domain;

import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Entity
public class ChatMessage implements Comparable<ChatMessage>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private MessageType type;

    //보내는 사람
    private String sender;
    //내용
    private String content;
    //시간
    private Timestamp createTime;
    private LocalDate createDate;

    @Override
    public int compareTo(ChatMessage o) {
        return (int)(this.getCreateTime().getTime() - o.getCreateTime().getTime());
    }

    public enum MessageType {
        CHAT, JOIN, LEAVE
    }
}
