package com.backend.teamtalk.domain;

import com.backend.teamtalk.dto.SignupRequestDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Table(name = "user")
@AllArgsConstructor
@Builder
@ToString
@NoArgsConstructor
@Getter
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
//    @JsonIgnore
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy = "user")
    @Column(nullable = true)
    @JsonManagedReference
    private List<Board> boards = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    @JsonManagedReference
    private List<Comment> comments = new ArrayList<>();

    //Custom User Details Service 에서 user 가 활성화 상태인지 체크
    @JsonIgnore
    @Column(name = "activated")
    private boolean activated;

    @ManyToMany
    @JoinTable(
            name = "user_authority",
            joinColumns = {@JoinColumn(name = "id", referencedColumnName = "id")},  //조심할 것: id = user_id
            inverseJoinColumns = {@JoinColumn(name = "authority_name", referencedColumnName = "authority_name")})
    private Set<Authority> authorities;

//    @OneToMany(mappedBy = "user")
//    private Set<UserAuthority> authorities;


    @Column(nullable = true)
    private String skill;    //어떻게 지정할지 미정


    public User(SignupRequestDto signupRequestDto) {
        this.username = signupRequestDto.getUsername();
        this.password = signupRequestDto.getPassword();
        this.skill = signupRequestDto.getSkill();
    }

}
