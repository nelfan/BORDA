package com.softserve.borda;

import com.softserve.borda.controllers.BoardController;
import com.softserve.borda.controllers.UserController;
import com.softserve.borda.dto.UpdateTagDTO;
import com.softserve.borda.entities.*;
import com.softserve.borda.repositories.BoardRepository;
import com.softserve.borda.repositories.RoleRepository;
import com.softserve.borda.repositories.TagRepository;
import com.softserve.borda.repositories.UserRepository;
import com.softserve.borda.services.*;
import org.modelmapper.ModelMapper;
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

    UserBoardRelationService userBoardRelationService;

    UserBoardRoleService userBoardRoleService;

    TagRepository tagRepository;

    ModelMapper modelMapper;

    UserService userService;

    BoardService boardService;

    BoardColumnService boardColumnService;

    TicketService ticketService;

    InvitationService invitationService;

    public BordaApplication(UserRepository userRepository, BoardRepository boardRepository,
                            UserBoardRelationService userBoardRelationService,
                            UserBoardRoleService userBoardRoleService, TagRepository tagRepository,
                            UserService userService, UserController userController,
                            PasswordEncoder passwordEncoder, RoleRepository roleRepository,
                            BoardService boardService, BoardColumnService boardColumnService,
                            TicketService ticketService, BoardController boardController, InvitationService invitationService, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.boardRepository = boardRepository;
        this.userBoardRelationService = userBoardRelationService;
        this.userBoardRoleService = userBoardRoleService;
        this.userService = userService;
        this.roleRepository = roleRepository;
        this.boardService = boardService;
        this.boardColumnService = boardColumnService;
        this.ticketService = ticketService;
        this.invitationService = invitationService;
        this.modelMapper = modelMapper;

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
        userBoardRoleService.create(owner);
        UserBoardRole collaborator = new UserBoardRole(UserBoardRole.UserBoardRoles.COLLABORATOR.name());
        userBoardRoleService.create(collaborator);
        UserBoardRole viewer = new UserBoardRole(UserBoardRole.UserBoardRoles.VIEWER.name());
        userBoardRoleService.create(viewer);
        String[] colors = {"#99ec91", "#e9af43", "#43d0e9"};
        String[] tagText = {"Task", "Bug", "User Story"};
        for(int i = 0; i < 10; i++) {
            Board board = new Board();
            board.setName("Board" + i);
            UserBoardRelation userBoardRelation = new UserBoardRelation();
            userBoardRelation.setUser(users.get(0));
            userBoardRelation.setBoard(board);
            userBoardRelation.setUserBoardRoleId(owner.getId());
            board.getUserBoardRelations().add(userBoardRelation);
            users.get(0).getUserBoardRelations().add(userBoardRelation);
            boardRepository.save(board);
            userRepository.save(users.get(0));
            userBoardRelationService.create(userBoardRelation);
            boards.add(board);
            for(int j = 0; j < 3; j++) {
                Tag tag = new Tag();
                tag.setBoardId(board.getId());
                tag.setText(tagText[j]);
                tag.setColor(colors[j]);
                tagRepository.save(tag);
            }
            for(long j = 1; j <= 3; j++) {
                Tag tag = tagRepository.findById(j).get();
                UpdateTagDTO updateTagDTO = modelMapper.map(tag, UpdateTagDTO.class);
                boardController.addNewTagToBoard(updateTagDTO, i + 1L);
            }
        }

        for(int i = 10; i < 50; i++) {
            Board board = new Board();
            board.setName("Board" + i);
            UserBoardRelation userBoardRelation = new UserBoardRelation();
            userBoardRelation.setUser(users.get(0));
            userBoardRelation.setBoard(board);
            userBoardRelation.setUserBoardRoleId(collaborator.getId());
            board.getUserBoardRelations().add(userBoardRelation);
            users.get(0).getUserBoardRelations().add(userBoardRelation);
            boardRepository.save(board);
            userRepository.save(users.get(0));
            userBoardRelationService.create(userBoardRelation);
            boards.add(board);
            for(long j = 1; j <= 3; j++) {
                Tag tag = tagRepository.findById(j).get();
                UpdateTagDTO updateTagDTO = modelMapper.map(tag, UpdateTagDTO.class);
                boardController.addNewTagToBoard(updateTagDTO, i + 1L);
            }
        }

        List<Tag> tags = new ArrayList<>();

        for(int i = 50; i < 56; i++) {
            Board board = new Board();
            board.setName("Board" + i);
            UserBoardRelation userBoardRelation = new UserBoardRelation();
            userBoardRelation.setUser(users.get(1));
            userBoardRelation.setBoard(board);
            userBoardRelation.setUserBoardRoleId(owner.getId());
            board.getUserBoardRelations().add(userBoardRelation);
            users.get(1).getUserBoardRelations().add(userBoardRelation);
            boardRepository.save(board);
            userRepository.save(users.get(1));
            userBoardRelationService.create(userBoardRelation);
            boards.add(board);
            for(long j = 1; j <= 3; j++) {
                Tag tag = tagRepository.findById(j).get();
                UpdateTagDTO updateTagDTO = modelMapper.map(tag, UpdateTagDTO.class);
                boardController.addNewTagToBoard(updateTagDTO, i + 1L);
            }
        }

        for(int i = 0; i<3; i++){
            Invitation invitation = new Invitation();
            invitation.setBoard(boards.get(i+50));
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
        invitation2.setReceiver(users.get(0));
        invitation2.setSender(users.get(1));
        invitation2.setIsAccepted(true);
        invitation2.setReceiverId(users.get(0).getId());
        invitation2.setSenderId(users.get(1).getId());
        invitation2.setUserBoardRoleId(collaborator.getId());
        invitation2.setBoardId(boards.get(55).getId());
        invitationService.create(invitation2);
    }

    public static void main(String[] args) {
        SpringApplication.run(BordaApplication.class, args);
    }

}
