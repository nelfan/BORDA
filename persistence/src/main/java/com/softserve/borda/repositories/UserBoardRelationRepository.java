package com.softserve.borda.repositories;

import com.softserve.borda.entities.UserBoardRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserBoardRelationRepository extends JpaRepository<UserBoardRelation, Long> {

    List<UserBoardRelation> findAllByUserId(Long userId);

    List<UserBoardRelation> findAllByUserIdAndUserBoardRoleId(Long userId, Long userBoardRoleId);

    List<UserBoardRelation> findAllByBoardIdAndUserBoardRoleId(Long boardId, Long userBoardRoleId);

    List<UserBoardRelation> findAllByBoardId(Long boardId);
}
