package com.backend.teamtalk.service;

import com.backend.teamtalk.domain.Board;
import com.backend.teamtalk.dto.BoardRequestDto;
import com.backend.teamtalk.jwt.JwtAuthenticationEntryPoint;
import com.backend.teamtalk.repository.BoardRepository;
import com.backend.teamtalk.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.MalformedParameterizedTypeException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@RequiredArgsConstructor
@Service
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;


    //get all boards
    public Map<String, Object> getAllBoards(String username) {
        Map<String, Object> userInfo = new LinkedHashMap<>();

//        List<Board> boards = boardRepository.findByUsername(username); //널 처리 할 것
        com.backend.teamtalk.domain.User user = userRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("There is nobody by that name.")
        );

        List<Board> boards = boardRepository.findByUserId(user.getId());

        userInfo.put("userId", user.getId());   //어차피 전부 제이슨으로 줘야 하니까
        userInfo.put("userSkill", user.getSkill());
        userInfo.put("boards", boards);

        return userInfo;

    }


    //get one board
    public Board getOneBoard(Long board_id) {
        Board board = boardRepository.findById(board_id)
                .orElseThrow(IllegalArgumentException::new);
        return board;
    }

//    create board
//    public void createBoard(BoardRequestDto requestDto) {
//        Board board = new Board(requestDto);
//        boardRepository.save(board);
//    }

    //create board (login)
    //이미 파라미터로 받은 User 에 데이터가 없으면 principal. 점 찍는 순간 null pointer exception 터져버림
    public void createBoard(BoardRequestDto requestDto, User principal) {
        //로그인을 해야 board 생성이 되는데, 이런 일이 일어날까?
        com.backend.teamtalk.domain.User user = userRepository.findByUsername(principal.getUsername()).orElseThrow(
                () -> new IllegalArgumentException("There is nobody by that name.")
        );    //왜 안돼지???? 왜 의존성예외가 터지지

//        if (principal == null) {    //수정 해야 해
//            //500에러 말고 예외처리를 좀 하고 싶은데..어떻게 하지? -> 전혀 안먹히네
//            //내 에러가 안 터지고, 핸들러 에러 401로 간다.
//            new IllegalArgumentException("There is nobody by that name.");
//        }
//
//        String username = principal.getUsername();
//        com.backend.teamtalk.domain.User user = userRepository.findByUsername(username).orElseThrow(
//                () -> new IllegalArgumentException("nobody")
//        );

        Board board = new Board(requestDto, user);
        boardRepository.save(board);
    }

    /**
     * 글 작성자 == 글 수정자
     * return 아무거나 해도 될듯
     * @param board_id
     * @param requestDto
     * @param principal
     */
    //update board
    @Transactional
    public Board updateBoard(Long board_id, BoardRequestDto requestDto, User principal) {
        Board board = boardRepository.findById(board_id)
                .orElseThrow(IllegalArgumentException::new);    //3번 보드

        com.backend.teamtalk.domain.User user = userRepository.findByUsername(principal.getUsername()).orElseThrow(
                () -> new IllegalArgumentException("There is nobody by that name.")
        );

        if (!board.getUser().getId().equals(user.getId())) {
            return null;    //null 말고 다른 거 하고 싶은데
        } else {
            board.update(requestDto);
        }
        return board;

    }

    //delete board
    public Board deleteBoard(Long board_id, User principal) {
        Board board = boardRepository.findById(board_id).orElseThrow(
                () -> new IllegalArgumentException("There is no bulletin board.")
        );

        com.backend.teamtalk.domain.User user = userRepository.findByUsername(principal.getUsername()).orElseThrow(
                () -> new IllegalArgumentException("There is nobody by that name.")
        );
        //반복 코드 메서드로 빼도 될 듯

        if (!board.getUser().getId().equals(user.getId())) {
            return null;    //null 말고 다른 거 하고 싶은데
        } else {
            boardRepository.deleteById(board_id);
        }
        return board;
    }


}
