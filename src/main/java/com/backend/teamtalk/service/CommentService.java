package com.backend.teamtalk.service;

import com.backend.teamtalk.domain.Card;
import com.backend.teamtalk.domain.Comment;
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

        Card card = cardRepository.findById(card_id).orElseThrow(
                () -> new IllegalArgumentException("There is no card.")
        );
        com.backend.teamtalk.domain.User user = userRepository.findByUsername(principal.getUsername()).orElseThrow(
                () -> new IllegalArgumentException("There is nobody by that name.")
        );

        Comment comment = new Comment(requestDto, card, user);
        commentRepository.save(comment);

    }

    /*
     * 실수: card_id 로 그에 맞는 카드를 가져온 다음 그 카드에 달린 댓글들을 가져와야 하는데,
     * 이 로직을 빼먹고 card_id 만 뿌려줘서 어느 카드를 가든 같은 댓글들이 조회.
     * 프론트에서 이를 발견 하고 알려주셔서 수정 완료.
     */

    //get comments (특정 카드에 달려있는 comments 가져오기)
    public Map<String, Object> readComments(Long card_id, User principal) {
        Map<String, Object> commentInfo = new LinkedHashMap<>();

        com.backend.teamtalk.domain.User user = userRepository.findByUsername(principal.getUsername())
                .orElseThrow(
                () -> new IllegalArgumentException("nobody else...")
        );

        //카드에 달려있는 댓글들 가져오기
        Card card = cardRepository.findById(card_id).orElseThrow(
                () -> new IllegalArgumentException("There is no card.")
        );
        List<Comment> comments = card.getComments();


        commentInfo.put("cardId", card_id);
        commentInfo.put("comments", comments);

        return commentInfo;
    }

    /*
     * 에러 테스트: 예상 지점으로 에러가 잘 흘러감.(성공)
     * comment 작성한 유저 == comment 를 수정하려는 유저
     * 의문: null 처리 말고 다른 예외 처리 방법 찾아 볼 것.
     */

    //update comment
    @Transactional
    public Long updateComment(Long card_id, Long comment_id, CommentRequestDto requestDto, User principal) {
        Card card = cardRepository.findById(card_id).orElseThrow(
                () -> new IllegalArgumentException("There is no card.")
        );

        Comment comment = commentRepository.findById(comment_id).orElseThrow(
                () -> new IllegalArgumentException("There is no reply.")
        );

        com.backend.teamtalk.domain.User user = userRepository.findByUsername(principal.getUsername()).orElseThrow(
                () -> new IllegalArgumentException("There is nobody by that name.")
        );

        //코멘트를 수정하려는 유저와, 코멘트를 작성한 유저가 일치하는지
        if (!comment.getUser().getId().equals(user.getId())) {
            return null;
        } else {
            comment.update(requestDto, user, card);

            return comment.getId();
        }


    }

    /*
     * 실수: 반환형을 boolean 으로 테스트 해보니, comment 가 삭제 됐다고 메세지는 뜨는데 db 에서 삭제가 안됨.
     * 반환형: Comment (성공), Long(성공)
     */

    //delete comment
    public Long deleteComment(Long card_id, Long comment_id, User principal) {
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
            return null;
        } else {
            commentRepository.deleteById(comment_id);

            return comment.getId();
        }

    }
}
