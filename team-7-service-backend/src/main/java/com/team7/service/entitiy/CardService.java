package com.team7.service.entitiy;

import com.team7.cloud.service.AwsS3Service;
import com.team7.db.dto.CardDto;
import com.team7.db.model.entity.Card;
import com.team7.db.model.entity.CardVendor;
import com.team7.db.model.entity.Mbti;
import com.team7.db.repository.card.CardReopository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CardService {
    private final CardReopository cardRepository;
    private final AwsS3Service awsS3Service;
    public Card findCardByCardUid(Long uid){
        Card rv = cardRepository.findCardByCardUid(uid);
        return rv;
    }

    public ArrayList<Card> findCardsByName(String name){
        ArrayList<Card> rv = cardRepository.findCardsByName(name);
        return rv;
    }

    public ArrayList<Card> findCardsByType(String type){
        ArrayList<Card> rv = cardRepository.findCardsByType(type);
        return rv;
    }

    public ArrayList<Card> findCardsByAvailable(int available){
        ArrayList<Card> rv = cardRepository.findCardsByAvailable(available);
        return rv;
    }
    public ArrayList<Card> findCardsByCardVendor(CardVendor cardVendor){
        ArrayList<Card> rv = cardRepository.findCardsByCardVendor(cardVendor);
        return rv;
    }

    public ArrayList<Card> findCardsByMbti(Mbti mbti){
        ArrayList<Card> rv = cardRepository.findCardsByMbti(mbti);
        return rv;
    }

    public ArrayList<Card> findAllCards(){
        ArrayList<Card> rv = new ArrayList<>(cardRepository.findAll());
        return rv;
    }

    public ArrayList<CardDto> cardsToCardDtos(ArrayList<Card> cards){

        return new ArrayList<>(cards.stream().map(card -> new CardDto(card, awsS3Service)).collect(Collectors.toList()));
    }

    public CardDto cardToCardDto(Card card){
        return new CardDto(card, awsS3Service);
    }

    public ArrayList<Card> searchCardsByName(String name){
        return cardRepository.findCardsByNameLike("%"+name+"%");
    }
}

