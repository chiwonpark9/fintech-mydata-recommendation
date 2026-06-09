package com.team7.db.model.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "mbti")
public class Mbti {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mbti_uid")
    private Long mbtiUid;

    @Column(name = "mbti_value", length = 5, nullable = false)
    private String value;

    // 생성자, getter, setter...
}