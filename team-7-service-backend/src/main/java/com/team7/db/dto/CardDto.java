package com.team7.db.dto;


import com.team7.cloud.service.AwsS3Service;
import com.team7.db.model.entity.Card;
import com.team7.db.model.entity.CardVendor;
import com.team7.db.model.entity.Mbti;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Getter
@Setter
public class CardDto {


    private Long cardUid;

    private String name;

    private String type;

    private Long annualFee;

    private int available;

    private String image;

    private CardVendor cardVendor;

    private Mbti mbti;

    ArrayList<BenefitDto> benefits;


    public CardDto(Card card, AwsS3Service awsS3Service){
        this.cardUid = card.getCardUid();
        this.name = card.getName();
        this.type = card.getType();
        this.annualFee = card.getAnnualFee();
        this.available = card.getAvailable();
        this.image = awsS3Service.getFilePresignedURL(card.getImage());
        this.cardVendor = card.getCardVendor();
        this.mbti = card.getMbti();
        benefits = new ArrayList<>(card.getCardBenefits()
                .stream()
                .map(cardBenefit -> new BenefitDto(cardBenefit))
                .collect(Collectors.toList()));

    }
}
