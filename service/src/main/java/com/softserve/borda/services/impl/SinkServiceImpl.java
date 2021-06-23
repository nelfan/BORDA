package com.softserve.borda.services.impl;

import com.softserve.borda.dto.BoardColumnDTO;
import com.softserve.borda.dto.TicketDTO;
import com.softserve.borda.services.SinkService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Sinks;

import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class SinkServiceImpl implements SinkService {

    private final Map<Long, Sinks.Many<List<BoardColumnDTO>>> columnSinks;

    private final Map<Long, Sinks.Many<List<TicketDTO>>> ticketSinks;

    @Transactional(readOnly = true)
    @Override
    public Sinks.Many<List<BoardColumnDTO>> getColumnsSink(Long boardId) {
        Sinks.Many<List<BoardColumnDTO>> sink = columnSinks.get(boardId);
        return sink == null ? createColumnsSink(boardId) : sink;
    }

    @Transactional(readOnly = true)
    @Override
    public Sinks.Many<List<TicketDTO>> getTicketsSink(Long columnId) {
        Sinks.Many<List<TicketDTO>> sink = ticketSinks.get(columnId);
        return sink == null ? createTicketsSink(columnId) : sink;
    }

    private Sinks.Many<List<BoardColumnDTO>> createColumnsSink(Long boardId) {
        Sinks.Many<List<BoardColumnDTO>> sink = Sinks.many().replay().latest();
        columnSinks.put(boardId, sink);
        return sink;
    }

    private Sinks.Many<List<TicketDTO>> createTicketsSink(Long columnId) {
        Sinks.Many<List<TicketDTO>> sink = Sinks.many().replay().latest();
        ticketSinks.put(columnId, sink);
        return sink;
    }
}
