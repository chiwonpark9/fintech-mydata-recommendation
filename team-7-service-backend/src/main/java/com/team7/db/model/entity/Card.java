package com.team7.db.model.entity;
import com.team7.db.model.relationship.CardBenefit;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "card")
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "card_uid")
    private Long cardUid;

    @Column(name = "name", length = 30, nullable = false)
    private String name;

    @Column(name = "type", length = 10, nullable = false)
    private String type;

    @Column(name = "available", nullable = false)
    private int available;

    @Column(name = "annual_fee")
    private Long annualFee;

    @Lob
    @Column(name = "image")
    private String image;

    @ManyToOne
    @JoinColumn(name = "card_vendor", referencedColumnName = "card_vendor_uid", nullable = false)
    private CardVendor cardVendor;

    @ManyToOne
    @JoinColumn(name = "mbti", referencedColumnName = "mbti_uid", nullable = false)
    private Mbti mbti;

    @OneToMany(mappedBy = "card", fetch= FetchType.EAGER)
    List<CardBenefit> cardBenefits;


    // 생성자, getter, setter...1
}