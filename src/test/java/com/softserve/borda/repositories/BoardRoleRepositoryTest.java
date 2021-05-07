package com.softserve.borda.repositories;

import com.softserve.borda.entities.BoardRole;
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
class BoardRoleRepositoryTest {

    @Autowired
    BoardRoleRepository boardRoleRepository;

    @Test
    void shouldInsertAndReturnRole() {
        BoardRole boardRole = new BoardRole();
        boardRole.setName("Role1");
        BoardRole expected = boardRoleRepository.save(boardRole);
        BoardRole actual = boardRoleRepository.getOne(boardRole.getId());
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void shouldInsertAndDeleteRole() {
        BoardRole boardRole = new BoardRole();
        boardRole.setName("Role1");
        boardRoleRepository.save(boardRole);
        boardRoleRepository.deleteById(boardRole.getId());
        Assertions.assertFalse(boardRoleRepository.findById(boardRole.getId()).isPresent());
    }

    @Test
    void shouldInsertAndUpdateRole() {
        BoardRole boardRole = new BoardRole();
        boardRole.setName("Role1");
        boardRoleRepository.save(boardRole);
        boardRole.setName("Role1updated");
        BoardRole expected = boardRoleRepository.save(boardRole);
        BoardRole actual = boardRoleRepository.getOne(boardRole.getId());
        Assertions.assertEquals(expected, actual);
    }
}