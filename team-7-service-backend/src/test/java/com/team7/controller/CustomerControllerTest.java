package com.team7.controller;

import com.team7.cloud.service.AwsS3Service;
import com.team7.controller.util.ControllerUtil;
import com.team7.db.dto.CardDto;
import com.team7.db.dto.CustomerInfoDTO;
import com.team7.db.dto.MyDataInfoDTO;
import com.team7.db.model.entity.Card;
import com.team7.db.model.entity.Mbti;
import com.team7.db.model.entity.MyData;
import com.team7.db.model.entity.User;
import com.team7.db.model.relationship.CardBenefit;
import com.team7.db.model.relationship.Ownership;
import com.team7.service.entitiy.CustomerService;
import com.team7.service.entitiy.MbtiService;
import com.team7.service.entitiy.MyDataService;
import com.team7.service.relationship.OwnershipService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class CustomerControllerTest {

    @Mock
    private AwsS3Service awsS3Service;
    @Mock
    private CustomerService customerService;
    @Mock
    private OwnershipService ownershipService;
    @Mock
    private MyDataService myDataService;
    @Mock
    private MbtiService mbtiService;
    @Mock
    private ControllerUtil controllerUtil;

    @InjectMocks
    private CustomerController customerController;

    @BeforeEach
    void setup() {
        openMocks(this);
    }

    @Test
    void testGetCustomerInfo() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        when(controllerUtil.getUserNameFromHeader(request)).thenReturn("user1");
        when(customerService.findUserByAccountId("user1")).thenReturn(Optional.of(new User()));

        CustomerInfoDTO result = customerController.getCustomerInfo(request, response);

        assertEquals(new CustomerInfoDTO(new User()).getAccountId(), result.getAccountId());
        verify(customerService, times(1)).findUserByAccountId("user1");
    }

    @Test
    void testGetCustomerCards() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        User user = new User();
        Card card = new Card();
        card.setCardBenefits(new ArrayList< CardBenefit>());
        Ownership ownership = new Ownership();
        ownership.setCard(card);
        ArrayList<Ownership> ownerships = new ArrayList<>();
        ownerships.add(ownership);

        when(controllerUtil.getUserNameFromHeader(request)).thenReturn("user1");
        when(customerService.findUserByAccountId("user1")).thenReturn(Optional.of(user));
        when(ownershipService.getOwnershipsByCustomer(user)).thenReturn(ownerships);

        ArrayList<CardDto> result = customerController.getCustomerCards(request, response);

        assertEquals(1, result.size());
        verify(customerService, times(1)).findUserByAccountId("user1");
        verify(ownershipService, times(1)).getOwnershipsByCustomer(user);
    }

    @Test
    void testGetCustomerMyData() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        User user = new User();
        MyData myData = new MyData();

        when(controllerUtil.getUserNameFromHeader(request)).thenReturn("user1");
        when(customerService.findUserByAccountId("user1")).thenReturn(Optional.of(user));
        when(myDataService.findMyDataByCustomer(user)).thenReturn(Optional.of(myData));

        MyDataInfoDTO result = customerController.getCustomerMyData(request, response);

        assertEquals(new MyDataInfoDTO(myData).getAge(), result.getAge());
        assertEquals(new MyDataInfoDTO(myData).getResidence(), result.getResidence());
        assertEquals(new MyDataInfoDTO(myData).getLifeStage(), result.getLifeStage());
        assertEquals(new MyDataInfoDTO(myData).getVehicleAvailability(), result.getVehicleAvailability());

        verify(customerService, times(1)).findUserByAccountId("user1");
        verify(myDataService, times(1)).findMyDataByCustomer(user);
    }

    @Test
    void testUpdateCustomerMbti() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.addParameter("Mbti", "ESFJ");
        User user = new User();
        Mbti mbti = new Mbti();
        mbti.setMbtiUid(12L);
        mbti.setValue("ESFJ");
        user.setMbti(new Mbti());
        user.getMbti().setValue("ISFJ");
        mbti.setValue("ESFJ");

        when(controllerUtil.getUserNameFromHeader(request)).thenReturn("user2");
        when(customerService.findUserByAccountId("user2")).thenReturn(Optional.of(user));
        when(mbtiService.findMbtiByMbtiValue("ESFJ")).thenReturn(mbti);
        customerController.updateCustomerMbti(request, response);

        assertEquals("ESFJ", user.getMbti().getValue());
        verify(customerService, times(1)).save(user);
    }

    @Test
    void testUpdateCustomerInfo(){
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

    }

}