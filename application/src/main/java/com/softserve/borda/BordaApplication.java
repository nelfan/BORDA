package com.softserve.borda;

import com.softserve.borda.controllers.UserController;
import com.softserve.borda.entities.*;
import com.softserve.borda.repositories.BoardRepository;
import com.softserve.borda.repositories.RoleRepository;
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

    UserRepository userRepository;

    BoardRepository boardRepository;

    RoleRepository roleRepository;

    UserBoardRelationService userBoardRelationService;

    UserBoardRoleService userBoardRoleService;

    TagService tagService;

    ModelMapper modelMapper;

    UserService userService;

    BoardService boardService;

    BoardColumnService boardColumnService;

    TicketService ticketService;

    InvitationService invitationService;

    public BordaApplication(UserRepository userRepository, BoardRepository boardRepository,
                            UserBoardRelationService userBoardRelationService,
                            UserBoardRoleService userBoardRoleService, TagService tagService,
                            UserService userService, UserController userController,
                            PasswordEncoder passwordEncoder, RoleRepository roleRepository,
                            BoardService boardService, BoardColumnService boardColumnService,
                            TicketService ticketService, InvitationService invitationService, ModelMapper modelMapper) {
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
        this.tagService = tagService;

        Role userRole = new Role(Role.Roles.ROLE_USER.name());
        roleRepository.save(userRole);
        Role adminRole = new Role(Role.Roles.ROLE_ADMIN.name());
        roleRepository.save(adminRole);

        List<User> users = new ArrayList<>();
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
            boardRepository.save(board);
            userBoardRelationService.create(userBoardRelation);

            for(int j = 0; j < 3; j++) {
                Tag tag = new Tag();
                tag.setBoardId(board.getId());
                tag.setText(tagText[j]);
                tag.setColor(colors[j]);
                tagService.create(tag);
            }
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(BordaApplication.class, args);
    }

}
