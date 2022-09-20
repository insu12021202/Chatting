import { useNavigate } from "react-router-dom";

const Room = ({ username }) => {
  let navigate = useNavigate();
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
