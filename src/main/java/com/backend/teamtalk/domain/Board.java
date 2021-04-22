package com.backend.teamtalk.domain;

import com.backend.teamtalk.dto.BoardRequestDto;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@ToString
@Getter
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,
                    property = "id")
public class Board {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;


    @ManyToOne
    @JsonBackReference
    private User user;

    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE)
    @JsonManagedReference
    private List<Pin> pins = new ArrayList<>();


    //constructor
    //create board
    public Board(BoardRequestDto boardRequestDto, User user) {
        this.title = boardRequestDto.getTitle();
        this.user = user;

    }

    //임시로 남겨둠
    public Board(BoardRequestDto boardRequestDto) {
        this.title = boardRequestDto.getTitle();

    }

    //update board
    public void update(BoardRequestDto boardRequestDto) {
        this.title = boardRequestDto.getTitle();
    }



}
