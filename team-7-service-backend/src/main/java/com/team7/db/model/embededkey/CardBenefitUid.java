package com.team7.db.model.embededkey;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@RequiredArgsConstructor
@Embeddable
public class CardBenefitUid implements Serializable {

    private Long cardUid;
    private Long benefitUid;

    // 생성자, getter, setter...

    // equals()와 hashCode() 오버라이드
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CardBenefitUid that = (CardBenefitUid) o;
        return Objects.equals(cardUid, that.cardUid) &&
                Objects.equals(benefitUid, that.benefitUid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cardUid, benefitUid);
    }
}