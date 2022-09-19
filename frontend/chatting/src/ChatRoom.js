import { useState } from "react";

const ChatRoom = () => {
  const [chat, setChat] = useState("");

  const onSubmit = (e) => {
    console.log(chat);
    e.preventDefault();
  };

  const onChange = (e) => {
    setChat(e.target.value);
  };

  return (
    <div>
      <form>
        <input onChange={onChange} value={chat} type="text"></input>
        <button type="submit" onClick={onSubmit}>
          전송
        </button>
      </form>
    </div>
  );
};

export default ChatRoom;
