package com.softserve.borda.services.impl;

import com.softserve.borda.entities.Tag;
import com.softserve.borda.exceptions.CustomEntityNotFoundException;
import com.softserve.borda.exceptions.CustomFailedToDeleteEntityException;
import com.softserve.borda.repositories.TagRepository;
import com.softserve.borda.services.TagService;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Log
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    @Override
    public List<Tag> getAll() {
        return tagRepository.findAll();
    }

    @Override
    public Tag getTagById(Long id) {
        return tagRepository.findById(id).orElseThrow(
                () -> new CustomEntityNotFoundException(Tag.class));
    }

    @Override
    public Tag create(Tag tag) {
        return tagRepository.save(tag);
    }

    @Override
    public Tag update(Tag tag) {
        Tag existingTag = getTagById(tag.getId());
        existingTag.setText(tag.getText());
        existingTag.setColor(tag.getColor());
        existingTag.setBoardId(tag.getBoardId());
        return tagRepository.save(existingTag);
    }

    @Override
    public boolean deleteTagById(Long id) {
        try {
            tagRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            log.severe(e.getMessage());
            throw new CustomFailedToDeleteEntityException(Tag.class);
        }
    }

    @Override
    public List<Tag> getAllTagsByBoardId(Long boardId) {
        return tagRepository.getAllTagsByBoardId(boardId);
    }
}
