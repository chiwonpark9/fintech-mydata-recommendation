package com.team7.db.dto;

import com.team7.db.model.entity.User;
import com.team7.db.model.entity.MyData;
import com.team7.db.model.entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MyDataDto {
    private Long mydataId;
    private int age;
    private String residence;
    private Boolean vehicleAvailability;
    private String lifeStage;
    private User user;

    public MyDataDto(MyData myData){
        this.mydataId = myData.getMydataId();
        this.age = myData.getAge();
        this.residence = myData.getResidence();
        this.vehicleAvailability = myData.getVehicleAvailability();
        this.lifeStage = myData.getLifeStage();
        this.user = myData.getUser();
    }
}
