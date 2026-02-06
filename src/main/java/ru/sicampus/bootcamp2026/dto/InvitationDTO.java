package ru.sicampus.bootcamp2026.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class InvitationDTO {
    private Long id;
    private Long meetingId;
    private Long personId;
    private String status;
    private LocalDateTime respondedAt;
    private LocalDateTime createdAt;
}
