package com.softserve.borda.repositories;

import com.softserve.borda.entities.Invitation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InvitationRepository extends JpaRepository<Invitation, Long> {
    Optional<Invitation> findByIsAccepted(Boolean isAccepted);
    List<Invitation> findAllByReceiverId(Long receiverId);
}
