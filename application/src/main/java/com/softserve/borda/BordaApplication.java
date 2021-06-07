package com.softserve.borda;

import com.softserve.borda.controllers.BoardController;
import com.softserve.borda.controllers.UserController;
import com.softserve.borda.entities.*;
import com.softserve.borda.repositories.*;
import com.softserve.borda.services.BoardColumnService;
import com.softserve.borda.services.BoardService;
import com.softserve.borda.services.TicketService;
import com.softserve.borda.services.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class BordaApplication {

    BoardController boardController;

    UserRepository userRepository;

    BoardRepository boardRepository;

    RoleRepository roleRepository;

    UserBoardRelationRepository userBoardRelationRepository;

    UserBoardRoleRepository userBoardRoleRepository;

    TagRepository tagRepository;

    UserService userService;

    BoardService boardService;

    BoardColumnService boardColumnService;

    TicketService ticketService;

    public BordaApplication(UserRepository userRepository, BoardRepository boardRepository,
                            UserBoardRelationRepository userBoardRelationRepository,
                            UserBoardRoleRepository userBoardRoleRepository, TagRepository tagRepository,
                            UserService userService, UserController userController,
                            PasswordEncoder passwordEncoder, RoleRepository roleRepository,
                            BoardService boardService, BoardColumnService boardColumnService,
                            TicketService ticketService, BoardController boardController) {
        this.userRepository = userRepository;
        this.boardRepository = boardRepository;
        this.userBoardRelationRepository = userBoardRelationRepository;
        this.userBoardRoleRepository = userBoardRoleRepository;
        this.userService = userService;
        this.roleRepository = roleRepository;
        this.boardService = boardService;
        this.boardColumnService = boardColumnService;
        this.ticketService = ticketService;

        Role userRole = new Role(Role.Roles.ROLE_USER.name());
        roleRepository.save(userRole);
        Role adminRole = new Role(Role.Roles.ROLE_ADMIN.name());
        roleRepository.save(adminRole);

        List<User> users = new ArrayList<>();
        List<Board> boards = new ArrayList<>();
        for(int i = 0; i < 3; i++) {
            User user = new User();
            user.setUsername("user" + i);
            user.setPassword(passwordEncoder.encode("pass" + i));
            user.setEmail("email" + i);
            user.setFirstName("FirstName" + i);
            user.setLastName("LastName" + i);
            user.getRoles().add(userRole);
            userRepository.save(user);
            users.add(user);
        }

        UserBoardRole owner = new UserBoardRole(UserBoardRole.UserBoardRoles.OWNER.name());
        userBoardRoleRepository.save(owner);
        UserBoardRole collaborator = new UserBoardRole(UserBoardRole.UserBoardRoles.COLLABORATOR.name());
        userBoardRoleRepository.save(collaborator);
        for(int i = 0; i < 10; i++) {
            Board board = new Board();
            board.setName("Board" + i);
            UserBoardRelation userBoardRelation = new UserBoardRelation();
            userBoardRelation.setUser(users.get(0));
            userBoardRelation.setBoard(board);
            userBoardRelation.setUserBoardRole(owner);
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
            userBoardRelation.setUserBoardRole(collaborator);
            board.getUserBoardRelations().add(userBoardRelation);
            users.get(0).getUserBoardRelations().add(userBoardRelation);
            boardRepository.save(board);
            userRepository.save(users.get(0));
            userBoardRelationRepository.save(userBoardRelation);
            boards.add(board);
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(BordaApplication.class, args);
    }

}
