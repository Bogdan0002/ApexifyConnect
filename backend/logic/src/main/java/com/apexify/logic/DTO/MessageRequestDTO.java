package com.apexify.logic.DTO;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageRequestDTO {
    private String recipientEmail;
    private String content;
}
