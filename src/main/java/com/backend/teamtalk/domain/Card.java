package com.backend.teamtalk.domain;

import com.backend.teamtalk.dto.CardDescriptionDto;
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

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")    //빠뜨리니까 디비에는 저장 되는데 조회 할 때 빈 문자로 나옴
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

    @OneToMany(mappedBy = "card")
    @Column(nullable = true)
    @JsonIgnore // 처음 조회 때 보드->핀->카드 만 보여주면 되지 카드 안의 커멘트들 까지 보여줄 필요는 없으니까
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
