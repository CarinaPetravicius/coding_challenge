package com.challenge.coding_challenge.domain.usecase;

import com.challenge.coding_challenge.domain.exception.TooManyRequestsException;
import com.challenge.coding_challenge.domain.gateway.GetAllPreviousCreditLineApprovedInAPeriodForCustomerGateway;
import com.challenge.coding_challenge.domain.gateway.GetAllPreviousCreditLineForCustomerGateway;
import com.challenge.coding_challenge.domain.gateway.SaveCreditLineForCustomerGateway;
import com.challenge.coding_challenge.domain.model.CreditLineDomain;
import com.challenge.coding_challenge.domain.model.FoundingTypeDomain;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreateCreditLineForACustomerUseCaseTest {

    @Mock
    private GetAllPreviousCreditLineForCustomerGateway getAllPreviousCreditLineForCustomer;

    @Mock
    private GetAllPreviousCreditLineApprovedInAPeriodForCustomerGateway getAllPreviousCreditLineApprovedInAPeriodForCustomer;

    @Mock
    private SaveCreditLineForCustomerGateway saveCreditLineForCustomer;

    @InjectMocks
    private CreateCreditLineForACustomerUseCase createCreditLineForACustomerUseCase;

    @Test
    void shouldCreateTheFirstAcceptedCredit() {
        final LocalDateTime currentRequestDate = LocalDateTime.now();

        final CreditLineDomain creditLineToBeCreated = new CreditLineDomain(null, "1234560", FoundingTypeDomain.Startup,
                6550.50, 80500.00, 11000.00, currentRequestDate, null,
                null, null, null);

        when(getAllPreviousCreditLineForCustomer.execute(creditLineToBeCreated.getClientId())).thenReturn(List.of());
        doNothing().when(saveCreditLineForCustomer).execute(any());

        final CreditLineDomain creditLineResponse = createCreditLineForACustomerUseCase.execute(creditLineToBeCreated);

        assertTrue(creditLineResponse.getAccepted());
        assertEquals(creditLineToBeCreated.getRequestedCreditLine(), creditLineResponse.getCreditLineAuthorized());
        assertEquals(CreditLineDomain.CREDIT_AUTHORIZED, creditLineResponse.getMessage());
        verifyNoInteractions(getAllPreviousCreditLineApprovedInAPeriodForCustomer);
    }

    @Test
    void shouldCreateTheSecondAcceptedCreditWithin2Minutes() {
        final LocalDateTime currentRequestDate = LocalDateTime.now();
        final LocalDateTime firstRequestDate = currentRequestDate.minusMinutes(1);

        final CreditLineDomain firstCreditLineSavedBefore = new CreditLineDomain(null, "1234561", FoundingTypeDomain.SME,
                4550.50, 80500.00, 10000.00, firstRequestDate,
                16100.00, true, 10000.00, CreditLineDomain.CREDIT_AUTHORIZED);

        final CreditLineDomain creditLineToBeCreated = new CreditLineDomain(null, "1234561", FoundingTypeDomain.SME,
                6550.50, 80500.00, 11000.00, currentRequestDate, null,
                null, null, null);

        final CreditLineDomain secondCreditLineSavedNow = new CreditLineDomain(null, "1234561", FoundingTypeDomain.SME,
                6550.50, 80500.00, 11000.00, currentRequestDate,
                16100.00, true, 10000.00, CreditLineDomain.CREDIT_AUTHORIZED);

        when(getAllPreviousCreditLineForCustomer.execute(creditLineToBeCreated.getClientId())).thenReturn(List.of(firstCreditLineSavedBefore));
        doNothing().when(saveCreditLineForCustomer).execute(any());
        when(getAllPreviousCreditLineApprovedInAPeriodForCustomer.execute(any(), any()))
                .thenReturn(List.of(firstCreditLineSavedBefore, secondCreditLineSavedNow));

        final CreditLineDomain creditLineResponse = createCreditLineForACustomerUseCase.execute(creditLineToBeCreated);

        assertTrue(creditLineResponse.getAccepted());
        assertEquals(firstCreditLineSavedBefore.getCreditLineAuthorized(), creditLineResponse.getCreditLineAuthorized());
        assertEquals(firstCreditLineSavedBefore.getMessage(), creditLineResponse.getMessage());
    }

    @Test
    void shouldCreateTheThirdAcceptedCreditWithin2MinutesButThrowTooManyRequest() {
        final LocalDateTime currentRequestDate = LocalDateTime.now();
        final LocalDateTime firstRequestDate = currentRequestDate.minusMinutes(1);
        final LocalDateTime secondRequestDate = firstRequestDate.plusSeconds(30);

        final CreditLineDomain firstCreditLineSavedBefore = new CreditLineDomain(null, "1234562", FoundingTypeDomain.SME,
                4550.50, 80500.00, 10000.00, firstRequestDate,
                16100.00, true, 10000.00, CreditLineDomain.CREDIT_AUTHORIZED);

        final CreditLineDomain secondCreditLineSavedBefore = new CreditLineDomain(null, "1234562", FoundingTypeDomain.SME,
                6550.50, 81500.00, 12000.00, secondRequestDate,
                16500.00, true, 10000.00, CreditLineDomain.CREDIT_AUTHORIZED);

        final CreditLineDomain creditLineToBeCreated = new CreditLineDomain(null, "1234562", FoundingTypeDomain.SME,
                7550.50, 82500.00, 15000.00, currentRequestDate, null,
                null, null, null);

        final CreditLineDomain thirdCreditLineSavedNow = new CreditLineDomain(null, "1234562", FoundingTypeDomain.SME,
                7550.50, 82500.00, 11000.00, currentRequestDate,
                16800.00, true, 10000.00, CreditLineDomain.CREDIT_AUTHORIZED);

        when(getAllPreviousCreditLineForCustomer.execute(creditLineToBeCreated.getClientId()))
                .thenReturn(List.of(secondCreditLineSavedBefore, firstCreditLineSavedBefore));
        doNothing().when(saveCreditLineForCustomer).execute(any());
        when(getAllPreviousCreditLineApprovedInAPeriodForCustomer.execute(any(), any()))
                .thenReturn(List.of(firstCreditLineSavedBefore, secondCreditLineSavedBefore, thirdCreditLineSavedNow));

        assertThrows(TooManyRequestsException.class, () -> createCreditLineForACustomerUseCase.execute(creditLineToBeCreated));

        verify(getAllPreviousCreditLineForCustomer, times(1)).execute(any());
        verify(saveCreditLineForCustomer, times(1)).execute(any());
        verify(getAllPreviousCreditLineApprovedInAPeriodForCustomer, times(1)).execute(any(), any());
    }

    @Test
    void shouldCreateTheFirstNotAcceptedCredit() {
        final LocalDateTime currentRequestDate = LocalDateTime.now();

        final CreditLineDomain creditLineToBeCreated = new CreditLineDomain(null, "1234563", FoundingTypeDomain.Startup,
                6550.50, 30500.00, 9900.50, currentRequestDate, null,
                null, null, null);

        when(getAllPreviousCreditLineForCustomer.execute(creditLineToBeCreated.getClientId())).thenReturn(List.of());
        doNothing().when(saveCreditLineForCustomer).execute(any());

        final CreditLineDomain creditLineResponse = createCreditLineForACustomerUseCase.execute(creditLineToBeCreated);

        assertFalse(creditLineResponse.getAccepted());
        assertEquals(0.0, creditLineResponse.getCreditLineAuthorized());
        assertEquals(CreditLineDomain.CREDIT_NOT_AUTHORIZED, creditLineResponse.getMessage());
        verify(saveCreditLineForCustomer, times(1)).execute(any());
        verifyNoInteractions(getAllPreviousCreditLineApprovedInAPeriodForCustomer);
    }

    @Test
    void shouldNotCreateTheSecondNotAcceptedCreditWithin30SecondsAndThrowTooManyRequestAndDoNotSaveTheRequest() {
        final LocalDateTime currentRequestDate = LocalDateTime.now();
        final LocalDateTime firstRequestDate = currentRequestDate.minusSeconds(30);

        final CreditLineDomain firstCreditLineSavedBefore = new CreditLineDomain(null, "1234564", FoundingTypeDomain.SME,
                4550.50, 30500.00, 15000.00, firstRequestDate,
                5100.00, false, 0.0, CreditLineDomain.CREDIT_NOT_AUTHORIZED);

        final CreditLineDomain creditLineToBeCreated = new CreditLineDomain(null, "1234564", FoundingTypeDomain.SME,
                7550.50, 32500.00, 15000.00, currentRequestDate, null,
                null, null, null);

        when(getAllPreviousCreditLineForCustomer.execute(creditLineToBeCreated.getClientId())).thenReturn(List.of(firstCreditLineSavedBefore));

        assertThrows(TooManyRequestsException.class, () -> createCreditLineForACustomerUseCase.execute(creditLineToBeCreated));

        verify(getAllPreviousCreditLineForCustomer, times(1)).execute(any());
        verifyNoInteractions(saveCreditLineForCustomer, getAllPreviousCreditLineApprovedInAPeriodForCustomer);
    }

    @Test
    void shouldCreateTheSecondAcceptedCreditAfterANotAcceptedCreditAfter30Seconds() {
        final LocalDateTime currentRequestDate = LocalDateTime.now();
        final LocalDateTime firstRequestDate = currentRequestDate.minusSeconds(31);

        final CreditLineDomain firstCreditLineSavedBefore = new CreditLineDomain(null, "1234565", FoundingTypeDomain.SME,
                4550.50, 30500.00, 15000.00, firstRequestDate,
                5100.00, false, 0.0, CreditLineDomain.CREDIT_NOT_AUTHORIZED);

        final CreditLineDomain creditLineToBeCreated = new CreditLineDomain(null, "1234565", FoundingTypeDomain.SME,
                6550.50, 40500.00, 5000.00, currentRequestDate, null,
                null, null, null);

        when(getAllPreviousCreditLineForCustomer.execute(creditLineToBeCreated.getClientId())).thenReturn(List.of(firstCreditLineSavedBefore));
        doNothing().when(saveCreditLineForCustomer).execute(any());

        final CreditLineDomain creditLineResponse = createCreditLineForACustomerUseCase.execute(creditLineToBeCreated);

        assertTrue(creditLineResponse.getAccepted());
        assertEquals(creditLineToBeCreated.getRequestedCreditLine(), creditLineResponse.getCreditLineAuthorized());
        assertEquals(CreditLineDomain.CREDIT_AUTHORIZED, creditLineResponse.getMessage());
        verify(saveCreditLineForCustomer, times(1)).execute(any());
        verifyNoInteractions(getAllPreviousCreditLineApprovedInAPeriodForCustomer);
    }

    @Test
    void theClientWillBeContactedByASalesAgentInTheThirdNotAcceptedCredit() {
        final LocalDateTime currentRequestDate = LocalDateTime.now();
        final LocalDateTime firstRequestDate = currentRequestDate.minusSeconds(80);
        final LocalDateTime secondRequestDate = currentRequestDate.minusSeconds(40);

        final CreditLineDomain firstCreditLineSavedBefore = new CreditLineDomain(null, "1234566", FoundingTypeDomain.SME,
                4550.50, 30500.00, 15000.00, firstRequestDate,
                5100.00, false, 0.0, CreditLineDomain.CREDIT_NOT_AUTHORIZED);

        final CreditLineDomain secondCreditLineSavedBefore = new CreditLineDomain(null, "1234566", FoundingTypeDomain.SME,
                5550.50, 30600.00, 15000.00, secondRequestDate,
                5120.00, false, 0.0, CreditLineDomain.CREDIT_NOT_AUTHORIZED);

        final CreditLineDomain creditLineToBeCreated = new CreditLineDomain(null, "1234566", FoundingTypeDomain.SME,
                6550.50, 40500.00, 15500.00, currentRequestDate, null,
                null, null, null);

        when(getAllPreviousCreditLineForCustomer.execute(creditLineToBeCreated.getClientId()))
                .thenReturn(List.of(secondCreditLineSavedBefore, firstCreditLineSavedBefore));
        doNothing().when(saveCreditLineForCustomer).execute(any());

        final CreditLineDomain creditLineResponse = createCreditLineForACustomerUseCase.execute(creditLineToBeCreated);

        assertFalse(creditLineResponse.getAccepted());
        assertEquals(0.0, creditLineResponse.getCreditLineAuthorized());
        assertEquals(CreditLineDomain.SALES_AGENT_MESSAGE, creditLineResponse.getMessage());
        verify(saveCreditLineForCustomer, times(1)).execute(any());
        verifyNoInteractions(getAllPreviousCreditLineApprovedInAPeriodForCustomer);
    }

    @Test
    void theClientWillContinueBeContactedByASalesAgentAfterTheThirdNotAcceptedCreditAndDoNotSaveTheRequest() {
        final LocalDateTime currentRequestDate = LocalDateTime.now();
        final LocalDateTime firstRequestDate = currentRequestDate.minusSeconds(120);
        final LocalDateTime secondRequestDate = currentRequestDate.minusSeconds(80);
        final LocalDateTime thirdRequestDate = currentRequestDate.minusSeconds(40);

        final CreditLineDomain firstCreditLineSavedBefore = new CreditLineDomain(null, "1234567", FoundingTypeDomain.SME,
                4550.50, 30500.00, 15000.00, firstRequestDate,
                5100.00, false, 0.0, CreditLineDomain.CREDIT_NOT_AUTHORIZED);

        final CreditLineDomain secondCreditLineSavedBefore = new CreditLineDomain(null, "1234567", FoundingTypeDomain.SME,
                5550.50, 30600.00, 15000.00, secondRequestDate,
                5120.00, false, 0.0, CreditLineDomain.CREDIT_NOT_AUTHORIZED);

        final CreditLineDomain thirdCreditLineSavedBefore = new CreditLineDomain(null, "1234567", FoundingTypeDomain.SME,
                5650.50, 30600.00, 15000.00, thirdRequestDate,
                5120.00, false, 0.0, CreditLineDomain.SALES_AGENT_MESSAGE);

        final CreditLineDomain creditLineToBeCreated = new CreditLineDomain(null, "1234567", FoundingTypeDomain.SME,
                6550.50, 40500.00, 15500.00, currentRequestDate, null,
                null, null, null);

        when(getAllPreviousCreditLineForCustomer.execute(creditLineToBeCreated.getClientId()))
                .thenReturn(List.of(thirdCreditLineSavedBefore, secondCreditLineSavedBefore, firstCreditLineSavedBefore));

        final CreditLineDomain creditLineResponse = createCreditLineForACustomerUseCase.execute(creditLineToBeCreated);

        assertFalse(creditLineResponse.getAccepted());
        assertEquals(0.0, creditLineResponse.getCreditLineAuthorized());
        assertEquals(CreditLineDomain.SALES_AGENT_MESSAGE, creditLineResponse.getMessage());
        verifyNoInteractions(saveCreditLineForCustomer, getAllPreviousCreditLineApprovedInAPeriodForCustomer);
    }

}