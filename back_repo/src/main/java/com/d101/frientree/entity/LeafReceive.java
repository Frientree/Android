package com.d101.frientree.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "leaf_receive")
public class LeafReceive {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "leaf_receive_num")
    private Long leafReceiveNum;


    @ManyToOne
    @JoinColumn(name = "leaf_num", referencedColumnName = "leaf_num")
    private LeafDetail leafDetail;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

}