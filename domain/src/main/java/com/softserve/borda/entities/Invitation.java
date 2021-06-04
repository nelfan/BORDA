package com.softserve.borda.entities;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;

@Entity(name="invitations")
@Data
public class Invitation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "invitation_id")
    private Long id;

    @ToString.Exclude
    @ManyToOne
    private User sender;

    @ToString.Exclude
    @ManyToOne
    private User receiver;

    @ToString.Exclude
    @ManyToOne
    private Board board;

    @ToString.Exclude
    @ManyToOne
    private UserBoardRole userBoardRole;

    private Boolean isAccepted;
}
