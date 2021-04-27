package com.softserve.borda.entities;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;

@Data
@Entity
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String body;

    @ToString.Exclude
    @ManyToOne
    private BoardList boardList;

}
