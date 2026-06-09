package com.team7.db.model.embededkey;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@RequiredArgsConstructor
@Embeddable
public class OwnershipUid implements Serializable {

    private Long customerUid;
    private Long cardUid;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OwnershipUid that = (OwnershipUid) o;
        return Objects.equals(customerUid, that.customerUid) &&
                Objects.equals(cardUid, that.cardUid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customerUid, cardUid);
    }
}