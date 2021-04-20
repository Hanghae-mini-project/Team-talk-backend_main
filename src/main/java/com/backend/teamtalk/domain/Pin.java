package com.backend.teamtalk.domain;

import com.backend.teamtalk.dto.PinRequestDto;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor
@ToString
@Getter @Setter
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,
                    property = "id")   //무한순환 테스트 중 (, property = "id")
public class Pin {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @OneToMany(mappedBy = "pin", cascade = CascadeType.REMOVE)
    @JsonManagedReference
    private List<Card> cards;

    @ManyToOne
    @JsonBackReference  //순환 참조는 막았는데 보드 아이디는 안찍힌다. ㅠ_ㅜ -> 특정 핀 조회에 보드 아이디 반영
    private Board board;


    //create pin
    public Pin(PinRequestDto requestDto, Board board) {
        this.title = requestDto.getTitle();
        this.board = board;

    }

    //update pin
    public void update(PinRequestDto requestDto) {
        this.title = requestDto.getTitle();
    }
}
