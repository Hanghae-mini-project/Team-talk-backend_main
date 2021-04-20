package com.backend.teamtalk.service;

import com.backend.teamtalk.domain.Card;
import com.backend.teamtalk.domain.Comment;
import com.backend.teamtalk.dto.CardRequestDto;
import com.backend.teamtalk.dto.CommentRequestDto;
import com.backend.teamtalk.repository.CardRepository;
import com.backend.teamtalk.repository.CommentRepository;
import com.backend.teamtalk.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final CardRepository cardRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    //create comment
    public void createComment(Long card_id, CommentRequestDto requestDto, User principal) {
        //requestDto, card, user
        Card card = cardRepository.findById(card_id).orElseThrow(
                () -> new IllegalArgumentException("There is no card.")
        );
        com.backend.teamtalk.domain.User user = userRepository.findByUsername(principal.getUsername()).orElseThrow(
                () -> new IllegalArgumentException("There is nobody by that name.")
        );

        Comment comment = new Comment(requestDto, card, user);
        commentRepository.save(comment);

    }

    //comment written by user.
    public Map<String, Object> readComments(Long card_id, User principal) {
        Map<String, Object> commentInfo = new LinkedHashMap<>();
        com.backend.teamtalk.domain.User user = userRepository.findByUsername(principal.getUsername())
                .orElseThrow(
                () -> new IllegalArgumentException("nobody else...")
        );
        List<Comment> comments = user.getComments();

        commentInfo.put("cardId", card_id);
        commentInfo.put("comments", comments);


        return commentInfo;
    }

    //update comment
    @Transactional
    public void updateComment(Long card_id, Long comment_id, CommentRequestDto requestDto, User principal) {
        Card card = cardRepository.findById(card_id).orElseThrow(
                () -> new IllegalArgumentException("There is no card.")
        );

        Comment comment = commentRepository.findById(comment_id).orElseThrow(
                () -> new IllegalArgumentException("nothing!")
        );
        com.backend.teamtalk.domain.User user = userRepository.findByUsername(principal.getUsername()).orElseThrow(
                () -> new IllegalArgumentException("There is nobody by that name.")
        );
        comment.update(requestDto, user, card);
    }

    //delete comment
    public boolean deleteComment(Long card_id, Long comment_id, User principal) {
        Card card = cardRepository.findById(card_id).orElseThrow(
                () -> new IllegalArgumentException("There is no card.")
        );

        Comment comment = commentRepository.findById(comment_id).orElseThrow(
                () -> new IllegalArgumentException("nothing!")
        );
        com.backend.teamtalk.domain.User user = userRepository.findByUsername(principal.getUsername()).orElseThrow(
                () -> new IllegalArgumentException("There is nobody by that name.")
        );

        if (!comment.getUser().getId().equals(user.getId())) {
            //아 어떤 예외처리로 해야 하는 걸까? 이렇게 말고 예외 쓰고 싶은데
            return false;
        } else {
            commentRepository.deleteById(comment_id);
            return true;
        }

    }
}
