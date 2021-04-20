package com.backend.teamtalk.service;

import com.backend.teamtalk.domain.Board;
import com.backend.teamtalk.domain.Pin;
import com.backend.teamtalk.dto.PinRequestDto;
import com.backend.teamtalk.repository.BoardRepository;
import com.backend.teamtalk.repository.PinRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@RequiredArgsConstructor
@Service
public class PinService {

    private final PinRepository pinRepository;
    private final BoardRepository boardRepository;


    //create pin
    public void createPin(Long board_id, PinRequestDto requestDto) {
        //board 호출
        Board board = boardRepository.findById(board_id)
                .orElseThrow(IllegalArgumentException::new);

        Pin pin = new Pin(requestDto, board);           //순서 확인 때문에 build 사용하는 건가?
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

