package com.softserve.borda.repositories;

import com.softserve.borda.entities.User;
import com.softserve.borda.entities.UserBoardRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    List<User> findAllByUserBoardRelationsIn(Collection<List<UserBoardRelation>> userBoardRelations);

    Boolean existsUserByUsername(String username);

    @Query("select u from users u join u.tickets tickets where tickets.id = :ticketId")
    List<User> findAllMembersByTicketId(@Param("ticketId") Long ticketId);
}