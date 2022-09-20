import axios from "axios";
import { useEffect, useState } from "react";
import SockJS from "sockjs-client";
import { Stomp } from "@stomp/stompjs";

const ChatRoom = ({ username }) => {
  const [chat, setChat] = useState("");
  let stompClient = null;

  let chatLog = [
    {
      "2022/09/01": [
        { username: "a", chat: "hihihi", time: "12:10" },
        { username: "b", chat: "hihihiasd", time: "12:13" },
        { username: "c", chat: "hihihi", time: "12:15" },
      ],
    },
    {
      "2022/09/02": [
        { username: "a", chat: "hihihi", time: "12:10" },
        { username: "b", chat: "hihihiasd", time: "12:13" },
        { username: "c", chat: "hihihi", time: "12:15" },
      ],
    },
    {
      "2022/09/03": [
        { username: "a", chat: "hihihi", time: "12:10" },
        { username: "b", chat: "hihihiasd", time: "12:13" },
        { username: "c", chat: "hihihi", time: "12:15" },
      ],
    },
  ];

  useEffect(() => {
    connect();
  }, []);
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
    await axios.get("/chat/room").then((res) => {
      //여기서 채팅 기록 받아오기
      console.log(res.data);
      //받은 roomID를 기반으로 subscribe 주소 열기
      stompClient.subscribe("/topic/public", onMessageReceived);

      console.log("send 이전");
      // Tell your username to the server
      stompClient.send(
        "/app/chat/addUser",
        {},
        JSON.stringify({ sender: username, type: "JOIN" })
      );

      console.log("send 이후");
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

  const sendMessage = () => {
    if (chat && stompClient) {
      var chatMessage = {
        sender: username,
        content: chat,
        type: "CHAT",
      };
      stompClient.send("app/chat/sendMessage", {}, JSON.stringify(chatMessage));
      setChat("");
    }
  };

  const onSubmit = (e) => {
    e.preventDefault();
    sendMessage();
  };

  const onChange = (e) => {
    setChat(e.target.value);
  };

  return (
    <>
      <div></div>
      <div>
        <form onSubmit={onSubmit}>
          <input onChange={onChange} value={chat} type="text"></input>
          <button type="submit">전송</button>
        </form>
      </div>
    </>
  );
};

export default ChatRoom;
