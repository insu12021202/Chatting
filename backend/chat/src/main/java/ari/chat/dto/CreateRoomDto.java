package ari.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateRoomDto {

    private Long roomId;
    private String roomName;

}
