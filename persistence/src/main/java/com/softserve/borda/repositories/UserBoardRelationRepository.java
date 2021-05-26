package com.softserve.borda.repositories;

import com.softserve.borda.entities.UserBoardRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserBoardRelationRepository extends JpaRepository<UserBoardRelation, Long> {
}
