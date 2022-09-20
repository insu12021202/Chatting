import axios from "axios";
import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import SockJS from "sockjs-client";
import { Stomp } from "@stomp/stompjs";

const Room = ({ username }) => {
  let navigate = useNavigate();
  //버튼 클릭 시
  const onClick = () => {
    navigate("/chatRoom");
  };
  return (
    <div className="App">
      <span>{username}님 안녕하세요</span>
      <button onClick={onClick}>채팅방 입장하기</button>
    </div>
  );
};

export default Room;
