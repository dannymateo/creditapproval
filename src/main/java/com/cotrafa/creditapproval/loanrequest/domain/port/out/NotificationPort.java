package com.cotrafa.creditapproval.loanrequest.domain.port.out;


import com.cotrafa.creditapproval.loanrequest.domain.model.NotificationData;

public interface NotificationPort {
    void sendReceivedEmail(NotificationData data);
    void sendApprovedEmail(NotificationData data);
    void sendRejectedEmail(NotificationData data);
}
