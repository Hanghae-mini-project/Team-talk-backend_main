package com.backend.teamtalk.domain;

import com.backend.teamtalk.dto.CardDescriptionDto;
import com.backend.teamtalk.dto.CardRequestDto;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(nullable = true)
    private String description;

    @OneToMany
    @Column(nullable = true)
    private List<Comment> comments = new ArrayList<>();

    @ManyToOne
    @JsonBackReference //순환참조 막기
    private Pin pin;


    //create card
    public Card(CardRequestDto requestDto, Pin pin) {
        this.title = requestDto.getTitle();
        //comment, description은 상세조회 페이지에서 작성
        this.pin = pin;
    }


    //update card (title)
    public void update(CardRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.description = requestDto.getDescription();
    }

    //update card (description)
//    public void updateDescription(CardDescriptionDto requestDto) {
//        this.description = requestDto.getDescription();
//    }
}
