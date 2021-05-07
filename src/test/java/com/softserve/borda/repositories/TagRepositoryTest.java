package com.softserve.borda.repositories;

import com.softserve.borda.entities.Tag;
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
class TagRepositoryTest {

    @Autowired
    TagRepository tagRepository;

    @Test
    void shouldInsertAndReturnTag() {
        Tag tag = new Tag();
        tag.setText("Tag1");
        tag.setColor(Tag.Color.GREEN);
        Tag expected = tagRepository.save(tag);
        Tag actual = tagRepository.getOne(tag.getId());
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void shouldInsertAndDeleteTag() {
        Tag tag = new Tag();
        tag.setText("Tag1");
        tag.setColor(Tag.Color.GREEN);
        tagRepository.save(tag);
        tagRepository.deleteById(tag.getId());
        Assertions.assertFalse(tagRepository.findById(tag.getId()).isPresent());
    }

    @Test
    void shouldInsertAndUpdateTag() {
        Tag tag = new Tag();
        tag.setText("Tag1");
        tag.setColor(Tag.Color.GREEN);
        tagRepository.save(tag);
        tag.setText("Tag1updated");
        tag.setColor(Tag.Color.BLUE);
        Tag expected = tagRepository.save(tag);
        Tag actual = tagRepository.getOne(tag.getId());
        Assertions.assertEquals(expected, actual);
    }
}