package com.team7.db.repository.card;

import com.team7.db.model.entity.Card;
import com.team7.db.model.entity.User;
import com.team7.db.model.relationship.Ownership;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;

public interface OwnershipRepository extends JpaRepository<Ownership, Long> {

    public ArrayList<Ownership> findOwnershipsByUser(User user);
    public ArrayList<Ownership> findOwnershipByCard(Card card);

}
