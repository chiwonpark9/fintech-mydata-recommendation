package com.team7.controller;

import com.team7.cloud.service.AwsS3Service;
import com.team7.controller.util.ControllerUtil;
import com.team7.db.dto.CardDto;
import com.team7.db.model.entity.Card;
import com.team7.db.model.entity.Mbti;
import com.team7.db.model.entity.MyData;
import com.team7.db.model.entity.User;
import com.team7.db.model.relationship.CardBenefit;
import com.team7.service.entitiy.CustomerService;
import com.team7.service.entitiy.MyDataService;
import com.team7.service.relationship.CardBenefitService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/recommend")
@RequiredArgsConstructor
public class RecommendationController {
    private final AwsS3Service awsS3Service;
    private final MyDataService myDataService;
    private final CustomerService customerService;
    private final CardBenefitService cardBenefitService;
    private final ControllerUtil controllerutil;

    @GetMapping("/mymbti")
    public RedirectView recommendByMyMbti(HttpServletRequest request, HttpServletResponse response){
        RedirectView redirectView = new RedirectView();
        Optional<User> user = customerService
                .findUserByAccountId(controllerutil.getUserNameFromHeader(request));

        if (user.isEmpty()){
            System.out.println("no user");
            redirectView.setUrl("/cards");
        }

        else{

            Mbti mbti = user.get().getMbti();

            if(mbti ==  null){
                System.out.println("mbti empty");
                redirectView.setUrl("/cards");
            }

            else{
                System.out.println("user with mbti");
                redirectView.setUrl("/cards/mbti/" + mbti.getValue().toUpperCase());
            }

        }

        return redirectView;
    }
    @GetMapping("/mbti/{mbti}")
    public RedirectView recommendByMbti(@PathVariable String mbti, HttpServletRequest request){

        RedirectView redirectView = new RedirectView();
        mbti = mbti.toUpperCase();
        redirectView.setUrl("/cards/mbti/" + mbti);
        return redirectView;
    }

    @GetMapping("/mydata")
    @ResponseBody
    public ArrayList<CardDto> recommendByMyData(HttpServletRequest request, HttpServletResponse response){
        User user = customerService.findUserByAccountId(controllerutil.getUserNameFromHeader(request)).get();
        Optional<MyData> mydata = myDataService.findMyDataByCustomer(user);
        ArrayList<String> benefitOns = new ArrayList<>();

        if(mydata.isPresent()) {
            System.out.println("found mydata for user");
            String lifeStage = mydata.get().getLifeStage();
            if (lifeStage.equals("UNI") ) {
                //쇼핑, 교통, 구독, 편의점, 카페, 해외직구, 간편결제
                benefitOns = Stream.of("쇼핑", "교통", "구독", "편의점", "카페", "해외직구", "간편결제")
                        .collect(Collectors.toCollection(ArrayList::new));
            } else if (lifeStage.equals("CHILD_BABY")  || lifeStage.equals("CHILD_TEEN") || lifeStage.equals("NEW_WED")) {
                //통신-공과금, 주유, 마트, 카페, 여행, 해외직구, 간편결제
                benefitOns = Stream.of("통신", "공과금", "주유", "마트", "카페", "여행", "해외직구", "간편결제")
                        .collect(Collectors.toCollection(ArrayList::new));

            } else if (lifeStage.equals("NEW_JOB")) {
                //쇼핑, 교통/ 구독/ 여행, 해외직구, 간편결제
                benefitOns = Stream.of("쇼핑", "교통", "구독", "해외직구", "간편결제", "여행")
                        .collect(Collectors.toCollection(ArrayList::new));

            } else if (lifeStage.equals("MID_AGE")) {
                benefitOns = Stream.of("통신", "공과금", "주유", "마트", "여행", "교육", "간편결제")
                        .collect(Collectors.toCollection(ArrayList::new));

            } else if (lifeStage.equals("NEW_WED") ) {
                benefitOns = Stream.of("통신", "공과금", "주유", "마트", "여행", "해외직구", "간편결제")
                        .collect(Collectors.toCollection(ArrayList::new));

            }
        }


        ArrayList<CardBenefit> cardBenefits = cardBenefitService.getCardBenefitByBenefitOns(benefitOns);

        HashSet<Card> cards = new HashSet<>(cardBenefits.stream()
                .map(CardBenefit::getCard)
                .collect(Collectors.toSet()));

        ArrayList<CardDto> rv = new ArrayList<>(cards
                .stream()
                .map(card -> new CardDto(card, awsS3Service))
                .collect(Collectors.toList()));
        Collections.shuffle(rv);
        return rv;
    }
}
