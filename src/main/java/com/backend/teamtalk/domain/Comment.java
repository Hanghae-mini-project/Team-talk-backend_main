package com.backend.teamtalk.domain;

import com.backend.teamtalk.dto.CommentRequestDto;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter //user 를 get 해야 하므로 사용. service 에서 확인 가능
@NoArgsConstructor
@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String comment;

    @JsonBackReference
    @ManyToOne
    private User user;

    @JsonBackReference
    @ManyToOne
    private Card card;

    //create comment
    public Comment(CommentRequestDto requestDto, Card card, User user) {
        this.comment = requestDto.getComment();
        this.card = card;
        this.user = user;
    }

    //update comment
    public void update(CommentRequestDto requestDto, User user, Card card) {
        this.comment = requestDto.getComment();
        this.user = user;
        this.card = card;
    }
}
