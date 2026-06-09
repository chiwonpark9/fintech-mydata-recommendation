package com.team7.controller;

import com.team7.cloud.service.AwsS3Service;
import com.team7.db.dto.CardDto;
import com.team7.db.model.entity.Card;
import com.team7.db.model.entity.Mbti;
import com.team7.db.model.relationship.CardBenefit;
import com.team7.service.entitiy.MbtiService;
import com.team7.service.relationship.CardBenefitService;
import com.team7.service.entitiy.CardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class CardInfoControllerTest {

    @Mock
    private CardService cardService;

    @Mock
    private CardBenefitService cardBenefitService;

    @Mock
    private AwsS3Service awsS3Service;

    @Mock
    private MbtiService mbtiService;

    @InjectMocks
    private CardInfoController cardInfoController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetCards() {
        // Mocking
        when(cardService.findAllCards()).thenReturn(new ArrayList<>());

        // Test
        ArrayList<CardDto> result = cardInfoController.getCards();

        // Verification
        assertEquals(0, result.size());
    }

    @Test
    public void testGetCreditCards() {
        // Mocking
        when(cardService.findCardsByType("신용")).thenReturn(new ArrayList<>());

        // Test
        ArrayList<CardDto> result = cardInfoController.getCreditCards();

        // Verification
        assertEquals(0, result.size());
    }

    @Test
    public void testGetPrepaidCards() {
        // Mocking
        when(cardService.findCardsByType("체크")).thenReturn(new ArrayList<>());

        // Test
        ArrayList<CardDto> result = cardInfoController.getPrepaidCards();

        // Verification
        assertEquals(0, result.size());
    }

    @Test
    public void testGetCardByUid() {
        // Mocking
        Card card = new Card();
        card.setCardBenefits(new ArrayList<CardBenefit>());
        when(cardService.findCardByCardUid(anyLong())).thenReturn(card);

        // Test
        CardDto result = cardInfoController.getCardByUid(1L);

        // Verification
        assertEquals(CardDto.class, result.getClass());
    }

    @Test
    public void testGetSearchCards() {
        // Mocking
        when(cardService.searchCardsByName(anyString())).thenReturn(new ArrayList<>());

        // Test
        ArrayList<CardDto> result = cardInfoController.getSearachCards("test");

        // Verification
        assertEquals(0, result.size());
    }

    @Test
    public void testGetMbtiCards() {
        // Mocking
        when(mbtiService.findMbtiByMbtiValue(anyString())).thenReturn(new Mbti());
        when(cardService.findCardsByMbti(any(Mbti.class))).thenReturn(new ArrayList<>());

        // Test
        ArrayList<CardDto> result = cardInfoController.getMbtiCards("test", new MockHttpServletResponse());

        // Verification
        assertEquals(0, result.size());
    }

    @Test
    public void testGetMbtiCards_Exception() {

        MockHttpServletResponse response = new MockHttpServletResponse();
        // Mocking
        when(mbtiService.findMbtiByMbtiValue(anyString())).thenThrow(new RuntimeException());

        // Test
        ArrayList<CardDto> result = cardInfoController.getMbtiCards("test", response);

        // Verification
        assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatus());
    }
}
