package ari.chat.service;

import ari.chat.Repository.MessageRepository;
import ari.chat.Repository.UserRepository;
import ari.chat.domain.ChatMessage;
import ari.chat.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

@Service
@Transactional(readOnly = true)
@Slf4j
@RequiredArgsConstructor
public class ChatService {
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    @PostConstruct
    //의존관게 주입완료되면 실행되는 코드
    private void init() {
        //chatRooms = new LinkedHashMap<>();
    }

    @Transactional
    public User saveUser(User user){
        userRepository.save(user);
        return user;
    }

    public List<ChatMessage> getMessages(){
        return messageRepository.findAll();
    }

    // 채팅이 모여져있는 messages를 날짜별->오름차순으로 분류
    public Map<LocalDate, List<ChatMessage>> getMessageGroups(){
        List<ChatMessage> messages = messageRepository.findAll();
        TreeMap<LocalDate, List<ChatMessage>> groups = new TreeMap<>();
        List<ChatMessage> chatting;

        for(ChatMessage message : messages){
            List<ChatMessage> group = groups.get(message.getCreateDate());
            LocalDate date = message.getCreateDate();

            if(group == null)
                chatting = new ArrayList<>();
            else
                chatting = groups.get(message.getCreateDate());

            chatting.add(message);
            groups.put(date, chatting);
        }

        Set<LocalDate> keys = groups.keySet();
        for(LocalDate key : keys){
            chatting = groups.get(key);
            Collections.sort(chatting);
            groups.put(key, chatting);
        }

        return groups;
    }

    @Transactional
    public void saveMessage(ChatMessage message, String sender) {
        message.setCreateDate(ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDate());
        message.setCreateTime(Timestamp.valueOf(ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime()));

        message.setSender(sender);

        messageRepository.save(message);
    }
}
