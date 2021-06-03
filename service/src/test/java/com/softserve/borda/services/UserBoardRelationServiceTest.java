package com.softserve.borda.services;

import com.softserve.borda.entities.Board;
import com.softserve.borda.entities.User;
import com.softserve.borda.entities.UserBoardRelation;
import com.softserve.borda.exceptions.CustomEntityNotFoundException;
import com.softserve.borda.repositories.UserBoardRoleRepository;
import com.softserve.borda.repositories.UserBoardRelationRepository;
import com.softserve.borda.services.impl.UserBoardRelationServiceImpl;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserBoardRelationServiceTest {

    @Mock
    UserBoardRoleRepository userBoardRoleRepository;

    @Mock
    UserBoardRelationRepository userBoardRelationRepository;

    @InjectMocks
    UserBoardRelationServiceImpl userBoardRelationService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        userBoardRelationService = new UserBoardRelationServiceImpl(userBoardRelationRepository,
                userBoardRoleRepository);
    }

    @Test
    void shouldGetAllUserBoardRelations() {
        List<UserBoardRelation> userBoardRelations = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            UserBoardRelation userBoardRelation = new UserBoardRelation();
            userBoardRelation.setId((long) i);
            userBoardRelation.setUser(new User());
            userBoardRelation.setBoard(new Board());
            userBoardRelations.add(userBoardRelation);
        }

        when(userBoardRelationRepository.findAll()).thenReturn(userBoardRelations);

        List<UserBoardRelation> userBoardRelationList = userBoardRelationService.getAll();

        assertEquals(3, userBoardRelationList.size());
        verify(userBoardRelationRepository, times(1)).findAll();
    }

    @Test
    void shouldGetUserBoardRelationById() {
        UserBoardRelation expected = new UserBoardRelation();
        expected.setId(1L);

        when(userBoardRelationRepository.findById(1L)).thenReturn(java.util.Optional.of(expected));
        UserBoardRelation actual = userBoardRelationService.getUserBoardRelationById(1L);
        assertEquals(actual, expected);
        verify(userBoardRelationRepository, times(1)).findById(1L);
    }

    @Test
    void shouldCreateUserBoardRelation() {
        UserBoardRelation userBoardRelation = new UserBoardRelation();

        UserBoardRelation expected = new UserBoardRelation();
        expected.setId(1L);

        when(userBoardRelationRepository.save(userBoardRelation)).thenReturn(expected);

        UserBoardRelation actual = userBoardRelationService.create(userBoardRelation);

        assertEquals(expected, actual);
        verify(userBoardRelationRepository, times(1)).save(userBoardRelation);
    }

    @Test
    void shouldUpdateUserBoardRelation() {
        UserBoardRelation userBoardRelation = new UserBoardRelation();

        UserBoardRelation userBoardRelationSaved = new UserBoardRelation();
        userBoardRelationSaved.setId(1L);

        UserBoardRelation userBoardRelationUpdated = new UserBoardRelation();
        userBoardRelationUpdated.setId(1L);

        when(userBoardRelationRepository.save(userBoardRelation)).thenReturn(userBoardRelationSaved);

        when(userBoardRelationRepository.save(userBoardRelationUpdated)).thenReturn(userBoardRelationUpdated);

        userBoardRelationService.create(userBoardRelation);

        UserBoardRelation actual = userBoardRelationService.update(userBoardRelationUpdated);

        assertEquals(userBoardRelationUpdated, actual);
        verify(userBoardRelationRepository, times(1)).save(userBoardRelation);
        verify(userBoardRelationRepository, times(1)).save(userBoardRelationUpdated);
    }

    @Test
    void shouldDeleteUserBoardRelationById() {
        UserBoardRelation userBoardRelation = new UserBoardRelation();
        userBoardRelation.setId(1L);

        when(userBoardRelationRepository.findById(1L)).thenReturn(Optional.empty());

        userBoardRelationService.deleteUserBoardRelationById(userBoardRelation.getId());

        assertThrows(CustomEntityNotFoundException.class,
                () -> userBoardRelationService.getUserBoardRelationById(1L));
        verify(userBoardRelationRepository, times(1)).deleteById(userBoardRelation.getId());
    }
}
