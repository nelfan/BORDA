package com.softserve.borda.dto;

import lombok.Data;

@Data
public class CommentDTO {

    private String text;
    private UserFullDTO user;
    private TicketDTO ticket;
}
