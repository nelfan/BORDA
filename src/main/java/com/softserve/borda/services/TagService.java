package com.softserve.borda.services;

import com.softserve.borda.entities.Tag;

import java.util.List;

public interface TagService {
    List<Tag> getAll();
    Tag getTagById(Long id);
    Tag createOrUpdate(Tag tag);
    void deleteTagById(Long id);
}
