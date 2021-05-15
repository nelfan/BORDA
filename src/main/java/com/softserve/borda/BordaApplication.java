package com.softserve.borda;

import com.softserve.borda.controllers.UserController;
import com.softserve.borda.entities.*;
import com.softserve.borda.repositories.*;
import com.softserve.borda.services.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class BordaApplication {

    UserRepository userRepository;

    BoardRepository boardRepository;

    RoleRepository roleRepository;

    UserBoardRelationRepository userBoardRelationRepository;

    BoardRoleRepository boardRoleRepository;

    UserService userService;

    public BordaApplication(UserRepository userRepository, BoardRepository boardRepository,
                            UserBoardRelationRepository userBoardRelationRepository,
                            BoardRoleRepository boardRoleRepository, UserService userService, UserController userController, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.boardRepository = boardRepository;
        this.userBoardRelationRepository = userBoardRelationRepository;
        this.boardRoleRepository = boardRoleRepository;
        this.userService = userService;
        this.roleRepository = roleRepository;

        Role role = new Role(Role.Roles.ROLE_USER.name());
        role = roleRepository.save(role);

        List<User> users = new ArrayList<>();
        List<Board> boards = new ArrayList<>();
        for(int i = 0; i < 3; i++) {
            User user = new User();
            user.setUsername("user" + i);
            user.setPassword(passwordEncoder.encode("pass" + i));
            user.setEmail("email" + i);
            user.setFirstName("FirstName" + i);
            user.setLastName("LastName" + i);
            user.getRoles().add(role);
            userRepository.save(user);
            users.add(user);
        }
        BoardRole owner = new BoardRole(BoardRole.BoardRoles.OWNER.name());
        boardRoleRepository.save(owner);
        BoardRole collaborator = new BoardRole(BoardRole.BoardRoles.COLLABORATOR.name());
        boardRoleRepository.save(collaborator);
        for(int i = 0; i < 10; i++) {
            Board board = new Board();
            board.setName("Board" + i);
            UserBoardRelation userBoardRelation = new UserBoardRelation();
            userBoardRelation.setUser(users.get(0));
            userBoardRelation.setBoard(board);
            userBoardRelation.setBoardRole(owner);
            board.getUserBoardRelations().add(userBoardRelation);
            users.get(0).getUserBoardRelations().add(userBoardRelation);
            boardRepository.save(board);
            userRepository.save(users.get(0));
            userBoardRelationRepository.save(userBoardRelation);
            boards.add(board);
        }

        for(int i = 10; i < 50; i++) {
            Board board = new Board();
            board.setName("Board" + i);
            UserBoardRelation userBoardRelation = new UserBoardRelation();
            userBoardRelation.setUser(users.get(0));
            userBoardRelation.setBoard(board);
            userBoardRelation.setBoardRole(collaborator);
            board.getUserBoardRelations().add(userBoardRelation);
            users.get(0).getUserBoardRelations().add(userBoardRelation);
            boardRepository.save(board);
            userRepository.save(users.get(0));
            userBoardRelationRepository.save(userBoardRelation);
            boards.add(board);
        }
        userService.getUserById(1L);
        userController.getUserById(1L);
    }

    public static void main(String[] args) {
        SpringApplication.run(BordaApplication.class, args);
    }

}
