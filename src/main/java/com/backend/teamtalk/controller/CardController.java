package com.backend.teamtalk.controller;

import com.backend.teamtalk.domain.Card;
import com.backend.teamtalk.dto.CardDescriptionDto;
import com.backend.teamtalk.dto.CardRequestDto;
import com.backend.teamtalk.repository.CardRepository;
import com.backend.teamtalk.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class CardController {

    private final CardRepository cardRepository;
    private final CardService cardService;


    //create card
    @PostMapping("/api/cards/{pin_id}")
    public String createCard(@PathVariable Long pin_id, @RequestBody CardRequestDto requestDto) {
        cardService.createCard(pin_id, requestDto);
        return "create card: success";

    }

    //get all cards
    @GetMapping("/api/cards")
    public List<Card> getAllCards() {
        List<Card> allCards = cardRepository.findAll();

        return allCards;
    }

    //get one card == card detail
    @GetMapping("/api/cards/{card_id}")
    public Map<String, Object> getOneCard(@PathVariable Long card_id) {
        return cardService.getOneCard(card_id);
    }


    //update card (title)
    @PutMapping("/api/cards/{card_id}") // 수정
    public String updateCard(@PathVariable Long card_id, @RequestBody CardRequestDto requestDto) {
        cardService.updateCard(card_id, requestDto);
        return "update card: success";
    }

    //update card(description)
//    @PutMapping("/api/cards/details/{card_id}")
//    public String updateDescription(@PathVariable Long card_id, @RequestBody CardDescriptionDto requestDto) {
//        cardService.updateDescription(card_id, requestDto);
//        return "update description: success";
//    }


    //delete card
    @DeleteMapping("/api/cards/{card_id}")
    public String deleteCard(@PathVariable Long card_id) {
        cardService.deleteCard(card_id);
        return "delete card: success";
    }
}
