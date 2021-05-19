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
    private String date;
    private List<UserSimpleDTO> members = new ArrayList<>();
    private List<CommentDTO> comments;
    private List<TagDTO> tags;
}
