package com.backend.teamtalk.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

//@Table(name = "authority")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Authority {

    @Id
    @Column(name = "authority_name")
    private String authorityName;   //ROLE_USER


//    @OneToMany(mappedBy = "authority")
//    private List<UserAuthority> userAuthorities = new ArrayList<>();
}
