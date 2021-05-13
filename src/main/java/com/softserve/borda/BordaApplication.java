package com.softserve.borda;

import com.softserve.borda.controllers.UserController;
import com.softserve.borda.entities.Board;
import com.softserve.borda.entities.BoardRole;
import com.softserve.borda.entities.User;
import com.softserve.borda.entities.UserBoardRelation;
import com.softserve.borda.repositories.BoardRepository;
import com.softserve.borda.repositories.BoardRoleRepository;
import com.softserve.borda.repositories.UserBoardRelationRepository;
import com.softserve.borda.repositories.UserRepository;
import com.softserve.borda.services.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class BordaApplication {

    public static void main(String[] args) {
        SpringApplication.run(BordaApplication.class, args);
    }

}
