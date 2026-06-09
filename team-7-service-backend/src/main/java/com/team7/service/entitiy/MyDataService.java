package com.team7.service.entitiy;

import com.team7.db.dto.MyDataDto;
import com.team7.db.model.entity.User;
import com.team7.db.model.entity.MyData;
import com.team7.db.repository.customer.MyDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MyDataService {
    private final MyDataRepository myDataRepository;

    public MyData findMyDataByMydataId(Long mydataId){
        MyData myData = myDataRepository.findMyDataByMydataId(mydataId);
        return myData;
    };

    public Optional<MyData> findMyDataByCustomer(User user){
        Optional<MyData> myData = myDataRepository.findMyDataByUser(user);
        return myData;
    };

    public ArrayList<MyData> findMyDatasByAge(int age){
        ArrayList<MyData> myDatas = myDataRepository.findMyDatasByAge(age);
        return myDatas;
    };

    public ArrayList<MyData> findMyDatasByResidence(String residence){
        ArrayList<MyData> myDatas = myDataRepository.findMyDatasByResidence(residence);
        return myDatas;
    };

    public ArrayList<MyData> findMyDatasByVehicleAvailability(Boolean vehicleAvaiability){
        ArrayList<MyData> myDatas = myDataRepository.findMyDatasByVehicleAvailability(vehicleAvaiability);
        return myDatas;
    };

    public ArrayList<MyData> findMyDatasByLifeStage(String lifeStage){
        ArrayList<MyData> myDatas = myDataRepository.findMyDatasByLifeStage(lifeStage);
        return myDatas;
    };

    public MyDataDto myDataToMyDataDto(MyData myData){return new MyDataDto((myData));}

    public ArrayList<MyDataDto> myDatasToMyDataDtos(ArrayList<MyData> myDatas){

        return new ArrayList<>(myDatas.stream().map(myData -> new MyDataDto(myData)).collect(Collectors.toList()));
    }

    public ArrayList<MyData> findAllMyDatas(){
        ArrayList<MyData> rv = new ArrayList<>(myDataRepository.findAll());
        return rv;
    }

}
