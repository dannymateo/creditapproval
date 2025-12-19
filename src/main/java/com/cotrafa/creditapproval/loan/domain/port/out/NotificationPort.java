package com.cotrafa.creditapproval.loan.domain.port.out;

import com.cotrafa.creditapproval.loan.domain.model.Notification;

public interface NotificationPort {
    void send(Notification notification);
}