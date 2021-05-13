package com.softserve.borda.services;

import com.softserve.borda.entities.User;
import com.softserve.borda.repositories.UserRepository;
import com.softserve.borda.services.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserServiceImpl userService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        userService = new UserServiceImpl(userRepository);
    }

    @Test
    void shouldGetAllUsers() {
        List<User> users = new ArrayList<>();
        for(int i = 0; i < 3; i++) {
            User user = new User();
            user.setId((long) i);
            user.setUsername("user" + i);
            user.setPassword("pass" + i);
            user.setEmail("email" + i);
            user.setFirstName("FirstName" + i);
            user.setLastName("LastName" + i);
            users.add(user);
        }

        when(userRepository.findAll()).thenReturn(users);

        List<User> userList = userService.getAll();

        assertEquals(3, userList.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void shouldGetUserById() {
        User expected = new User();
        expected.setId(1L);
        expected.setUsername("user");
        expected.setPassword("pass");
        expected.setEmail("email");
        expected.setFirstName("FirstName");
        expected.setLastName("LastName");

        when(userRepository.findById(1L)).thenReturn(Optional.of(expected));
        User actual = userService.getUserById(1L);
        assertEquals(actual, expected);
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void shouldCreateUser() {
        User user = new User();
        user.setUsername("user");
        user.setPassword("pass");
        user.setEmail("email");
        user.setFirstName("FirstName");
        user.setLastName("LastName");

        User expected = new User();
        expected.setId(1L);
        expected.setUsername("user");
        expected.setPassword("pass");
        expected.setEmail("email");
        expected.setFirstName("FirstName");
        expected.setLastName("LastName");

        when(userRepository.save(user)).thenReturn(expected);

        User actual = userService.createOrUpdate(user);

        assertEquals(expected, actual);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void shouldUpdateUser() {
        User user = new User();
        user.setUsername("user");
        user.setPassword("pass");
        user.setEmail("email");
        user.setFirstName("FirstName");
        user.setLastName("LastName");

        User userSaved = new User();
        userSaved.setId(1L);
        userSaved.setUsername("user");
        userSaved.setPassword("pass");
        userSaved.setEmail("email");
        userSaved.setFirstName("FirstName");
        userSaved.setLastName("LastName");

        User userUpdated = new User();
        userUpdated.setId(1L);
        userUpdated.setUsername("userUpdated");
        userUpdated.setPassword("passUpdated");
        userUpdated.setEmail("emailUpdated");
        userUpdated.setFirstName("FirstNameUpdated");
        userUpdated.setLastName("LastNameUpdated");

        when(userRepository.save(user)).thenReturn(userSaved);

        when(userRepository.save(userUpdated)).thenReturn(userUpdated);

        userService.createOrUpdate(user);
        
        User actual = userService.createOrUpdate(userUpdated);

        assertEquals(userUpdated, actual);
        verify(userRepository, times(1)).save(user);
        verify(userRepository, times(1)).save(userUpdated);
    }

    @Test
    void shouldDeleteUserById() {
        User user = new User();
        user.setId(1L);
        user.setUsername("user");
        user.setPassword("pass");
        user.setEmail("email");
        user.setFirstName("FirstName");
        user.setLastName("LastName");

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        userService.deleteUserById(user.getId());

        assertNull(userService.getUserById(1L));
        verify(userRepository, times(1)).deleteById(user.getId());
    }
}
