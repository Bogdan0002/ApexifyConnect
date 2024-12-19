package com.apexifyconnect.DAO.impl;

import com.apexifyconnect.DAO.interfaces.MessageDAO;
import com.apexifyconnect.Model.Message;
import com.apexifyconnect.Repository.MessageRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class MessageDAOImpl implements MessageDAO {
    private final MessageRepository messageRepository;

    public MessageDAOImpl(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public Message save(Message message) {
        return messageRepository.save(message);
    }

    @Override
    public Optional<Message> findById(Long id) {
        return messageRepository.findById(id);
    }

    @Override
    public List<Message> findConversation(String user1Email, String user2Email) {
        return messageRepository.findConversation(user1Email, user2Email);
    }

    @Override
    public void markAsRead(Long messageId) {
        messageRepository.findById(messageId).ifPresent(message -> {
            message.setRead(true);
            messageRepository.save(message);
        });
    }
}
