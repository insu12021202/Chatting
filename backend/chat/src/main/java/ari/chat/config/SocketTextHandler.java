package ari.chat.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Set;

public class SocketTextHandler extends TextWebSocketHandler {

    // 웹소켓 연결정보를 담고있는 session
    private final Set<WebSocketSession> sessions = ConcurrentHashMap.newKeySet();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        JsonObject jsonObject = new JsonObject();
        String username = jsonObject.get("user").toString();

        for(WebSocketSession s : sessions){
            s.sendMessage(new TextMessage("Hi!" + username + "! How may I help you?"));
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session);
    }
}
