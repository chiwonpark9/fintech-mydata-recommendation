package com.team7.db.model.relationship;

import com.team7.db.model.entity.Card;
import com.team7.db.model.embededkey.OwnershipUid;
import com.team7.db.model.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "ownership")
public class Ownership {

    @EmbeddedId
    private OwnershipUid ownershipUid = new OwnershipUid();

    @ManyToOne
    @MapsId("customerUid")
    @JoinColumn(name = "customer_uid")
    private User user;

    @ManyToOne
    @MapsId("cardUid")
    @JoinColumn(name = "card_uid")
    private Card card;

    // 생성자, getter, setter...
}
