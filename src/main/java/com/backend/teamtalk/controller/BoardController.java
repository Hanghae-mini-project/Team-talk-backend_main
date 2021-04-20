package com.backend.teamtalk.controller;


import com.backend.teamtalk.domain.Board;
import com.backend.teamtalk.dto.BoardRequestDto;
import com.backend.teamtalk.repository.BoardRepository;
import com.backend.teamtalk.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@RequiredArgsConstructor
@RestController
public class BoardController {

    private final BoardRepository boardRepository;
    private final BoardService boardService;

    //show boards in main page (로그인 기능 구현했을 시 -> user_id를 가지고 들어옴)
//    @GetMapping("/main")
//    public Board getMain(Authentication authentication) {
//        //로그인 아직 안만들어서 안됨.
//        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
//        // userId꺼내서 로직 처리
//
//    }


    //get all boards
//    @GetMapping("/api")
//    public List<Board> getAllBoards() {
//        List<Board> allBoards = boardRepository.findAll();  //수정,삭제가 빈번하다면 list는 효율이...
//
//        return allBoards;
//    }

    //get all boards (순환참조 막기)  --> Success
    //로그인 한 회원의 보드 가져오기 -> 사실 보드 타이틀, 아이디만 있으면 되기 때문에 board 에서 comment 는 ignore 처리 해놨음
    @GetMapping("/main/{username}")    //username 이 넘어올 수도 있음
    public Map<String, Object> getAllBoards(@PathVariable String username) {
        Map<String, Object> userInfo = boardService.getAllBoards(username);
        return userInfo;
    }
    
    

    //get one board
    @GetMapping("/api/boards/{board_id}") //{board_id}
    public Board getOneBoard(@PathVariable Long board_id) {
        return boardService.getOneBoard(board_id);
    }


    //create board
//    @PostMapping("/api/boards")
//    public String createBoard(@RequestBody BoardRequestDto requestDto) {
//        boardService.createBoard(requestDto);
//        return "create board: success.";
//    }

//    //로그인 기능 없이 보드 만들기
//    @PostMapping("/api/boards")
//    public String createBoard(@RequestBody BoardRequestDto requestDto) {
//        boardService.createBoard(requestDto);
//        return "create board: success.";
//    }

    //create boards (login)
    @PostMapping("/api/boards")
    public String createBoard(@RequestBody BoardRequestDto requestDto, @AuthenticationPrincipal User principal) {
        boardService.createBoard(requestDto, principal);
        return "create board: success.";
    }


    //update board (title)
    @PutMapping("/api/boards/{board_id}")
    public String updateBoard(@PathVariable Long board_id, @RequestBody BoardRequestDto requestDto) {
        boardService.updateBoard(board_id, requestDto);
        return "update board: success.";
    }

    @DeleteMapping("api/boards/{board_id}")
    public String deleteBoard(@PathVariable Long board_id) {
        boardService.deleteBoard(board_id);
        return "delete board: success.";
    }
}
