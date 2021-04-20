package com.backend.teamtalk.service;

import com.backend.teamtalk.domain.Card;
import com.backend.teamtalk.domain.Pin;
import com.backend.teamtalk.dto.CardDescriptionDto;
import com.backend.teamtalk.dto.CardRequestDto;
import com.backend.teamtalk.repository.CardRepository;
import com.backend.teamtalk.repository.PinRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.Map;


@RequiredArgsConstructor
@Service
public class CardService {

    private final CardRepository cardRepository;
    private final PinRepository pinRepository;


    //create card
    public void createCard(Long pin_id, CardRequestDto requestDto) {
        //해당 pin 가져오기
        Pin pin = pinRepository.findById(pin_id)
                .orElseThrow(() -> new IllegalArgumentException("no exist pin"));   //exception 구글링 하기
        //카드 만들기
        Card card = new Card(requestDto, pin);
        cardRepository.save(card);
    }

    //get one card
    public Map<String, Object> getOneCard(Long card_id) {
        Map<String, Object> cardInfo = new LinkedHashMap<>();

        Card card = cardRepository.findById(card_id)
                .orElseThrow(IllegalArgumentException::new);

        Long pinId = card.getPin().getId();

        cardInfo.put("pinId", pinId);
        cardInfo.put("card", card);

        return cardInfo;
    }

    //update card
    @Transactional
    public void updateCard(Long card_id, CardRequestDto requestDto) {
        Card card = cardRepository.findById(card_id).orElseThrow(
                () -> new IllegalArgumentException("There is no card.")
        );
        //제목 그대로, 컨텐츠만 바꿨다고 가정
        //requestDto의 '제목:카드검색, 내용: 블라블라' 그대로 다시 담긴다.
        card.update(requestDto);
    }

//    //update card (description)
//    @Transactional
//    public void updateDescription(Long card_id, CardDescriptionDto requestDto) {
//        Card card = cardRepository.findById(card_id).orElseThrow(
//                () -> new IllegalArgumentException("There is no card.")
//        );
//        card.updateDescription(requestDto);
//    }


    //delete card
    public void deleteCard(Long card_id) {
        cardRepository.findById(card_id).orElseThrow(
                () -> new IllegalArgumentException("There is no card.")
        );
        cardRepository.deleteById(card_id);
    }


}
