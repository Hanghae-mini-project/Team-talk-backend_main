package com.backend.teamtalk.controller;

import com.backend.teamtalk.domain.Comment;
import com.backend.teamtalk.dto.CommentRequestDto;
import com.backend.teamtalk.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class CommentController {
    //comment 생성, 수정, 삭제

    private final CommentService commentService;


    //create comment
    @PostMapping("/cards/{card_id}/comments")
    public String createComment(@PathVariable Long card_id,
                                @RequestBody CommentRequestDto requestDto,
                                @AuthenticationPrincipal User principal) {
        commentService.createComment(card_id, requestDto, principal);
        return "create comment: success";
    }

    //comment written by user
    @GetMapping("/cards/{card_id}/comments")
    public Map<String, Object> ReadComments(@PathVariable Long card_id,
                                            @AuthenticationPrincipal User principal) {
        return commentService.readComments(card_id, principal);
    }

    //update comment -> 누구나 다 조회 가능한 코멘트.
    @PutMapping("/cards/{card_id}/comments/{comment_id}")
    public String updateComment(@PathVariable Long card_id,
                                @PathVariable Long comment_id,
                                @RequestBody CommentRequestDto requestDto,
                                @AuthenticationPrincipal User principal) {
        commentService.updateComment(card_id, comment_id, requestDto, principal);

        return "update card: success";
    }

    //delete comment
    @DeleteMapping("/cards/{card_id}/comments/{comment_id}")
    public String deleteComment(@PathVariable Long card_id,
                                @PathVariable Long comment_id,
                                @AuthenticationPrincipal User principal) {
        commentService.deleteComment(card_id, comment_id, principal);

        return "delete card: success";
    }
}
