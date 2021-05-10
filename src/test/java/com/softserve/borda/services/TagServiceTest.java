package com.softserve.borda.services;

import com.softserve.borda.entities.Tag;
import com.softserve.borda.entities.Ticket;
import com.softserve.borda.repositories.TagRepository;
import com.softserve.borda.repositories.TicketRepository;
import com.softserve.borda.services.impl.TagServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@SpringBootTest
class TagServiceTest {

    @Mock
    TicketRepository ticketRepository;

    @Mock
    TagRepository tagRepository;

    @InjectMocks
    TagServiceImpl tagService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        tagService = new TagServiceImpl(tagRepository,
                ticketRepository);
    }

    @Test
    void shouldGetAllTags() {
        List<Tag> tags = new ArrayList<>();
        for(int i = 0; i < 3; i++) {
            Tag tag = new Tag();
            tag.setId((long) i);
            tag.setText("tagtext");
            tag.setColor(Tag.Color.GREEN);
            tags.add(tag);
        }

        when(tagRepository.findAll()).thenReturn(tags);

        List<Tag> tagList = tagService.getAll();

        assertEquals(3, tagList.size());
        verify(tagRepository, times(1)).findAll();
    }

    @Test
    void shouldGetAllTagsByTicketId() {
        List<Tag> tags = new ArrayList<>();
        Ticket ticket = new Ticket();
        ticket.setId(1L);
        ticket.setName("ticketName");
        ticket.setBody("ticketBody");
        for (int i = 0; i < 3; i++) {
            Tag tag = new Tag();
            tag.setId((long) i);
            tag.setText("tag" + i);
            tags.add(tag);
            ticket.getTags().add(tag);
        }

        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));

        List<Tag> tagList = tagService.getAllTagsByTicket(ticket);

        assertEquals(3, tagList.size());
        assertEquals(tags, tagList);
        verify(ticketRepository, times(1)).findById(1L);
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

        Tag actual = tagService.createOrUpdate(tag);

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

        tagService.createOrUpdate(tag);

        Tag actual = tagService.createOrUpdate(tagUpdated);

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

        assertNull(tagService.getTagById(1L));
        verify(tagRepository, times(1)).deleteById(tag.getId());
    }
}
