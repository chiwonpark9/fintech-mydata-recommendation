package com.team7.db.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "my_data")
public class MyData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mydata_id")
    private Long mydataId;

    @Column(name = "age", nullable = false)
    private int age;

    @Column(name = "residence", length = 20, nullable = false)
    private String residence;

    @Column(name = "vehicle_availability", nullable = false)
    private Boolean vehicleAvailability;

    @Column(name = "life_stage", length = 20, nullable = false)
    private String lifeStage;

    @OneToOne
//    @JoinColumn(name = "customer", referencedColumnName = "customer_uid", nullable = false)
    @JoinColumn(name = "customer", referencedColumnName = "uid", nullable = false)
    private User user;

}
