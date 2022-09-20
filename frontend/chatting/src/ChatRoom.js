import axios from "axios";
import { useEffect, useState } from "react";
import SockJS from "sockjs-client";
import { Stomp } from "@stomp/stompjs";

let socket = new SockJS("http://localhost:8080//ws");
let stompClient = Stomp.over(socket);

const ChatRoom = ({ username }) => {
  const [chat, setChat] = useState("");
  const [message, setMessage] = useState("");
  const [messageList, setMessageList] = useState("");

  useEffect(() => {
    connect();
  }, []);
  //connet 함수
  const connect = () => {
    if (username) {
      stompClient.connect({}, onConnected, () => {
        console.log("Error");
      });
    }
  };

  const onConnected = async () => {
    await axios.get("/chat/room").then((res) => {
      //여기서 채팅 기록 받아오기
      console.log(res.data);
      setMessageList(res.data.data);
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
      console.log("Chatting: ", message.content);
      setMessage((prev) => [...prev, message.content]);
    }
  };

  const sendMessage = () => {
    if (chat && stompClient) {
      console.log("send 실행됨, chat: ", chat);
      var chatMessage = {
        sender: username,
        content: chat,
        type: "CHAT",
      };
      stompClient.send(
        "/app/chat/sendMessage",
        {},
        JSON.stringify(chatMessage)
      );
      setChat("");
    }
  };

  const onSubmit = () => {
    sendMessage();
  };

  const onChange = (e) => {
    setChat(e.target.value);
  };
  if (!messageList) {
    return <div>로딩 중</div>;
  } else {
    console.log("asdasd", messageList);
    return (
      <>
        <div></div>
        <div>
          <input onChange={onChange} value={chat} type="text"></input>
          <button type="submit" onClick={onSubmit}>
            전송
          </button>
          <div>
            {messageList &&
              messageList.map((item, i) => {
                return (
                  <div key={i}>
                    <span key={i}>{item.date}</span>
                    {item.messages.map((item, i) => {
                      return <div key={i}>{item.content}</div>;
                    })}
                  </div>
                );
              })}
            {message &&
              message.map((item, i) => {
                return <div key={i}>{item}</div>;
              })}
          </div>
        </div>
      </>
    );
  }
};

export default ChatRoom;
