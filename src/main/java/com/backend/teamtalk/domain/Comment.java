package com.backend.teamtalk.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String comment;

    @JsonBackReference  //참조를 막고
    @ManyToOne
    private User user;

    @JsonBackReference
    @ManyToOne
    private Card card;
}
