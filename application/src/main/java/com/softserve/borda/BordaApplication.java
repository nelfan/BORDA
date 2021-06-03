package com.softserve.borda;

import com.softserve.borda.controllers.UserController;
import com.softserve.borda.entities.*;
import com.softserve.borda.repositories.*;
import com.softserve.borda.services.BoardListService;
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

    UserRepository userRepository;

    BoardRepository boardRepository;

    RoleRepository roleRepository;

    UserBoardRelationRepository userBoardRelationRepository;

    BoardRoleRepository boardRoleRepository;

    TagRepository tagRepository;

    UserService userService;

    BoardService boardService;

    BoardListService boardListService;

    TicketService ticketService;

    public BordaApplication(UserRepository userRepository, BoardRepository boardRepository,
                            UserBoardRelationRepository userBoardRelationRepository,
                            BoardRoleRepository boardRoleRepository, TagRepository tagRepository,
                            UserService userService, PasswordEncoder passwordEncoder,
                            RoleRepository roleRepository, BoardService boardService,
                            BoardListService boardListService, TicketService ticketService) {
        this.userRepository = userRepository;
        this.boardRepository = boardRepository;
        this.userBoardRelationRepository = userBoardRelationRepository;
        this.boardRoleRepository = boardRoleRepository;
        this.userService = userService;
        this.roleRepository = roleRepository;
        this.boardService = boardService;
        this.boardListService = boardListService;
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

        List<Tag> tags = new ArrayList<>();
        for(int i = 0; i < 3; i++) {
            Tag tag = new Tag();
            tag.setText("label"+i);
            tag.setColor("color " + i);
            tag.setBoard(boards.get(0));
            tagRepository.save(tag);
            tags.add(tag);
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

        BoardList boardList = new BoardList();
        boardList.setName("BoardList1");
        boards.get(0).getBoardLists().add(boardList);
        boardList = boardListService.create(boardList);
        Ticket ticket = new Ticket();
        ticket.setTitle("Ticket1");
        ticket.setDescription("Ticket for testing");
        ticket = ticketService.create(ticket);
        boardList.getTickets().add(ticket);
        boardListService.update(boardList);
        boardService.update(boards.get(0));
    }

    public static void main(String[] args) {
        SpringApplication.run(BordaApplication.class, args);
    }

}
