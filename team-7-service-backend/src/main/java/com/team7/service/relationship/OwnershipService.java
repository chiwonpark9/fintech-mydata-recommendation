package com.team7.service.relationship;

import com.team7.db.model.entity.Card;
import com.team7.db.model.entity.User;
import com.team7.db.model.relationship.Ownership;
import com.team7.db.repository.card.OwnershipRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;


@Getter
@Setter
@Service
@RequiredArgsConstructor
public class OwnershipService {
    private final OwnershipRepository ownershipRepository;

    public ArrayList<Ownership> getOwnershipsByCustomer(User user){
        ArrayList<Ownership> rv = ownershipRepository.findOwnershipsByUser(user);

        return rv;

    }

    public ArrayList<Ownership> getOwnershipsByCard(Card card){
        ArrayList<Ownership> rv = ownershipRepository.findOwnershipByCard(card);
        return rv;
    }
}


