package com.cotrafa.creditapproval.loan.application.service;

import com.cotrafa.creditapproval.customer.domain.model.Customer;
import com.cotrafa.creditapproval.customer.domain.port.in.GetCustomerUseCase;
import com.cotrafa.creditapproval.loan.domain.model.Loan;
import com.cotrafa.creditapproval.loan.domain.model.Notification;
import com.cotrafa.creditapproval.loan.domain.port.out.LoanRepositoryPort;
import com.cotrafa.creditapproval.loan.domain.port.out.NotificationPort;
import com.cotrafa.creditapproval.loanrequest.domain.model.LoanRequest;
import com.cotrafa.creditapproval.loanrequest.domain.port.out.LoanRequestRepositoryPort;
import com.cotrafa.creditapproval.loanstatus.domain.model.LoanStatus;
import com.cotrafa.creditapproval.loanstatus.domain.port.in.GetLoanStatusUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Unit Test: Loan Application Notification Logic")
public class LoanNotificationTest {

    @Mock
    private NotificationPort notificationPort;

    @Mock
    private LoanRequestRepositoryPort loanRequestRepositoryPort;

    @Mock
    private GetCustomerUseCase customerUseCase;

    @Mock
    private LoanRepositoryPort loanRepositoryPort;

    @Mock
    private GetLoanStatusUseCase loanStatusUseCase;

    @InjectMocks
    private LoanApplicationService loanApplicationService;

    @Test
    @DisplayName("Should trigger the notification port with correct data when a loan is processed")
    void shouldSendNotificationWhenLoanIsCreated() {
        // GIVEN
        UUID requestId = UUID.randomUUID();
        UUID loanId = UUID.randomUUID();

        LoanRequest mockRequest = LoanRequest.builder()
                .id(requestId)
                .customerId(UUID.randomUUID())
                .amount(new BigDecimal("5000000"))
                .annualRate(15.0)
                .termMonths(12)
                .build();

        Loan savedLoanMock = Loan.builder()
                .id(loanId)
                .loanRequestId(requestId)
                .amount(new BigDecimal("5000000"))
                .build();

        when(loanRequestRepositoryPort.findById(requestId)).thenReturn(Optional.of(mockRequest));
        when(customerUseCase.getById(any())).thenReturn(Customer.builder().email("test@test.com").firstName("Juan").build());
        when(loanStatusUseCase.getByName(any())).thenReturn(LoanStatus.builder().id(UUID.randomUUID()).build());
        when(loanRepositoryPort.save(any(Loan.class))).thenReturn(savedLoanMock);

        // WHEN
        loanApplicationService.createFromRequest(requestId);

        // THEN
        ArgumentCaptor<Notification> notificationCaptor = ArgumentCaptor.forClass(Notification.class);
        verify(notificationPort, times(1)).send(notificationCaptor.capture());

        assertEquals("loan-approval-template", notificationCaptor.getValue().getTemplate());
    }
}