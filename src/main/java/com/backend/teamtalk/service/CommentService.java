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
        ); //principal 에 점 찍는 순간 null 터지는데 그러면 이 에러까지 안오나? board 랑 비교

        Comment comment = new Comment(requestDto, card, user);
        commentRepository.save(comment);

    }

    //특정 카드에 달려있는 댓글들 가져오기.
    public Map<String, Object> readComments(Long card_id, User principal) {
        Map<String, Object> commentInfo = new LinkedHashMap<>();

        com.backend.teamtalk.domain.User user = userRepository.findByUsername(principal.getUsername())
                .orElseThrow(
                () -> new IllegalArgumentException("nobody else...")
        );
//        List<Comment> comments = user.getComments();

        //카드에 달려있는 댓글들 가져오기
        Card card = cardRepository.findById(card_id).orElseThrow(
                () -> new IllegalArgumentException("There is no card.")
        );      //3번 카드
        List<Comment> comments = card.getComments();


        commentInfo.put("cardId", card_id);
        commentInfo.put("comments", comments);


        return commentInfo;
    }



    //update comment
    @Transactional          //반환 타입 고민해 볼 것
    public Comment updateComment(Long card_id, Long comment_id, CommentRequestDto requestDto, User principal) {
        Card card = cardRepository.findById(card_id).orElseThrow(
                () -> new IllegalArgumentException("There is no card.")
        );  //에러가 흘러옴!!!

        Comment comment = commentRepository.findById(comment_id).orElseThrow(
                () -> new IllegalArgumentException("There is no reply.")
        ); //오, 테스트 성공!!!
        com.backend.teamtalk.domain.User user = userRepository.findByUsername(principal.getUsername()).orElseThrow(
                () -> new IllegalArgumentException("There is nobody by that name.")
        );

        //코멘트를 수정하려는 유저와, 코멘트를 작성한 유저가 일치하는지
        if (!comment.getUser().getId().equals(user.getId())) {
            //아 어떤 예외처리로 해야 하는 걸까? 이렇게 말고 예외 쓰고 싶은데
            return null;
        } else {
            comment.update(requestDto, user, card);
            return comment;
        }


    }

    //delete comment
    public Comment deleteComment(Long card_id, Long comment_id, User principal) {
        Card card = cardRepository.findById(card_id).orElseThrow(
                () -> new IllegalArgumentException("There is no card.")
        );

        Comment comment = commentRepository.findById(comment_id).orElseThrow(
                () -> new IllegalArgumentException("nothing!")
        );
        com.backend.teamtalk.domain.User user = userRepository.findByUsername(principal.getUsername()).orElseThrow(
                () -> new IllegalArgumentException("There is nobody by that name.")
        );

        //코멘트를 삭제하려는 유저와, 코멘트를 작성한 유저가 일치하는지
        if (!comment.getUser().getId().equals(user.getId())) {
            //아 어떤 예외처리로 해야 하는 걸까? 이렇게 말고 예외 쓰고 싶은데
            return null;
        } else {
            commentRepository.deleteById(comment_id);
            return comment; //boolean 으로 하니까 삭제 메세지는 뜨는데 db 에서 삭제가 안된다.
        }

    }
}
