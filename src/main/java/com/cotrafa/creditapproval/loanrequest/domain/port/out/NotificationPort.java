package com.cotrafa.creditapproval.loanrequest.domain.port.out;

import com.cotrafa.creditapproval.loanrequest.domain.model.LoanRequest;

public interface NotificationPort {
    void sendReceivedEmail(LoanRequest request);
    void sendApprovedEmail(LoanRequest request);
    void sendRejectedEmail(LoanRequest request);
}
