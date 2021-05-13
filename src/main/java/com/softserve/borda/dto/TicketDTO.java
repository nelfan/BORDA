package com.softserve.borda.dto;

import lombok.Data;

import java.util.List;

@Data
public class TicketDTO {

    private Long id;
    private String name;
    private String body;
    private BoardListDTO boardList;
    List<CommentDTO> comments;
    private List<TagDTO> tags;
}
