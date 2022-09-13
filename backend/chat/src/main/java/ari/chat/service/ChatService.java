package ari.chat.service;

import ari.chat.Repository.JoinRepository;
import ari.chat.Repository.MessageRepository;
import ari.chat.Repository.RoomRepository;
import ari.chat.Repository.UserRepository;
import ari.chat.domain.ChatRoom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
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
    public ChatRoom findById(String roomId) {
        //return chatRooms.get(roomId);

        return roomRepository.findById(Long.valueOf(roomId)).orElse(null);
    }

    @Transactional
    //채팅방 생성
    public ChatRoom createRoom(String name) {
        /*
        ChatRoom chatRoom = ChatRoom.create(name);
        chatRooms.put(chatRoom.getRoomId(), chatRoom);
        return chatRoom;
        */

        ChatRoom chatRoom = ChatRoom.create(name);
        roomRepository.save(chatRoom);

        return chatRoom;
    }
}
