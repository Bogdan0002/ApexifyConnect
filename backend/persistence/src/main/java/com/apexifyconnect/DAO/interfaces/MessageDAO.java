package com.apexifyconnect.DAO.interfaces;

import com.apexifyconnect.Model.Message;

import java.util.List;
import java.util.Optional;

public interface MessageDAO {
    Message save(Message message);
    Optional<Message> findById(Long id);
    List<Message> findConversation(String user1Email, String user2Email);
    void markAsRead(Long messageId);
}
