import { Stomp } from "@stomp/stompjs";
import axios from "axios";
import { useEffect, useState } from "react";
import SockJS from "sockjs-client";
import "./App.css";

function App() {
  const [username, setUsername] = useState("");
  const [roomID, setRoomID] = useState();
  const [roomName, setRoomName] = useState("");
  let stompClient = null;

  //username과 roomName을 보내주고 roomID와 roomName 받아오기
  const getRoomData = async () => {
    // await axios.get("/hello").then((res) => {
    //   console.log(res.data);
    // });
    await axios
      .post("/chat/room", { name: "roomName", user1: username })
      .then((res) => {
        setRoomID(res.data.roomID);
        setRoomName(res.data.roomName);
        console.log("aa", res.data);
      });
  };
  //connet 함수
  const connect = () => {
    if (username) {
      var socket = new SockJS("/ws");
      stompClient = Stomp.over(socket);

      stompClient.connect({}, onConnected, () => {
        console.log("Error");
      });
    }
  };

  const onConnected = async () => {
    await getRoomData();
    if (roomID) {
      stompClient.subscribe(`/queue/room/${roomID}`, onMessageReceived);

      // Tell your username to the server
      stompClient.send(
        "/app/chat/addUser",
        {},
        JSON.stringify({ sender: username, type: "JOIN" })
      );
    }
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
}

export default App;
