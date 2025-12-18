package com.cotrafa.creditapproval.loanrequest.infrastructure.adapter.out.notification;

import com.cotrafa.creditapproval.loanrequest.domain.model.LoanRequest;
import com.cotrafa.creditapproval.loanrequest.domain.port.out.NotificationPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class LoanNotificationAdapter implements NotificationPort {

    @Override
    public void sendReceivedEmail(LoanRequest request) {
        log.info("📧 EMAIL SENT: Dear customer, your loan request for {} is being reviewed.", request.getAmount());
    }

    @Override
    public void sendApprovedEmail(LoanRequest request) {
        log.info("📧 EMAIL SENT: CONGRATULATIONS! Your loan request {} has been APPROVED.", request.getId());
    }

    @Override
    public void sendRejectedEmail(LoanRequest request) {
        log.info("📧 EMAIL SENT: We regret to inform you that your request {} was REJECTED.", request.getId());
    }
}
