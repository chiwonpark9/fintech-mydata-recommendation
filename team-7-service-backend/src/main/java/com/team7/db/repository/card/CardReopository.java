package com.team7.db.repository.card;

import com.team7.db.model.entity.Card;
import com.team7.db.model.entity.CardVendor;
import com.team7.db.model.entity.Mbti;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface CardReopository extends JpaRepository<Card, Long> {

    public Card getCardByCardUid(Long uid);

    public Card findCardByCardUid(Long uid);

    public ArrayList<Card> findCardsByName(String name);

    public ArrayList<Card> findCardsByType(String type);

    public ArrayList<Card> findCardsByAvailable(int available);

    public ArrayList<Card> findCardsByCardVendor(CardVendor cardVendor);

    public ArrayList<Card> findCardsByMbti(Mbti mbti);

    public ArrayList<Card> findCardsByNameLike(String name);

}
