package com.softserve.borda.services;

import com.softserve.borda.entities.Tag;
import com.softserve.borda.entities.Ticket;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface TagService {
    List<Tag> getAll();

    List<Tag> getAllTagsByTicketId(Long ticketId);

    Tag getTagById(Long id);
    Tag createOrUpdate(Tag tag);
    void deleteTagById(Long id);

    boolean addTagToTicket(Tag tag, @NotNull Ticket ticket);
}
