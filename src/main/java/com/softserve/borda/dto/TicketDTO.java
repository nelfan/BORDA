package com.softserve.borda.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class TicketDTO {

    private Long id;
    private String title;
    private String description;
    private Byte[] img;
    private List<UserSimpleDTO> members = new ArrayList<>();
    List<CommentDTO> comments;
    private List<TagDTO> tags;
}
