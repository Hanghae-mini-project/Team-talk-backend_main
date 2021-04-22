package com.backend.teamtalk.controller;


import com.backend.teamtalk.domain.Board;
import com.backend.teamtalk.dto.BoardRequestDto;
import com.backend.teamtalk.repository.BoardRepository;
import com.backend.teamtalk.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import java.util.Map;


@RequiredArgsConstructor
@RestController
public class BoardController {

    private final BoardService boardService;


    /*
     * 실수: 순환참조 문제 -> 수정 완료.
     * 로그인 한 회원의 보드를 가져오는 메서드.
     * 프론트에서 필요한 데이터: user id, user skill, board id, board title.
     * board 에 관한 정보는 2개만 있으면 되므로 board 객체를 넘길 때 board 의 comments 는 ignore 처리 해놓음.
     */

    //get all boards (로그인 후 보이게 되는 메인 화면)
    @GetMapping("/main/{username}")
    public Map<String, Object> getAllBoards(@PathVariable String username) {
        Map<String, Object> userInfo = boardService.getAllBoards(username);
        return userInfo;
    }

    /*
     * 특정 board 를 클릭하면 보이는 화면
     * teamtalk board 의 특성 상 특정 board 를 클릭했을 때
     * board 안에 속한 pin 들, pin 안에 속한 card 들까지 전부 보여줘야 함.
     * card 는 title 만 보여주고, card 안에 속한 description 과 comments 들은 숨김.
     */

    //get one board
    @GetMapping("/api/boards/{board_id}")
    public Board getOneBoard(@PathVariable Long board_id) {
        return boardService.getOneBoard(board_id);
    }

    //create boards (login)
    @PostMapping("/api/boards")
    public String createBoard(@RequestBody BoardRequestDto requestDto, @AuthenticationPrincipal User principal) {
        boardService.createBoard(requestDto, principal);
        return "create board: success.";
    }

    //update board (title)
    @PutMapping("/api/boards/{board_id}")
    public String updateBoard(@PathVariable Long board_id, @RequestBody BoardRequestDto requestDto, @AuthenticationPrincipal User principal) {
        boardService.updateBoard(board_id, requestDto, principal);
        return "update board: success.";
    }

    //delete board
    @DeleteMapping("api/boards/{board_id}")
    public String deleteBoard(@PathVariable Long board_id, @AuthenticationPrincipal User principal) {
        boardService.deleteBoard(board_id, principal);
        return "delete board: success.";
    }
}
