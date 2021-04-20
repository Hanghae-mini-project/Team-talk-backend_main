//package com.backend.teamtalk.domain;
//
//import javax.persistence.*;
//
//public class UserAuthority {
//
//    @Id
//    @GeneratedValue
//    @Column(name = "user_authority_id")
//    private Long id;
//
//    @ManyToOne
//    @JoinColumn(name = "user_id")
//    private User User;
//
//    @ManyToOne
//    @JoinColumn(name = "authority_name")
//    private Authority authority;
//}
