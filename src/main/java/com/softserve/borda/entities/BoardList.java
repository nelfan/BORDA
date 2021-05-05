package com.softserve.borda.entities;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity(name = "board_lists")
public class BoardList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_list_id")
    private Long id;

    @NotBlank
    private String name;

    @ToString.Exclude
    @ManyToOne(cascade = CascadeType.PERSIST, optional = false)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @ToString.Exclude
    @OneToMany(mappedBy = "boardList",
            cascade = CascadeType.ALL)
    private List<Ticket> tickets = new ArrayList<>();
}
