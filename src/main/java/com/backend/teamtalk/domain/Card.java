package com.backend.teamtalk.domain;

import com.backend.teamtalk.dto.CardRequestDto;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

//빠뜨리니까 디비에는 저장 되는데 조회 할 때 빈 문자로 나옴
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
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

    //board 에 들어갔을 때 pin -> card title 까지만 보여주면 되므로, card 안에 속한 comments 는 ignore 처리
    @OneToMany(mappedBy = "card", cascade = CascadeType.REMOVE)
    @Column(nullable = true)
    @JsonIgnore
    private List<Comment> comments = new ArrayList<>();

    @ManyToOne
    @JsonBackReference
    private Pin pin;


    //create card
    public Card(CardRequestDto requestDto, Pin pin) {
        this.title = requestDto.getTitle();
        this.pin = pin;
    }


    //update card (title)
    public void update(CardRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.description = requestDto.getDescription();
    }

}
