package ari.chat.Repository;

import ari.chat.domain.ChatRoomJoin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JoinRepository extends JpaRepository<ChatRoomJoin, Long> {
}
