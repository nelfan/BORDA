package com.softserve.borda.services;

import com.softserve.borda.entities.User;
import com.softserve.borda.entities.UserBoardRelation;

import java.util.List;

public interface UserService {
    List<User> getAll();

    User getUserById(Long id);

    User create(User user);

    User update(User user);

    boolean deleteUserById(Long id);

    User getUserByUsername(String username);

    List<User> getUsersByBoardId(Long boardId);

    List<User> getUsersByBoardIdAndUserBoardRoleId(Long boardId,
                                                   Long userBoardRoleId);

    List<User> getUsersByUserBoardRelations(List<UserBoardRelation> userBoardRelations);

    Boolean existsUserByUsername(String username);

    List<User> getAllMembersByTicketId(Long ticketId);
}
