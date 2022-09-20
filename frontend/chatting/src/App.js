import { BrowserRouter, Route, Routes } from "react-router-dom";
import "./App.css";
import ChatRoom from "./ChatRoom";
import Room from "./Room";

function App() {
  const username = "INSU";
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Room username={username} />}></Route>
        <Route
          path="/chatRoom"
          element={<ChatRoom username={username} />}
        ></Route>
      </Routes>
    </BrowserRouter>
  );
}

export default App;
