package com.team7.db.dto;

import com.team7.db.model.entity.MyData;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MyDataInfoDTO {
    private int age;
    private String residence;
    private Boolean vehicleAvailability;
    private String lifeStage;

    public MyDataInfoDTO(MyData myData){
        this.age = myData.getAge();
        this.residence = myData.getResidence();
        this.vehicleAvailability = myData.getVehicleAvailability();
        this.lifeStage = myData.getLifeStage();
    }
}
