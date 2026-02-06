package ru.sicampus.bootcamp2026.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.sicampus.bootcamp2026.dto.InvitationDTO;
import ru.sicampus.bootcamp2026.service.InvitationService;

import java.util.List;

@RestController
@RequestMapping("/api/invitations")
@RequiredArgsConstructor
public class InvitationController {

    private final InvitationService invitationService;

    @PostMapping("/{id}/respond")
    public InvitationDTO respondToInvitation(
            @PathVariable Long id,
            @RequestParam String response) { // ?response=ACCEPT
        return invitationService.respondToInvitation(id, response);
    }

    @GetMapping("/user/{userId}")
    public List<InvitationDTO> getUserInvitations(@PathVariable Long userId) {
        return invitationService.getUserInvitations(userId);
    }
}