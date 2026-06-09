package com.team7.controller;

import com.team7.cloud.service.AwsS3Service;
import com.team7.db.dto.CardDto;
import com.team7.db.model.entity.Card;
import com.team7.db.model.entity.Mbti;
import com.team7.service.entitiy.MbtiService;
import com.team7.service.relationship.CardBenefitService;
import com.team7.service.entitiy.CardService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/cards")

public class CardInfoController {
    public final CardService cardService;
    public final CardBenefitService cardBenefitService;
    public final AwsS3Service awsS3Service;
    public final MbtiService mbtiService;
    @ResponseBody
    @GetMapping()
    public ArrayList<CardDto> getCards(){
        return cardService.cardsToCardDtos(cardService.findAllCards());
    }

    @ResponseBody
    @GetMapping("/creditCards")
    public ArrayList<CardDto> getCreditCards(){
        ArrayList<CardDto> cards = new ArrayList<>(cardService.findCardsByType("신용")
                .stream()
                .map(card -> new CardDto(card, awsS3Service))
                .collect(Collectors.toList()));
        Collections.shuffle(cards);
        return cards;
    }

    @ResponseBody
    @GetMapping("/prepaidCards")
    public ArrayList<CardDto> getPrepaidCards(){
        ArrayList<CardDto> cards = new ArrayList<>(cardService.findCardsByType("체크")
                .stream()
                .map(card -> new CardDto(card, awsS3Service))
                .collect(Collectors.toList()));
        Collections.shuffle(cards);
        return cards;
    }


    @ResponseBody
    @GetMapping("/card/{id}")
    public CardDto getCardByUid(@PathVariable Long id){
        Card card = cardService.findCardByCardUid(id);
        CardDto rv = new CardDto(card, awsS3Service);
        return rv;
    }

    @ResponseBody
    @GetMapping("/search/{name}")
    public ArrayList<CardDto> getSearachCards(@PathVariable String name){
        ArrayList<CardDto> rv;
        ArrayList<Card> cards = cardService.searchCardsByName(name);
        log.info(String.valueOf(cards.size()));
        rv = new ArrayList<>(cards.stream().map(card -> cardService.cardToCardDto(card)).collect(Collectors.toList()));
        return rv;

    }

    @ResponseBody
    @GetMapping("/mbti/{mbti}")
    public ArrayList<CardDto> getMbtiCards(@PathVariable String mbti, HttpServletResponse response){
        ArrayList<CardDto> rv;
        try {
            mbti = mbti.toUpperCase();
            Mbti mbtiInstance = mbtiService.findMbtiByMbtiValue(mbti);
            ArrayList<Card> cards = cardService.findCardsByMbti(mbtiInstance);

            rv =new ArrayList<>(cards.stream().map(card -> cardService.cardToCardDto(card)).collect(Collectors.toList()));
            Collections.shuffle(rv);
            return rv;
        }
        catch(Exception e){
            response.setStatus(HttpStatus.FORBIDDEN.value());
            return null;
        }

    }
}
