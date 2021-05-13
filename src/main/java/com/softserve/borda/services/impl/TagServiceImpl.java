package com.softserve.borda.services.impl;

import com.softserve.borda.entities.Tag;
import com.softserve.borda.entities.Ticket;
import com.softserve.borda.exceptions.CustomEntityNotFoundException;
import com.softserve.borda.repositories.TagRepository;
import com.softserve.borda.repositories.TicketRepository;
import com.softserve.borda.services.TagService;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Service
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;
    private final TicketRepository ticketRepository;

    public TagServiceImpl(TagRepository tagRepository, TicketRepository ticketRepository) {
        this.tagRepository = tagRepository;
        this.ticketRepository = ticketRepository;
    }

    @Override
    public List<Tag> getAll() {
        return tagRepository.findAll();
    }

    @Override
    public List<Tag> getAllTagsByTicket(Ticket ticket) {
        return ticketRepository.findById(ticket.getId()).orElseThrow(
                () -> new CustomEntityNotFoundException(Ticket.class)).getTags();
    }

    @Override
    public Tag getTagById(Long id) {
        return tagRepository.findById(id).orElseThrow(
                () -> new CustomEntityNotFoundException(Tag.class));
    }

    @Override
    public Tag createOrUpdate(Tag tag) {
        if (tag.getId() != null) {
            Optional<Tag> tagOptional = tagRepository.findById(tag.getId());

            if (tagOptional.isPresent()) {
                Tag newTag = tagOptional.get();
                newTag.setText(tag.getText());
                newTag.setColor(tag.getColor());
                return tagRepository.save(newTag);
            }
        }
        return tagRepository.save(tag);
    }

    @Override
    public void deleteTagById(Long id) {
        tagRepository.deleteById(id);
    }

    @Override
    public boolean addTagToTicket(Tag tag, @NotNull Ticket ticket) {
        if (tag.getId() == null) {
            Ticket ticketEntity = ticketRepository.getOne(ticket.getId());
            ticketEntity.getTags().add(tag);
            ticketRepository.save(ticketEntity);
            return true;
        }
        return false;
    }
}
