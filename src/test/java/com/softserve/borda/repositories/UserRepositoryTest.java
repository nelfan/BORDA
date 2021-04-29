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
    public void myTest() throws Exception {
        User user = new User();
        user.setUsername("user1");
        user.setPassword("pass1");
        user.setEmail("email1");
        user.setFirstName("John");
        user.setLastName("Doe");
        user = userRepository.save(user);
        Assertions.assertEquals(user, userRepository.getOne(user.getId()));
    }

}