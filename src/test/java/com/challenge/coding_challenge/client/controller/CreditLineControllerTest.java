package com.challenge.coding_challenge.client.controller;

import com.challenge.coding_challenge.client.handler.RestErrorHandler;
import com.challenge.coding_challenge.domain.exception.TooManyRequestsException;
import com.challenge.coding_challenge.domain.model.CreditLineDomain;
import com.challenge.coding_challenge.domain.model.FoundingTypeDomain;
import com.challenge.coding_challenge.domain.usecase.CreateCreditLineForACustomerUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.SneakyThrows;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CreditLineController.class)
@AutoConfigureMockMvc
@ContextConfiguration(classes = {CreditLineController.class, RestErrorHandler.class})
public class CreditLineControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CreateCreditLineForACustomerUseCase createCreditLineForACustomer;

    private final LocalDateTime currentDate = LocalDateTime.now();

    private final CreditLineDomain creditLineToBeApproved = new CreditLineDomain(null, "1234550", FoundingTypeDomain.Startup,
            6550.50, 80500.00, 11000.00, currentDate, null,
            null, null, null);

    private final CreditLineDomain creditLineApproved = new CreditLineDomain(null, "1234550", FoundingTypeDomain.Startup,
            6550.50, 80500.00, 11000.00, currentDate, 16000.00,
            true, 11000.00, CreditLineDomain.CREDIT_AUTHORIZED);

    private final CreditLineDomain creditLineToBeNotApproved = new CreditLineDomain(null, "1234551", FoundingTypeDomain.SME,
            8550.50, 80500.00, 35000.00, currentDate, null,
            null, null, null);

    private final CreditLineDomain creditLineNotApproved = new CreditLineDomain(null, "1234551", FoundingTypeDomain.SME,
            8550.50, 80500.00, 35000.00, currentDate, 12500.00,
            false, 0.0, CreditLineDomain.CREDIT_NOT_AUTHORIZED);

    private final CreditLineDomain creditLineWithoutClientId = new CreditLineDomain(null, "", FoundingTypeDomain.Startup,
            6550.50, 80500.00, 11000.00, currentDate, null,
            null, null, null);

    private final CreditLineDomain creditLineWithoutCashBalance = new CreditLineDomain(null, "1234552", FoundingTypeDomain.Startup,
            null, 80500.00, 11000.00, currentDate, null,
            null, null, null);


    @BeforeEach
    void init() {
        clearAllCaches();
    }

    @Test
    @SneakyThrows
    void shouldAcceptedTheCreditLine() {
        when(createCreditLineForACustomer.execute(any())).thenReturn(creditLineApproved);

        mockMvc.perform(post("/v1/credit_line")
                .content(new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(creditLineToBeApproved))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.accepted").value(true))
                .andExpect(jsonPath("$.creditLineAuthorized").value(creditLineToBeApproved.getRequestedCreditLine()))
                .andExpect(jsonPath("$.message").value(CreditLineDomain.CREDIT_AUTHORIZED))
                .andExpect(status().isCreated()).andDo(print());
    }

    @Test
    @SneakyThrows
    void shouldNotAcceptedTheCreditLine() {
        when(createCreditLineForACustomer.execute(any())).thenReturn(creditLineNotApproved);

        mockMvc.perform(post("/v1/credit_line")
                        .content(new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(creditLineToBeNotApproved))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.accepted").value(false))
                .andExpect(jsonPath("$.creditLineAuthorized").value(0.0))
                .andExpect(jsonPath("$.message").value(CreditLineDomain.CREDIT_NOT_AUTHORIZED))
                .andExpect(status().isCreated()).andDo(print());
    }

    @Test
    @SneakyThrows
    void tryToDoManyCreditLineRequests() {
        when(createCreditLineForACustomer.execute(any())).thenThrow(new TooManyRequestsException());

        mockMvc.perform(post("/v1/credit_line")
                        .content(new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(creditLineToBeApproved))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errors").value(Matchers.containsInAnyOrder("Too many requests")))
                .andExpect(status().isTooManyRequests()).andDo(print());
    }

    @Test
    @SneakyThrows
    void tryToRequestACreditLineButGetAnInternalError() {
        when(createCreditLineForACustomer.execute(any())).thenThrow(new RuntimeException("Internal error"));

        mockMvc.perform(post("/v1/credit_line")
                        .content(new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(creditLineToBeApproved))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errors").value(Matchers.containsInAnyOrder("Internal error")))
                .andExpect(status().isInternalServerError()).andDo(print());
    }

    @Test
    @SneakyThrows
    void tryToRequestACreditLineWithoutClientId() {
        mockMvc.perform(post("/v1/credit_line")
                        .content(new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(creditLineWithoutClientId))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errors").value(Matchers.containsInAnyOrder("The clientId must be informed")))
                .andExpect(status().isBadRequest()).andDo(print());
    }

    @Test
    @SneakyThrows
    void tryToRequestACreditLineWithoutCacheBalance() {
        mockMvc.perform(post("/v1/credit_line")
                        .content(new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(creditLineWithoutCashBalance))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errors").value(Matchers.containsInAnyOrder("The cashBalance must be informed")))
                .andExpect(status().isBadRequest()).andDo(print());
    }

    @Test
    @SneakyThrows
    void tryToRequestACreditLineWithoutBody() {
        mockMvc.perform(post("/v1/credit_line")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()).andDo(print());
    }

}