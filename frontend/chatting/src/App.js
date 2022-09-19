import { useEffect, useState } from "react";
import "./App.css";

function App() {
  const [username, setUsername] = useState("");
  const onClick = () => {
    console.log(username);
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
