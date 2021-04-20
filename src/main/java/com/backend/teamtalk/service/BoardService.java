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
        if (principal == null) {
            //500에러 말고 예외처리를 좀 하고 싶은데..어떻게 하지? -> 전혀 안먹히네
            new IllegalArgumentException("There is nobody by that name.");
        }

        String username = principal.getUsername();
        com.backend.teamtalk.domain.User user = userRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("nobody")
        );

        Board board = new Board(requestDto, user);
        boardRepository.save(board);
    }

//    public void createBoard(BoardRequestDto requestDto, Long user_id) {
//        User user = userRepository.findById(user_id)
//                .orElseThrow(() -> new IllegalArgumentException("nobody"));
//
//        Board board = new Board(requestDto, user);
//        boardRepository.save(board);
//    }

    //update board
    @Transactional
    public void updateBoard(Long board_id, BoardRequestDto requestDto) {
        Board board = boardRepository.findById(board_id)
                .orElseThrow(IllegalArgumentException::new);
        board.update(requestDto);
    }

    //delete board
    public void deleteBoard(Long board_id) {
        boardRepository.findById(board_id).orElseThrow(
                () -> new IllegalArgumentException("There is no bulletin board.")
        );
        boardRepository.deleteById(board_id);
    }


}
