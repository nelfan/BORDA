package com.softserve.borda.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class BoardListDTO {

    private Long id;
    private String name;
    private CreateBoardDTO board;
    private List<TicketDTO> tickets = new ArrayList<>();
}
