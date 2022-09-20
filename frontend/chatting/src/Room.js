import { Stomp } from "@stomp/stompjs";
import axios from "axios";
import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import SockJS from "sockjs-client";

const Room = () => {
  const [username, setUsername] = useState("");
  const [roomID, setRoomID] = useState();
  const [roomName, setRoomName] = useState("");
  let stompClient = null;
  let navigate = useNavigate();

  //connet 함수
  const connect = () => {
    if (username) {
      var socket = new SockJS("http://localhost:8080//ws");
      stompClient = Stomp.over(socket);

      stompClient.connect({}, onConnected, () => {
        console.log("Error");
      });
    }
  };

  const onConnected = async () => {
    await axios
      .get("/chat/room")
      .then((res) => {
        console.log(res.data);
        setRoomID(res.data.roomId);
        setRoomName(res.data.roomName);
        //받은 roomID를 기반으로 subscribe 주소 열기
        stompClient.subscribe(
          `/queue/room/${res.data.roomId}`,
          onMessageReceived
        );

        console.log("send 이전");
        // Tell your username to the server
        stompClient.send(
          '/app/chat/addUser',
          {},
          JSON.stringify({ sender: username, type: "JOIN"})
        );

        console.log("send 이후");

        navigate("/chatRoom");
      });
  };

  const onMessageReceived = (payload) => {
    var message = JSON.parse(payload.body);

    if (message.type === "JOIN") {
      console.log("Join " + message.sender);
    } else if (message.type === "LEAVE") {
      console.log("Leave ", message.sender);
    } else {
      console.log("Chatting: ", message.sender);
    }
  };
  //버튼 클릭 시
  const onClick = () => {
    connect();
  };
  const onChange = (e) => {
    setUsername(e.target.value);
  };
  return (
    <div className="App">
      <input placeholder="username 입력" onChange={onChange}></input>
      <button onClick={onClick}>채팅방 입장하기</button>
    </div>
  );
};

export default Room;
