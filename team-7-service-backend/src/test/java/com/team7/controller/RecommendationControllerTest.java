package com.team7.controller;

import com.team7.cloud.service.AwsS3Service;
import com.team7.controller.util.ControllerUtil;
import com.team7.db.dto.CardDto;
import com.team7.db.model.entity.*;
import com.team7.db.model.relationship.CardBenefit;
import com.team7.service.entitiy.CustomerService;
import com.team7.service.entitiy.MyDataService;
import com.team7.service.relationship.CardBenefitService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.servlet.view.RedirectView;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RecommendationControllerTest {

    @Mock
    private AwsS3Service awsS3Service;

    @Mock
    private MyDataService myDataService;

    @Mock
    private CustomerService customerService;

    @Mock
    private CardBenefitService cardBenefitService;

    @Mock
    private ControllerUtil controllerUtil;

    @InjectMocks
    private RecommendationController recommendationController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testRecommendByMyMbti_UserNotFound() {
        // Mocking
        HttpServletRequest request = new MockHttpServletRequest();
        HttpServletResponse response = new MockHttpServletResponse();
        when(controllerUtil.getUserNameFromHeader(request)).thenReturn("testUser");
        when(customerService.findUserByAccountId("testUser")).thenReturn(Optional.empty());

        // Test
        RedirectView result = recommendationController.recommendByMyMbti(request, response);

        // Verification
        assertEquals("/cards", result.getUrl());
    }

    @Test
    public void testRecommendByMyMbti_MbtiEmpty() {
        // Mocking
        HttpServletRequest request = new MockHttpServletRequest();
        HttpServletResponse response = new MockHttpServletResponse();
        when(controllerUtil.getUserNameFromHeader(request)).thenReturn("testUser");
        User user = new User();
        when(customerService.findUserByAccountId("testUser")).thenReturn(Optional.of(user));

        // Test
        RedirectView result = recommendationController.recommendByMyMbti(request, response);

        // Verification
        assertEquals("/cards", result.getUrl());
    }


    @Test
    public void testRecommendByMyMbti_Successful() {
        // Mocking
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(controllerUtil.getUserNameFromHeader(request)).thenReturn("testUser");

        User user = new User();
        Mbti mbti = new Mbti();
        mbti.setValue("testMbti");
        user.setMbti(mbti);
        when(customerService.findUserByAccountId("testUser")).thenReturn(Optional.of(user));

        // Test
        RedirectView result = recommendationController.recommendByMyMbti(request, response);

        // Verification
        assertEquals("/cards/mbti/TESTMBTI", result.getUrl());
    }


    @Test
    public void testRecommendByMbti_SuccessfulRedirect() {
        // Mocking
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURL()).thenReturn(new StringBuffer("http://example.com"));

        // Test
        RedirectView result = recommendationController.recommendByMbti("test", request);

        // Verification
        assertEquals("/cards/mbti/TEST", result.getUrl());
    }

    @Test
    public void testRecommendByMyData_Successful_UNILifeStage() {
        // 의존성 목킹
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(controllerUtil.getUserNameFromHeader(request)).thenReturn("testUser");

        User user = new User();
        when(customerService.findUserByAccountId("testUser")).thenReturn(Optional.of(user));

        MyData myData = new MyData();
        myData.setLifeStage("UNI"); // 사용자의 라이프 스테이지를 시뮬레이션
        when(myDataService.findMyDataByCustomer(any(User.class))).thenReturn(Optional.of(myData));

        ArrayList<String> benefitOns = new ArrayList<>();
        // UNI 라이프 스테이지에 따른 혜택 추가
        // 예시:
         benefitOns.add("쇼핑");

        ArrayList<CardBenefit> cardBenefits = new ArrayList<>();
        // 혜택에 따른 카드 혜택 추가
        // 예시:
        CardBenefit cardBenefit = new CardBenefit();
        cardBenefit.setCard(new Card());
        cardBenefit.setBenefit(new Benefit());
        cardBenefits.add(cardBenefit);

        cardBenefit.getCard().setCardBenefits(cardBenefits);

        when(cardBenefitService.getCardBenefitByBenefitOns(any(ArrayList.class))).thenReturn(cardBenefits);

        // 테스트
        ArrayList<CardDto> result = recommendationController.recommendByMyData(request, response);

        // 검증
        // UNI 라이프 스테이지에 따른 결과를 확인하는 단언을 추가하세요.
        // 예시:
         assertNotEquals(0, result.size());
        // assertEquals(expectedCardDtoForUNILifeStage, result.get(0));
    }

}