package com.team7.db.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
@Table(name = "card_vendor")
public class CardVendor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "card_vendor_uid")
    private Long cardVendorUid;

    @Column(name = "name", length = 30, nullable = false)
    private String name;

    // 생성자, getter, setter...
}