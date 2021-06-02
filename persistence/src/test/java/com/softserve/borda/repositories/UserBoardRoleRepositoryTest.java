package com.softserve.borda.repositories;

import com.softserve.borda.entities.UserBoardRole;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserBoardRoleRepositoryTest {

    @Autowired
    BoardRoleRepository boardRoleRepository;

    @Test
    void shouldInsertAndReturnRole() {
        UserBoardRole userBoardRole = new UserBoardRole();
        userBoardRole.setName("Role1");
        UserBoardRole expected = boardRoleRepository.save(userBoardRole);
        UserBoardRole actual = boardRoleRepository.getOne(userBoardRole.getId());
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void shouldInsertAndDeleteRole() {
        UserBoardRole userBoardRole = new UserBoardRole();
        userBoardRole.setName("Role1");
        boardRoleRepository.save(userBoardRole);
        boardRoleRepository.deleteById(userBoardRole.getId());
        Assertions.assertFalse(boardRoleRepository.findById(userBoardRole.getId()).isPresent());
    }

    @Test
    void shouldInsertAndUpdateRole() {
        UserBoardRole userBoardRole = new UserBoardRole();
        userBoardRole.setName("Role1");
        boardRoleRepository.save(userBoardRole);
        userBoardRole.setName("Role1updated");
        UserBoardRole expected = boardRoleRepository.save(userBoardRole);
        UserBoardRole actual = boardRoleRepository.getOne(userBoardRole.getId());
        Assertions.assertEquals(expected, actual);
    }
}