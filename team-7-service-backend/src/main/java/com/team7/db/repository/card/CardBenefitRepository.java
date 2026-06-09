package com.team7.db.repository.card;

import com.team7.db.model.entity.Benefit;
import com.team7.db.model.entity.Card;
import com.team7.db.model.relationship.CardBenefit;
import com.team7.db.model.embededkey.CardBenefitUid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface CardBenefitRepository extends JpaRepository<CardBenefit, CardBenefitUid> {

    public CardBenefit findCardBenefitByCardBenefitUid(CardBenefitUid cardBenefitUid);
    public ArrayList<CardBenefit> findCardBenefitsByCard(Card card);

    public ArrayList<CardBenefit> findCardBenefitsByBenefit(Benefit benefit);

    public ArrayList<CardBenefit> findCardBenefitsByBenefitIn(ArrayList<Benefit> benefits);
}
