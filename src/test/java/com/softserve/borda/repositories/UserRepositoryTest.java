package com.softserve.borda.repositories;

import com.softserve.borda.entities.User;
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
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Test
    void shouldInsertAndReturnUser() {
        User user = new User();
        user.setUsername("user1");
        user.setPassword("pass1");
        user.setEmail("email1");
        user.setFirstName("John");
        user.setLastName("Doe");
        User expected = userRepository.save(user);
        User actual = userRepository.getOne(user.getId());
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void shouldInsertAndDeleteUser() {
        User user = new User();
        user.setUsername("user1");
        user.setPassword("pass1");
        user.setEmail("email1");
        user.setFirstName("John");
        user.setLastName("Doe");
        userRepository.save(user);
        userRepository.deleteById(user.getId());
        Assertions.assertFalse(userRepository.findById(user.getId()).isPresent());
    }

    @Test
    void shouldInsertAndUpdateUser() {
        User user = new User();
        user.setUsername("user1");
        user.setPassword("pass1");
        user.setEmail("email1");
        user.setFirstName("John");
        user.setLastName("Doe");
        userRepository.save(user);
        user.setUsername("user1updated");
        user.setPassword("pass1updated");
        user.setEmail("email1updated");
        User expected = userRepository.save(user);
        User actual = userRepository.getOne(user.getId());
        Assertions.assertEquals(expected, actual);
    }

}