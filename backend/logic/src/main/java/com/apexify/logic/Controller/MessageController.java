package com.apexify.logic.Controller;

import com.apexify.logic.DTO.MessageRequestDTO;
import com.apexify.logic.DTO.MessageResponseDTO;
import com.apexify.logic.Service.MessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/messages")
@CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*")
public class MessageController {
    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping("/send")
    public ResponseEntity<MessageResponseDTO> sendMessage(
            @RequestBody MessageRequestDTO request,
            @RequestHeader("Authorization") String token) {
        String senderEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(messageService.sendMessage(request, senderEmail));
    }

    @GetMapping("/conversation")
    public ResponseEntity<List<MessageResponseDTO>> getConversation(
            @RequestParam String otherUserEmail) {
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(messageService.getConversation(currentUserEmail, otherUserEmail));
    }

    @PatchMapping("/{messageId}/read")
    public ResponseEntity<MessageResponseDTO> markAsRead(@PathVariable Long messageId) {
        return ResponseEntity.ok(messageService.markMessageAsRead(messageId));
    }
}
