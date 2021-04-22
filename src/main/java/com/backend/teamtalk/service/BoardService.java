package com.backend.teamtalk.service;

import com.backend.teamtalk.domain.Board;
import com.backend.teamtalk.dto.BoardRequestDto;
import com.backend.teamtalk.repository.BoardRepository;
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
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;


    /*
     * 프론트에서 요청한 데이터: userId, userSkill, boards
     */
    //get all boards (로그인 후 보이게 되는 메인 화면)
    public Map<String, Object> getAllBoards(String username) {
        Map<String, Object> userInfo = new LinkedHashMap<>();

        com.backend.teamtalk.domain.User user = userRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("There is nobody by that name.")
        );

        List<Board> boards = boardRepository.findByUserId(user.getId());

        userInfo.put("userId", user.getId());
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


    /*
     * 실수: principal.getUsername() 의 결과 값을 변수로 따로 설정해 줌 -> nullPointerException 터짐 (당연)
     * 의문: 로그인을 해야 볼 수 있는 화면이고, 그 후 board 를 생성할 수 있는데 user 정보가 없어서 null 터지는 경우가 있을까?
     */

    //create board (login)
    public void createBoard(BoardRequestDto requestDto, User principal) {
        com.backend.teamtalk.domain.User user = userRepository.findByUsername(principal.getUsername()).orElseThrow(
                () -> new IllegalArgumentException("There is nobody by that name.")
        );

        Board board = new Board(requestDto, user);
        boardRepository.save(board);
    }


    /*
     * 글을 작성한 사람 == 로그인 한 사람일 경우만 수정, 삭제 가능.
     * 테스트 했을 때 잘 돌아간다면 반환형 타입 전부 수정할 것.
     * 의문: return null 말고 다른 예외처리를 사용할 수 없을까...?
     */

    //update board
    @Transactional
    public Long updateBoard(Long board_id, BoardRequestDto requestDto, User principal) {
        Board board = boardRepository.findById(board_id)
                .orElseThrow(IllegalArgumentException::new);

        com.backend.teamtalk.domain.User user = userRepository.findByUsername(principal.getUsername()).orElseThrow(
                () -> new IllegalArgumentException("There is nobody by that name.")
        );

        if (!board.getUser().getId().equals(user.getId())) {
            return null;
        } else {
            board.update(requestDto);
        }
        return board.getId();   //*****

    }

    //delete board
    public Long deleteBoard(Long board_id, User principal) {
        Board board = boardRepository.findById(board_id).orElseThrow(
                () -> new IllegalArgumentException("There is no bulletin board.")
        );

        com.backend.teamtalk.domain.User user = userRepository.findByUsername(principal.getUsername()).orElseThrow(
                () -> new IllegalArgumentException("There is nobody by that name.")
        );


        if (!board.getUser().getId().equals(user.getId())) {
            return null;
        } else {
            boardRepository.deleteById(board_id);
        }
        return board.getId();
    }


}
