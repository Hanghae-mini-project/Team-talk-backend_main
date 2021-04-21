package com.backend.teamtalk.service;

import com.backend.teamtalk.domain.Board;
import com.backend.teamtalk.domain.Pin;
import com.backend.teamtalk.dto.PinRequestDto;
import com.backend.teamtalk.repository.BoardRepository;
import com.backend.teamtalk.repository.PinRepository;
import com.backend.teamtalk.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@RequiredArgsConstructor
@Service
public class PinService {

    private final PinRepository pinRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;


    //로그인 하지 않은 사람이 url 로 쳐서 직접 들어올 수 있을까?
    //create pin
    public void createPin(Long board_id, PinRequestDto requestDto, User principal) {
        //user 호출
        com.backend.teamtalk.domain.User user = userRepository.findByUsername(principal.getUsername()).orElseThrow(
                () -> new IllegalArgumentException("There is nobody by that name.")
        );

        //board 호출
        Board board = boardRepository.findById(board_id).orElseThrow(
                        () -> new IllegalArgumentException("There is no bulletin board.")
        );

        Pin pin = new Pin(requestDto, board);
        pinRepository.save(pin);

    }



    //get one pin.
    public Map<String, Object> getOnePin(Long pin_id) {

        Map<String, Object> pinInfo = new LinkedHashMap<>();
        //pin 호출
        Pin pin = pinRepository.findById(pin_id)
                .orElseThrow(IllegalArgumentException::new);

        Long boardId = pin.getBoard().getId();

        pinInfo.put("boardId", boardId);
        pinInfo.put("pin", pin);

        return pinInfo;
    }


    //update pin
    @Transactional
    public void updatePin(Long pin_id, PinRequestDto requestDto) {
        Pin pin = pinRepository.findById(pin_id).orElseThrow(
                () -> new IllegalArgumentException("There is no pin.")
        );
        pin.update(requestDto);
    }

    //delete pin
    public void deletePin(Long pin_id) {
        pinRepository.findById(pin_id).orElseThrow(
                () -> new IllegalArgumentException("There is no pin.")
        );
        pinRepository.deleteById(pin_id);
    }
}

