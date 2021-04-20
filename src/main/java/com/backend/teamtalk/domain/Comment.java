package com.backend.teamtalk.domain;

import com.backend.teamtalk.dto.CommentRequestDto;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter //user 빼가야 해 서비스에서 확인
@NoArgsConstructor
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

    public Comment(CommentRequestDto requestDto, Card card, User user) {
        this.comment = requestDto.getComment();
        this.card = card;
        this.user = user;
    }

    public void update(CommentRequestDto requestDto, User user, Card card) {
        this.comment = requestDto.getComment();
        this.user = user;
        this.card = card;
    }
}
