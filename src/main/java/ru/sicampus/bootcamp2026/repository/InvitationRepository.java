package ru.sicampus.bootcamp2026.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sicampus.bootcamp2026.entity.Invitation;

import java.util.List;


@Repository
public interface InvitationRepository extends JpaRepository<Invitation, Long> {
    List<Invitation> findByUserId(Long userId);
    List<Invitation> findByMeetingId(Long meetingId);
}
