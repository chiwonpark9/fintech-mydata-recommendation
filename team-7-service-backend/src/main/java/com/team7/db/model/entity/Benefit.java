package com.team7.db.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "benefit")
public class Benefit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "benefit_uid")
    private Long benefitUid;

    @Column(name = "benefit_on", length = 20, nullable = false)
    private String benefitOn;

    @Column(name = "type", length = 20, nullable = false)
    private String type;

    @Column(name = "unit", length = 5, nullable = false)
    private String unit;

}
