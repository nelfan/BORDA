package com.softserve.borda.services.impl;

import com.softserve.borda.entities.Tag;
import com.softserve.borda.services.TagService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagServiceImpl implements TagService {

    @Override
    public List<Tag> getAll() {
        return null;
    }

    @Override
    public Tag getTagById(Long id) {
        return null;
    }

    @Override
    public Tag createOrUpdate(Tag tag) {
        return null;
    }

    @Override
    public void deleteTagById(Long id) {

    }
}
