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

    @Column(name = "sender_id")
    private Long senderId;

    @Column(name = "receiver_id")
    private Long receiverId;

    @Column(name = "board_id")
    private Long boardId;

    @Column(name = "user_board_role_id")
    private Long userBoardRoleId;

    private Boolean isAccepted;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name="sender_id", insertable = false, updatable = false)
    private User sender;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name="receiver_id", insertable = false, updatable = false)
    private User receiver;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name="board_id", insertable = false, updatable = false)
    private Board board;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name="user_board_role_id", insertable = false, updatable = false)
    private UserBoardRole userBoardRole;
}
