package com.softserve.borda.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class BoardColumnDTO {

    private Long id;
    private String name;
    private Double positionIndex;
    private List<TicketDTO> tickets = new ArrayList<>();
}
