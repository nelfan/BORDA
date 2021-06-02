package com.softserve.borda.repositories;

import com.softserve.borda.entities.Board;
import com.softserve.borda.entities.User;
import com.softserve.borda.entities.UserBoardRelation;
import com.softserve.borda.entities.UserBoardRole;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserBoardRelationRepositoryTest {

    @Autowired
    UserBoardRelationRepository userBoardRelationRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    BoardRepository boardRepository;

    @Autowired
    BoardRoleRepository boardRoleRepository;

    @Test
    void shouldInsertAndReturnUserBoardRelation() {
        User user = new User();
        user.setUsername("user");
        user.setPassword("pass");
        user.setEmail("email");
        user.setFirstName("FirstName");
        user.setLastName("LastName");
        userRepository.save(user);
        UserBoardRole owner = new UserBoardRole();
        owner.setName("owner");
        boardRoleRepository.save(owner);
        Board board = new Board();
        board.setName("Board");
        UserBoardRelation expected = new UserBoardRelation();
        expected.setUser(user);
        expected.setBoard(board);
        expected.setUserBoardRole(owner);
        board.getUserBoardRelations().add(expected);
        user.getUserBoardRelations().add(expected);
        boardRepository.save(board);
        userRepository.save(user);
        UserBoardRelation actual = userBoardRelationRepository.save(expected);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void shouldInsertAndDeleteUserBoardRelation() {
        User user = new User();
        user.setUsername("user");
        user.setPassword("pass");
        user.setEmail("email");
        user.setFirstName("FirstName");
        user.setLastName("LastName");
        userRepository.save(user);
        UserBoardRole owner = new UserBoardRole();
        owner.setName("owner");
        boardRoleRepository.save(owner);
        Board board = new Board();
        board.setName("Board");
        UserBoardRelation userBoardRelation = new UserBoardRelation();
        userBoardRelation.setUser(user);
        userBoardRelation.setBoard(board);
        userBoardRelation.setUserBoardRole(owner);
        board.getUserBoardRelations().add(userBoardRelation);
        user.getUserBoardRelations().add(userBoardRelation);
        boardRepository.save(board);
        userRepository.save(user);
        userBoardRelationRepository.save(userBoardRelation);
        userBoardRelationRepository.deleteById(userBoardRelation.getId());
        Assertions.assertFalse(userBoardRelationRepository.findById(userBoardRelation.getId()).isPresent());
    }

    @Test
    void shouldInsertAndUpdateUserBoardRelation() {User user = new User();
        user.setUsername("user");
        user.setPassword("pass");
        user.setEmail("email");
        user.setFirstName("FirstName");
        user.setLastName("LastName");
        userRepository.save(user);
        UserBoardRole owner = new UserBoardRole();
        owner.setName("owner");
        boardRoleRepository.save(owner);
        Board board = new Board();
        board.setName("Board");
        UserBoardRelation userBoardRelation = new UserBoardRelation();
        userBoardRelation.setUser(user);
        userBoardRelation.setBoard(board);
        userBoardRelation.setUserBoardRole(owner);
        board.getUserBoardRelations().add(userBoardRelation);
        user.getUserBoardRelations().add(userBoardRelation);
        boardRepository.save(board);
        userRepository.save(user);
        userBoardRelationRepository.save(userBoardRelation);
        UserBoardRole collaborator = new UserBoardRole();
        collaborator.setName("collaborator");
        boardRoleRepository.save(collaborator);
        userBoardRelation.setUserBoardRole(collaborator);
        UserBoardRelation expected = userBoardRelationRepository.save(userBoardRelation);
        UserBoardRelation actual = userBoardRelationRepository.getOne(userBoardRelation.getId());
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void shouldWorkProperly() {
        List<User> users = new ArrayList<>();
        List<Board> boards = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            User user = new User();
            user.setUsername("user" + i);
            user.setPassword("pass" + i);
            user.setEmail("email" + i);
            user.setFirstName("FirstName" + i);
            user.setLastName("LastName" + i);
            userRepository.save(user);
            users.add(user);
        }
        UserBoardRole owner = new UserBoardRole();
        owner.setName("owner");
        boardRoleRepository.save(owner);
        UserBoardRole collaborator = new UserBoardRole();
        collaborator.setName("collaborator");
        boardRoleRepository.save(collaborator);
        for (int i = 0; i < 3; i++) {
            Board board = new Board();
            board.setName("Board" + i);
            UserBoardRelation userBoardRelation = new UserBoardRelation();
            userBoardRelation.setUser(users.get(i));
            userBoardRelation.setBoard(board);
            userBoardRelation.setUserBoardRole(owner);
            board.getUserBoardRelations().add(userBoardRelation);
            users.get(i).getUserBoardRelations().add(userBoardRelation);
            boardRepository.save(board);
            userRepository.save(users.get(i));
            boards.add(board);
        }
        UserBoardRelation expected = new UserBoardRelation();
        expected.setUser(users.get(2));
        expected.setBoard(boards.get(1));
        expected.setUserBoardRole(collaborator);
        UserBoardRelation actual = userBoardRelationRepository.save(expected);
        users.get(2).getUserBoardRelations().add(expected);
        boards.get(1).getUserBoardRelations().add(expected);
        userRepository.save(users.get(2));
        boardRepository.save(boards.get(1));
        Assertions.assertEquals(expected, actual);
    }
}