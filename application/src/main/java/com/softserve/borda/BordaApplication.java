package com.softserve.borda;

import com.softserve.borda.controllers.BoardController;
import com.softserve.borda.controllers.UserController;
import com.softserve.borda.entities.*;
import com.softserve.borda.repositories.*;
import com.softserve.borda.services.*;
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

    InvitationService invitationService;

    public BordaApplication(UserRepository userRepository, BoardRepository boardRepository,
                            UserBoardRelationRepository userBoardRelationRepository,
                            UserBoardRoleRepository userBoardRoleRepository, TagRepository tagRepository,
                            UserService userService, UserController userController,
                            PasswordEncoder passwordEncoder, RoleRepository roleRepository,
                            BoardService boardService, BoardColumnService boardColumnService,
                            TicketService ticketService, BoardController boardController, InvitationService invitationService) {
        this.userRepository = userRepository;
        this.boardRepository = boardRepository;
        this.userBoardRelationRepository = userBoardRelationRepository;
        this.userBoardRoleRepository = userBoardRoleRepository;
        this.userService = userService;
        this.roleRepository = roleRepository;
        this.boardService = boardService;
        this.boardColumnService = boardColumnService;
        this.ticketService = ticketService;
        this.invitationService = invitationService;

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

        for(int i = 50; i < 56; i++) {
            Board board = new Board();
            board.setName("Board" + i);
            UserBoardRelation userBoardRelation = new UserBoardRelation();
            userBoardRelation.setUser(users.get(1));
            userBoardRelation.setBoard(board);
            userBoardRelation.setUserBoardRole(owner);
            board.getUserBoardRelations().add(userBoardRelation);
            users.get(1).getUserBoardRelations().add(userBoardRelation);
            boardRepository.save(board);
            userRepository.save(users.get(1));
            userBoardRelationRepository.save(userBoardRelation);
            boards.add(board);
        }

        for(int i = 0; i<3; i++){
            Invitation invitation = new Invitation();
            invitation.setBoard(boards.get(i+50));
            invitation.setUserBoardRole(collaborator);
            invitation.setReceiver(users.get(0));
            invitation.setSender(users.get(1));
            invitation.setIsAccepted(null);
            invitation.setReceiverId(users.get(0).getId());
            invitation.setSenderId(users.get(1).getId());
            invitation.setUserBoardRoleId(collaborator.getId());
            invitation.setBoardId(boards.get(i+50).getId());
            invitationService.create(invitation);
        }

        Invitation invitation1 = new Invitation();
        invitation1.setBoard(boards.get(54));
        invitation1.setUserBoardRole(collaborator);
        invitation1.setReceiver(users.get(0));
        invitation1.setSender(users.get(1));
        invitation1.setIsAccepted(false);
        invitation1.setReceiverId(users.get(0).getId());
        invitation1.setSenderId(users.get(1).getId());
        invitation1.setUserBoardRoleId(collaborator.getId());
        invitation1.setBoardId(boards.get(54).getId());
        invitationService.create(invitation1);

        Invitation invitation2 = new Invitation();
        invitation2.setBoard(boards.get(55));
        invitation2.setUserBoardRole(collaborator);
        invitation2.setReceiver(users.get(0));
        invitation2.setSender(users.get(1));
        invitation2.setIsAccepted(true);
        invitation2.setReceiverId(users.get(0).getId());
        invitation2.setSenderId(users.get(1).getId());
        invitation2.setUserBoardRoleId(collaborator.getId());
        invitation2.setBoardId(boards.get(55).getId());
        invitationService.create(invitation2);


        List<Tag> tags = new ArrayList<>();
        for(int i = 0; i < 3; i++) {
            Tag tag = new Tag();
            tag.setText("label"+i);
            tag.setColor("color " + i);
            tagRepository.save(tag);
            tags.add(tag);
        }

        BoardColumn boardColumn = new BoardColumn();
        boardColumn.setName("BoardColumn1");
        boards.get(0).getBoardColumns().add(boardColumn);
        boardColumn = boardColumnService.create(boardColumn);
        Ticket ticket = new Ticket();
        ticket.setTitle("Ticket1");
        ticket.setDescription("Ticket for testing");
        ticket = ticketService.create(ticket);
        boardColumn.getTickets().add(ticket);
        boardColumnService.update(boardColumn);
        boardService.update(boards.get(0));
    }

    public static void main(String[] args) {
        SpringApplication.run(BordaApplication.class, args);
    }

}
