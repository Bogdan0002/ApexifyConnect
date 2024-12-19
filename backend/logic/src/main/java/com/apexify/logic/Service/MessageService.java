package com.apexify.logic.Service;

import com.apexify.logic.DTO.MessageRequestDTO;
import com.apexify.logic.DTO.MessageResponseDTO;
import com.apexifyconnect.DAO.interfaces.MessageDAO;
import com.apexifyconnect.DAO.interfaces.UserDAO;
import com.apexifyconnect.Model.Message;
import com.apexifyconnect.Model.User;
import com.apexifyconnect.Repository.MessageRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageService {
    private final MessageDAO messageDAO;
    private final UserDAO userDAO;

    public MessageService(MessageDAO messageDAO, UserDAO userDAO) {
        this.messageDAO = messageDAO;
        this.userDAO = userDAO;
    }

    public MessageResponseDTO sendMessage(MessageRequestDTO requestDTO, String senderEmail) {
        User sender = userDAO.findByEmail(senderEmail)
                .orElseThrow(() -> new RuntimeException("Sender not found"));
        User recipient = userDAO.findByEmail(requestDTO.getRecipientEmail())
                .orElseThrow(() -> new RuntimeException("Recipient not found"));

        Message message = new Message();
        message.setSender(sender);
        message.setRecipient(recipient);
        message.setContent(requestDTO.getContent());
        message.setTimestamp(LocalDateTime.now());
        message.setRead(false);

        return convertToDTO(messageDAO.save(message));
    }

    public List<MessageResponseDTO> getConversation(String userEmail1, String userEmail2) {
        return messageDAO.findConversation(userEmail1, userEmail2)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public MessageResponseDTO markMessageAsRead(Long messageId) {
        messageDAO.markAsRead(messageId);
        return convertToDTO(messageDAO.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message not found")));
    }

    private MessageResponseDTO convertToDTO(Message message) {
        MessageResponseDTO dto = new MessageResponseDTO();
        dto.setId(message.getId());
        dto.setSenderEmail(message.getSender().getEmail());
        dto.setRecipientEmail(message.getRecipient().getEmail());
        dto.setContent(message.getContent());
        dto.setTimestamp(message.getTimestamp());
        dto.setRead(message.isRead());
        return dto;
    }
}

