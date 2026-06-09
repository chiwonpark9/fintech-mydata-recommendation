package com.team7.controller.admin;

import com.team7.db.dto.MyDataDto;
import com.team7.db.model.entity.MyData;
import com.team7.service.entitiy.MyDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("admin/mydata")
public class MyDataInfoController {
    public final MyDataService myDataService;
    @ResponseBody
    @GetMapping("/")
    public ArrayList<MyDataDto> getMyDatas(){
        return myDataService.myDatasToMyDataDtos(myDataService.findAllMyDatas());
    }

    @ResponseBody
    @GetMapping("/searh/{age}")
    public ArrayList<MyDataDto> getMyDataByAge(@PathVariable int age){
        ArrayList<MyData> myDatas = myDataService.findMyDatasByAge(age);
        ArrayList<MyDataDto> rv = new ArrayList<>(myDatas.stream().map(myData -> myDataService.myDataToMyDataDto(myData)).collect(Collectors.toList()));
        return rv;
    }

    @ResponseBody
    @GetMapping("/searh/{residence}")
    public ArrayList<MyDataDto> getMyDataByResidence(@PathVariable String residence){
        ArrayList<MyData> myDatas = myDataService.findMyDatasByResidence(residence);
        ArrayList<MyDataDto> rv = new ArrayList<>(myDatas.stream().map(myData -> myDataService.myDataToMyDataDto(myData)).collect(Collectors.toList()));
        return rv;
    }

    @ResponseBody
    @GetMapping("/searh/{vehicleAvailability}")
    public ArrayList<MyDataDto> getMyDataByVehicleAvailability(@PathVariable Boolean vehicleAvailability){
        ArrayList<MyData> myDatas = myDataService.findMyDatasByVehicleAvailability(vehicleAvailability);
        ArrayList<MyDataDto> rv = new ArrayList<>(myDatas.stream().map(myData -> myDataService.myDataToMyDataDto(myData)).collect(Collectors.toList()));
        return rv;
    }

    @ResponseBody
    @GetMapping("/searh/{lifeStage}")
    public ArrayList<MyDataDto> getMyDataByLifeStage(@PathVariable String lifeStage){
        ArrayList<MyData> myDatas = myDataService.findMyDatasByLifeStage(lifeStage);
        ArrayList<MyDataDto> rv = new ArrayList<>(myDatas.stream().map(myData -> myDataService.myDataToMyDataDto(myData)).collect(Collectors.toList()));
        return rv;
    }


}
