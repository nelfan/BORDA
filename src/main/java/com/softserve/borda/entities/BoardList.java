package com.softserve.borda.entities;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@Entity
public class BoardList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @ToString.Exclude
    @ManyToOne
    private Board board;

    @ToString.Exclude
    @OneToMany(mappedBy = "boardList")
    private List<Ticket> tickets;
}
