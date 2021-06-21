package com.softserve.borda.services;

import com.softserve.borda.entities.Tag;

import java.util.List;

public interface TagService {
    List<Tag> getAll();

    Tag getTagById(Long id);

    Tag create(Tag tag);

    Tag update(Tag tag);

    boolean deleteTagById(Long id);

    boolean addTagToTicket(Long ticketId, Long tagId);

    boolean deleteTagFromTicket(Long ticketId, Long tagId);

    List<Tag> getAllTagsByBoardId(Long boardId);

    List<Tag> getAllTagsByTicketId(Long ticketId);
}
