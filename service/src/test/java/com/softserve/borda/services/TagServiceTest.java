package com.softserve.borda.services;

import com.softserve.borda.entities.Tag;
import com.softserve.borda.exceptions.CustomEntityNotFoundException;
import com.softserve.borda.repositories.TagRepository;
import com.softserve.borda.services.impl.TagServiceImpl;
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
class TagServiceTest {

    @Mock
    TagRepository tagRepository;

    @InjectMocks
    TagServiceImpl tagService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        tagService = new TagServiceImpl(tagRepository);
    }

    @Test
    void shouldGetAllTags() {
        List<Tag> tags = new ArrayList<>();
        for(int i = 0; i < 3; i++) {
            Tag tag = new Tag();
            tag.setId((long) i);
            tag.setText("tagtext");
            tag.setColor("GREEN");
            tags.add(tag);
        }

        when(tagRepository.findAll()).thenReturn(tags);

        List<Tag> tagList = tagService.getAll();

        assertEquals(3, tagList.size());
        verify(tagRepository, times(1)).findAll();
    }

    @Test
    void shouldGetTagById() {
        Tag expected = new Tag();
        expected.setId(1L);
        expected.setText("tag");

        when(tagRepository.findById(1L)).thenReturn(java.util.Optional.of(expected));
        Tag actual = tagService.getTagById(1L);
        assertEquals(actual, expected);
        verify(tagRepository, times(1)).findById(1L);
    }

    @Test
    void shouldCreateTag() {
        Tag tag = new Tag();
        tag.setText("tag");

        Tag expected = new Tag();
        expected.setId(1L);
        expected.setText("tag");

        when(tagRepository.save(tag)).thenReturn(expected);

        Tag actual = tagService.create(tag);

        assertEquals(expected, actual);
        verify(tagRepository, times(1)).save(tag);
    }

    @Test
    void shouldUpdateTag() {
        Tag tag = new Tag();
        tag.setText("tag");

        Tag tagSaved = new Tag();
        tagSaved.setId(1L);
        tagSaved.setText("tag");

        Tag tagUpdated = new Tag();
        tagUpdated.setId(1L);
        tagUpdated.setText("tagUpdated");

        when(tagRepository.save(tag)).thenReturn(tagSaved);

        when(tagRepository.save(tagUpdated)).thenReturn(tagUpdated);

        tagService.create(tag);

        Tag actual = tagService.update(tagUpdated);

        assertEquals(tagUpdated, actual);
        verify(tagRepository, times(1)).save(tag);
        verify(tagRepository, times(1)).save(tagUpdated);
    }

    @Test
    void shouldDeleteTagById() {
        Tag tag = new Tag();
        tag.setId(1L);
        tag.setText("tag");

        when(tagRepository.findById(1L)).thenReturn(Optional.empty());

        tagService.deleteTagById(tag.getId());

        assertThrows(CustomEntityNotFoundException.class, () -> tagService.getTagById(1L));
        verify(tagRepository, times(1)).deleteById(tag.getId());
    }
}
