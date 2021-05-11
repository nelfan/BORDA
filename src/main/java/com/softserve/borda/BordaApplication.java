package com.softserve.borda;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BordaApplication {

//    UserRepository userRepository;
//
//    BoardRepository boardRepository;
//
//    UserBoardRelationRepository userBoardRelationRepository;
//
//    BoardRoleRepository boardRoleRepository;
//
//    UserService userService;
//
//    UserController userController;
//
//    public BordaApplication(UserRepository userRepository, BoardRepository boardRepository,
//                            UserBoardRelationRepository userBoardRelationRepository,
//                            BoardRoleRepository boardRoleRepository, UserService userService, UserController userController) {
//        this.userRepository = userRepository;
//        this.boardRepository = boardRepository;
//        this.userBoardRelationRepository = userBoardRelationRepository;
//        this.boardRoleRepository = boardRoleRepository;
//        this.userService = userService;
//
//
//        List<User> users = new ArrayList<>();
//        List<Board> boards = new ArrayList<>();
//        for(int i = 0; i < 3; i++) {
//            User user = new User();
//            user.setUsername("user" + i);
//            user.setPassword("pass" + i);
//            user.setEmail("email" + i);
//            user.setFirstName("FirstName" + i);
//            user.setLastName("LastName" + i);
//            userRepository.save(user);
//            users.add(user);
//        }
//        BoardRole owner = new BoardRole();
//        owner.setName("owner");
//        boardRoleRepository.save(owner);
//        BoardRole collaborator = new BoardRole();
//        collaborator.setName("collaborator");
//        boardRoleRepository.save(collaborator);
//        for(int i = 0; i < 3; i++) {
//            Board board = new Board();
//            board.setName("Board" + i);
//            UserBoardRelation userBoardRelation = new UserBoardRelation();
//            userBoardRelation.setUser(users.get(i));
//            userBoardRelation.setBoard(board);
//            userBoardRelation.getBoardRoles().add(owner);
//            board.getUserBoardRelations().add(userBoardRelation);
//            users.get(i).getUserBoardRelations().add(userBoardRelation);
//            boardRepository.save(board);
//            userRepository.save(users.get(i));
//            userBoardRelationRepository.save(userBoardRelation);
//            boards.add(board);
//        }
//        userService.getUserById(1L);
//        userController.getUser(1L);
//    }

    public static void main(String[] args) {
        SpringApplication.run(BordaApplication.class, args);
    }

}
