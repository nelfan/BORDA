package com.softserve.borda.services;

import com.softserve.borda.dto.BoardColumnDTO;
import com.softserve.borda.dto.TicketDTO;
import reactor.core.publisher.Sinks;

import java.util.List;

public interface SinkService {

    Sinks.Many<List<BoardColumnDTO>> getColumnsSink(Long boardId);

    Sinks.Many<List<TicketDTO>> getTicketsSink(Long columnId);
}
