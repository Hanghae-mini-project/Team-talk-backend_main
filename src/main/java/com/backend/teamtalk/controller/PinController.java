package com.backend.teamtalk.controller;


import com.backend.teamtalk.domain.Pin;
import com.backend.teamtalk.dto.PinRequestDto;
import com.backend.teamtalk.repository.PinRepository;
import com.backend.teamtalk.service.PinService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@RequiredArgsConstructor
@RestController
public class PinController {

    private final PinRepository pinRepository;
    private final PinService pinService;


    //create pin
    @PostMapping("/api/pins/{board_id}")
    public String createPin(@PathVariable Long board_id, @RequestBody PinRequestDto requestDto) {
        pinService.createPin(board_id, requestDto);

        return "create pin: success.";
    }


    //get one pin.
    @GetMapping("/api/pins/{pin_id}")
    public Map<String, Object> getOnePin(@PathVariable Long pin_id) {
        return pinService.getOnePin(pin_id);
    }

    //get all pins.
    @GetMapping("/api/pins")
    public List<Pin> getAllPins() {
        List<Pin> allPins = pinRepository.findAll();
        return allPins;
    }

    //update pin.
    @PutMapping("/api/pins/{pin_id}")
    public String updatePin(@PathVariable Long pin_id, @RequestBody PinRequestDto requestDto) {
        pinService.updatePin(pin_id, requestDto);
        return "update pin: success.";
    }

    //delete pin.
    @DeleteMapping("/api/pins/{pin_id}")
    public String deletePin(@PathVariable Long pin_id) {
        pinService.deletePin(pin_id);
        return "delete pin: success.";
    }
}
