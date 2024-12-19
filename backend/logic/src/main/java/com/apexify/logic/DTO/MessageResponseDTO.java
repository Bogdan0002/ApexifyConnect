package com.apexify.logic.DTO;

import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter
public class MessageResponseDTO {
    private Long id;
    private String senderEmail;
    private String recipientEmail;
    private String content;
    private LocalDateTime timestamp;
    private boolean read;
}
