package ari.chat.controller;

import ari.chat.domain.ChatRoom;
import ari.chat.dto.CreateRoomDto;
import ari.chat.dto.FindRoomDto;
import ari.chat.dto.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ari.chat.service.ChatService;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatRoomController {
    private final ChatService chatService;

    // 모든 채팅방 목록 반환
    @GetMapping("/rooms")
    @ResponseBody
    public Result<?> room() {

        List<CreateRoomDto> dto = chatService.findAllRoom().stream()
                .map(chatRoom -> new CreateRoomDto(chatRoom.getRoomId(), chatRoom.getRoomName()))
                .collect(Collectors.toList());

        return new Result(dto);
    }

    // 채팅방 생성
    @PostMapping("/room")
    @ResponseBody
    public CreateRoomDto createRoom(@RequestBody Map<String, Object> maps) {

        String name = (String)maps.get("name");
        String user1 = (String)maps.get("user1");

        String user2 = "김우진";
        ChatRoom chatRoom = chatService.createRoom(name, user1, user2);

        return new CreateRoomDto(chatRoom.getRoomId(), chatRoom.getRoomName());
    }

    // 특정 채팅방 조회
    @GetMapping("/room/{roomId}")
    @ResponseBody
    public FindRoomDto roomInfo(@PathVariable String roomId) {
        return chatService.findById(roomId);
    }

    /*
    // 채팅방 입장 화면
    @GetMapping("/room/enter/{roomId}")
    public String roomDetail(Model model, @PathVariable String roomId) {

        model.addAttribute("roomId", roomId);
        return "/chat/roomdetail";
    }

    // 채팅 리스트 화면
    @GetMapping("/room")
    public String rooms(Model model) {
        return "/chat/room";
    }
    */
}
