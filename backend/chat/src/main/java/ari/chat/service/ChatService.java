package ari.chat.service;

import ari.chat.Repository.JoinRepository;
import ari.chat.Repository.MessageRepository;
import ari.chat.Repository.RoomRepository;
import ari.chat.Repository.UserRepository;
import ari.chat.domain.ChatMessage;
import ari.chat.domain.ChatRoom;
import ari.chat.domain.ChatRoomJoin;
import ari.chat.domain.User;
import ari.chat.dto.FindRoomDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional(readOnly = true)
@Slf4j
@RequiredArgsConstructor
public class ChatService {

    private final RoomRepository roomRepository;
    private final MessageRepository messageRepository;
    private final JoinRepository joinRepository;
    private final UserRepository userRepository;

    private Map<String, ChatRoom> chatRooms;

    @PostConstruct
    //의존관게 주입완료되면 실행되는 코드
    private void init() {
        chatRooms = new LinkedHashMap<>();
    }

    //채팅방 불러오기
    public List<ChatRoom> findAllRoom() {
        /*
        //채팅방 최근 생성 순으로 반환
        List<ChatRoom> result = new ArrayList<>(chatRooms.values());
        Collections.reverse(result);

        */

        return roomRepository.findAll();
    }

    //채팅방 하나 불러오기
    public FindRoomDto findById(String roomId) {
        //return chatRooms.get(roomId);

        ChatRoom chatRoom = roomRepository.findById(Long.valueOf(roomId)).orElse(null);
        FindRoomDto findRoomDto = FindRoomDto.createChatRoomDto(
                chatRoom.getRoomId(), chatRoom.getRoomName(), getGroups(chatRoom.getMessages()));

        return findRoomDto;
    }

    @Transactional
    //채팅방 생성
    public ChatRoom createRoom(String name, String userName1, String userName2) {
        ChatRoom chatRoom = ChatRoom.create(name);
        roomRepository.save(chatRoom);

        User user1 = saveUser(new User(userName1));
        User user2 = saveUser(new User(userName2));

        ChatRoomJoin chatRoomJoin1 = new ChatRoomJoin(user1, chatRoom);
        ChatRoomJoin chatRoomJoin2 = new ChatRoomJoin(user2, chatRoom);

        joinRepository.save(chatRoomJoin1);
        joinRepository.save(chatRoomJoin2);

        return chatRoom;
    }

    @Transactional
    public User saveUser(User user){
        userRepository.save(user);
        return user;
    }

    // 채팅이 모여져있는 messages를 날짜별->오름차순으로 분류
    public Map<LocalDate, List<ChatMessage>> getGroups(List<ChatMessage> messages){
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
    public void saveMessage(ChatMessage message, Long roomId, String sender) {
        message.setCreateDate(LocalDate.now());
        message.setCreateTime(LocalDateTime.now());

        ChatRoom chatRoom = roomRepository.findById(roomId).orElse(null);
        chatRoom.addMessage(message);

        message.setChatRoom(chatRoom);
        message.setSender(sender);

        messageRepository.save(message);
    }
}
