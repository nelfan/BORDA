package com.softserve.borda.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class BoardListDTO {

    private Long id;
    private String name;
    private List<com.softserve.borda.dto.TicketDTO> tickets = new ArrayList<>();
}
